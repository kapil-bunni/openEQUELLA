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

package com.tle.core.hibernate.type;

import com.google.inject.Inject;
import com.tle.core.hibernate.ExtendedDialect;
import com.tle.core.xml.service.XmlService;
import com.tle.hibernate.dialect.ExtendedPostgresDialect;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

public class ImmutableHibernateXStreamType implements UserType {
  // XStream is thread-safe, and this class gets instantiated **a lot**.
  @Inject private static XmlService xstream;
  private final ExtendedDialect dialect;

  public ImmutableHibernateXStreamType(ExtendedDialect dialect) {
    this.dialect = dialect;
  }

  @Override
  public int[] sqlTypes() {
    int sqlType = Types.CLOB;

    // Since Hibernate 5.6, the DDL type for `CLOB` is `oid` instead of `text` in Postgres, which
    // can cause
    // PSQLException: ERROR: column "xxx" is of type oid but expression is of type character
    // varying.
    // Therefore, we use `LONGVARCHAR` instead for Postgres.
    if (dialect instanceof ExtendedPostgresDialect) {
      sqlType = Types.LONGVARCHAR;
    }

    return new int[] {sqlType};
  }

  @Override
  public Class<?> returnedClass() {
    return Serializable.class;
  }

  // We inherit this method, so won't quibble about its name
  @Override
  public boolean equals(Object x, Object y) throws HibernateException // NOSONAR
      {
    return Objects.equals(x, y);
  }

  @Override
  public int hashCode(Object x) throws HibernateException {
    return x.hashCode();
  }

  @Override
  public Object nullSafeGet(
      ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
      throws SQLException {
    // SharedSessionContractImplementor was included with SpringHib5,
    //  but doesn't look to be needed here.
    Reader reader = rs.getCharacterStream(names[0]);
    if (reader == null) {
      return null;
    }
    StringBuilder result = new StringBuilder(4096);
    try {
      char[] charbuf = new char[4096];
      for (int i = reader.read(charbuf); i > 0; i = reader.read(charbuf)) {
        result.append(charbuf, 0, i);
      }
    } catch (IOException e) {
      throw new SQLException(e.getMessage());
    }
    // wtf??
    String xml = result.toString();
    if (xml.trim().length() == 0) {
      return null;
    }
    return xstream.deserialiseFromXml(getClass().getClassLoader(), xml);
  }

  @Override
  public void nullSafeSet(
      PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
      throws SQLException {
    // SharedSessionContractImplementor was included with SpringHib5,
    //  but doesn't look to be needed here.
    if (value != null) {
      String string = xstream.serialiseToXml(value);
      StringReader reader = new StringReader(string);
      st.setCharacterStream(index, reader, string.length());
    } else {
      st.setNull(index, sqlTypes()[0]);
    }
  }

  @Override
  public Object deepCopy(Object value) throws HibernateException {
    return value;
  }

  @Override
  public boolean isMutable() {
    return false;
  }

  @Override
  public Serializable disassemble(Object value) throws HibernateException {
    return (Serializable) deepCopy(value);
  }

  @Override
  public Object assemble(Serializable cached, Object owner) throws HibernateException {
    return deepCopy(cached);
  }

  @Override
  public Object replace(Object original, Object target, Object owner) throws HibernateException {
    return original;
  }
}
