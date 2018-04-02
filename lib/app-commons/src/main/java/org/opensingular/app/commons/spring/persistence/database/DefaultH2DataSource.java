/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.app.commons.spring.persistence.database;

import com.zaxxer.hikari.HikariDataSource;
import org.opensingular.lib.commons.base.SingularException;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.persistence.util.SqlUtil;
import org.springframework.jdbc.datasource.DelegatingDataSource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

public class DefaultH2DataSource extends DelegatingDataSource implements Loggable {

    private String init = "CREATE SCHEMA if not exists DBSINGULAR;";
    private final String jdbcURL;

    public DefaultH2DataSource(boolean dropAllObjects, String appendToInitScript, String jdbcURL) {
        this.init = init + appendToInitScript;
        if (dropAllObjects) {
            init = "DROP ALL OBJECTS; " + init;
        }
        this.jdbcURL = jdbcURL + "INIT=" + init.replaceAll(";","\\\\;") + ";";
    }

    public DefaultH2DataSource() {
        this(SqlUtil.isDropCreateDatabase(), "", "jdbc:h2:file:./singulardb;AUTO_SERVER=TRUE;CACHE_SIZE=4096;EARLY_FILTER=1;MULTI_THREADED=1;LOCK_TIMEOUT=15000;");
    }

    @PostConstruct
    protected void init() {
        setTargetDataSource(embeddedDataSourceConfiguration());
    }

    protected DataSource embeddedDataSourceConfiguration() {
        try {
            getLogger().warn("Usando datasource banco embarcado H2");
            HikariDataSource dataSource = new HikariDataSource();//NOSONAR
            dataSource.setJdbcUrl(jdbcURL);

            dataSource.setUsername("sa");
            dataSource.setPassword("sa");
            dataSource.setDriverClassName("org.h2.Driver");

            return dataSource;
        } catch (Exception e) {
            throw SingularException.rethrow(e.getMessage(), e);
        }
    }

}
