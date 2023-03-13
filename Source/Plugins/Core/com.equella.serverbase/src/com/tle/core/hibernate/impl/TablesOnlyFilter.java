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

package com.tle.core.hibernate.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.boot.model.relational.AuxiliaryDatabaseObject;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.Index;
import org.hibernate.mapping.Table;

public class TablesOnlyFilter implements HibernateCreationFilter {
  private Set<String> tables;

  public TablesOnlyFilter(String... tables) {
    this.tables = new HashSet<String>();
    Collections.addAll(this.tables, tables);
  }

  @Override
  public boolean includeForeignKey(Table table, ForeignKey fk) {
    return includeTable(table);
  }

  @Override
  public boolean includeIndex(Table table, Index index) {
    return includeTable(table);
  }

  @Override
  public boolean includeObject(AuxiliaryDatabaseObject object) {
    return false;
  }

  @Override
  public boolean includeTable(Table table) {
    return tables.contains(table.getName());
  }
}
