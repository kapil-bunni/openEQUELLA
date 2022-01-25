/*
 * Licensed to The Apereo Foundation under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * The Apereo Foundation licenses this file to you under the Apache License,
 * Version 2.0, (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tle.core.security

import cats.data.{Kleisli, StateT}
import cats.effect.IO
import cats.syntax.applicative._
import com.tle.common.security.SecurityConstants
import com.tle.common.security.SecurityConstants.GRANT
import com.tle.core.db.{DB, UserContext}
import com.tle.exceptions.PrivilegeRequiredException
import com.tle.legacy.LegacyGuice
import io.doolse.simpledba.jdbc._

import scala.collection.JavaConverters._

object AclChecks {
  // General note on the queries in this class - With the advent of hibernate 5,
  // queries with '?' in them need to be ordinal ( ie `?4` ).  However, this class
  // does not leverage the JPA / Hibernate logic, so we can leave the `?`s as-is.

  private val OBJECT_PRIORITY_DB_FORMAT =
    f"${SecurityConstants.PRIORITY_OBJECT_INSTANCE + SecurityConstants.PRIORITY_MAX}%04d"

  def createSQL(privs: Iterable[String], exps: Iterable[_]) = {
    def qs(is: Iterable[_]): String = is.map(_ => "?").mkString("(", ", ", ")")

    s"SELECT MAX(ae.aggregate_ordering), ae.privilege, ae.target_object FROM access_entry ae WHERE ae.institution_id = ? " +
      s"AND ae.privilege IN ${qs(privs)} AND ae.expression_id IN ${qs(exps)} " +
      s"GROUP BY ae.target_object, ae.privilege, ae.aggregate_ordering ORDER BY ae.aggregate_ordering DESC"
  }

  def ensureOnePriv[A](privs: String*)(db: DB[A]): DB[A] =
    for {
      actualPrivs <- filterNonGrantedPrivileges(privs, true)
      r <- if (actualPrivs.isEmpty) throw new PrivilegeRequiredException(privs.asJavaCollection)
      else db
    } yield r

  case class PrivEntry(agg: String, priv: String, target: String)

  case class PrivState(granted: Set[String] = Set.empty,
                       revokedOverall: Set[String] = Set.empty,
                       revokedPerObj: Map[String, Set[String]] = Map.empty)

  def filterNonGrantedPrivileges(privileges: Iterable[String],
                                 includePossibleOwnerAcls: Boolean): DB[Set[String]] = Kleisli {
    uc: UserContext =>
      val currentUser = uc.user
      if (currentUser.isSystem || privileges.isEmpty) privileges.toSet.pure[JDBCIO]
      else {
        val _exps = currentUser.getCommonAclExpressions.asScala ++ currentUser.getNotOwnerAclExpressions.asScala
        val exps =
          if (includePossibleOwnerAcls) _exps ++ currentUser.getOwnerAclExpressions.asScala
          else _exps

        def processPriv(s: PrivState, entry: PrivEntry): PrivState = {
          val priv     = entry.priv
          val target   = entry.target
          val objEntry = entry.agg.substring(0, 4) == OBJECT_PRIORITY_DB_FORMAT
          if (s.granted(priv) || s
                .revokedOverall(priv) || (objEntry && s.revokedPerObj.get(target).exists(_(priv))))
            s
          else if (entry.agg.endsWith(Character.toString(GRANT))) s.copy(granted = s.granted + priv)
          else if (!objEntry) s.copy(revokedOverall = s.revokedOverall + priv)
          else
            s.copy(revokedPerObj =
              s.revokedPerObj.updated(target, s.revokedPerObj.getOrElse(target, Set.empty) + priv))
        }

        JDBCQueries
          .rowsStream(
            StateT.inspectF { con =>
              IO {
                val ps = con.prepareStatement(createSQL(privileges, exps))
                ps.setLong(1, uc.inst.getDatabaseId)
                privileges.zipWithIndex.foreach { case (p, i) => ps.setString(2 + i, p) }
                exps.zipWithIndex.foreach { case (e, i)       => ps.setLong(2 + privileges.size + i, e) }
                ps.executeQuery()
              }
            }
          )
          .map { rs =>
            PrivEntry(rs.getString(1), rs.getString(2), rs.getString(3))
          }
          .fold(PrivState())(processPriv)
          .compile
          .last
          .map {
            _.map(_.granted).getOrElse(Set.empty)
          }
      }
  }

  /**
   * Checks if the current user has the specified ACL.
   *
   * @param privilege Required ACL, typically defined as a constant in `com.tle.common.security.SecurityConstants`
   * @param includePossibleOwnerAcls `true` to include possible owner ACLs
   */
  def hasAcl(privilege: String, includePossibleOwnerAcls: Boolean = false): Boolean = {
    LegacyGuice.aclManager.hasPrivilege(Set(privilege).asJava, includePossibleOwnerAcls)
  }

  /**
   * Checks if the current user has the specified ACL or throws `PrivilegeRequiredException`.
   *
   * @param privilege Required ACL, typically defined as a constant in `com.tle.common.security.SecurityConstants`
   * @param includePossibleOwnerAcls `true` to include possible owner ACLs
   */
  def hasAclOrThrow(privilege: String, includePossibleOwnerAcls: Boolean = false): Unit = {
    if (!hasAcl(privilege, includePossibleOwnerAcls)) {
      throw new PrivilegeRequiredException(privilege)
    }
  }
}
