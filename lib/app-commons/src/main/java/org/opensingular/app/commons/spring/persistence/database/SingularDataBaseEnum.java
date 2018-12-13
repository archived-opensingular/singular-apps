/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.app.commons.spring.persistence.database;

import java.util.Arrays;
import java.util.List;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.Oracle8iDialect;
import org.hibernate.dialect.PostgreSQL9Dialect;
import org.hibernate.dialect.SQLServerDialect;
import org.opensingular.lib.commons.base.SingularException;

/**
 * A Enum responsible to contain the insert's scripts to especific Dialect.
 * Should have the function's creation script, and the actor table.
 */
public enum SingularDataBaseEnum implements SingularDataBaseSuport {


    ORACLE(Oracle8iDialect.class, "db/ddl/oracle/create-table-actor.sql", "db/ddl/oracle/create-quartz.sql", "db/ddl/oracle/create-function.sql"),
    MSSQL(SQLServerDialect.class, "db/ddl/sqlserver/create-table-actor.sql", "db/ddl/sqlserver/create-quartz.sql", "db/ddl/sqlserver/create-function.sql"),
    H2(H2Dialect.class, "db/ddl/h2/create-table-actor.sql", "db/ddl/h2/create-quartz.sql", "db/ddl/h2/create-function.sql"),
    PGSQL(PostgreSQL9Dialect.class, "db/ddl/postgres/create-table-actor.sql", "db/ddl/postgres/create-quartz.sql", "db/ddl/postgres/create-function.sql");

    private String actorScript;
    private String quartzScript;
    private List<String> scripts;
    private Class<? extends Dialect> dialect;

    SingularDataBaseEnum(Class<? extends Dialect> dialect, String defaultActorScript, String quartzScript, String... scripts) {
        this.dialect = dialect;
        this.actorScript = defaultActorScript;
        this.quartzScript = quartzScript;
        this.scripts = Arrays.asList(scripts);
    }

    @Override
    public List<String> getScripts() {
        return scripts;
    }

    public boolean isDialectSupported(Class<? extends Dialect> dialect) {
        return this.dialect.isAssignableFrom(dialect);
    }

    public String getDefaultActorScript() {
        return actorScript;
    }

    @Override
    public String getQuartzScript() {
        return quartzScript;
    }

    public static SingularDataBaseSuport getForDialect(Class<? extends Dialect> hibernateDialect) {
        for (SingularDataBaseEnum singularDataBaseEnum : SingularDataBaseEnum.values()) {
            if (singularDataBaseEnum.isDialectSupported(hibernateDialect)) {
                return singularDataBaseEnum;
            }
        }
        throw new SingularException(String.format("Database dialect %s not supported.", hibernateDialect));
    }
}
