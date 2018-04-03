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
import org.apache.commons.lang3.StringUtils;
import org.opensingular.lib.commons.base.SingularException;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.persistence.util.SqlUtil;
import org.springframework.jdbc.datasource.DelegatingDataSource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class DefaultH2DataSource extends DelegatingDataSource implements Loggable {

    private Map<String, String> options = new HashMap<>();
    private String jdbcURL;

    public DefaultH2DataSource(boolean dropAllObjects, String jdbcURL) {
        this.jdbcURL = jdbcURL;
        if (dropAllObjects) {
            options.put("INIT", "DROP ALL OBJECTS; ");
        }
        addToInit("CREATE SCHEMA if not exists DBSINGULAR;");
    }

    public DefaultH2DataSource() {
        this(SqlUtil.isDropCreateDatabase(), "jdbc:h2:file:./singulardb");
        setAutoServer(true);
        setCacheSize(4096);
        setEarlyFilter(true);
        setMultiThreaded(true);
        setLockTimeout(15000);
    }

    private String getJdbcUrl() {
        StringBuilder renderedOptions = new StringBuilder();
        for (Map.Entry<String, String> opt : options.entrySet()) {
            renderedOptions.append(opt.getKey()).append('=').append(escapeSemiColons(opt.getValue())).append(';');
        }
        if (!jdbcURL.endsWith(";") && renderedOptions.length() > 0) {
            jdbcURL = jdbcURL + ";";
        }
        return jdbcURL + renderedOptions.toString();
    }

    public DefaultH2DataSource addToInit(String sqlToAppend) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.defaultString(options.get("INIT")));
        sb.append(sqlToAppend);
        if (!sqlToAppend.endsWith(";")) {
            sb.append(";");
        }
        options.put("INIT", sb.toString());
        return this;
    }

    private String escapeSemiColons(String sqlToEscape) {
        return sqlToEscape.replaceAll(";", "\\\\;");
    }

    public DefaultH2DataSource setAutoServer(boolean autoServer) {
        if (autoServer) {
            options.put("AUTO_SERVER", "TRUE");
        } else {
            options.put("AUTO_SERVER", "FALSE");
        }
        return this;
    }

    public DefaultH2DataSource setEarlyFilter(boolean earlyFilter) {
        if (earlyFilter) {
            options.put("EARLY_FILTER", "TRUE");
        } else {
            options.put("EARLY_FILTER", "FALSE");
        }
        return this;
    }

    public DefaultH2DataSource setMultiThreaded(boolean multiThreaded) {
        if (multiThreaded) {
            options.put("MULTI_THREADED", "TRUE");
        } else {
            options.put("MULTI_THREADED", "FALSE");
        }
        return this;
    }

    public DefaultH2DataSource setCacheSize(int cacheSize) {
        options.put("CACHE_SIZE", String.valueOf(cacheSize));
        return this;
    }

    public DefaultH2DataSource setLockTimeout(int lockTimeout) {
        options.put("LOCK_TIMEOUT", String.valueOf(lockTimeout));
        return this;
    }

    public DefaultH2DataSource setMode(String mode) {
        options.put("MODE", mode);
        return this;
    }


    @PostConstruct
    protected void init() {
        setTargetDataSource(embeddedDataSourceConfiguration());
    }

    protected DataSource embeddedDataSourceConfiguration() {
        try {
            getLogger().warn("Using h2 embbeded data source");
            HikariDataSource dataSource = new HikariDataSource();//NOSONAR
            String           jdbcURL    = getJdbcUrl();
            getLogger().info("H2 CONNECTION URL: {}", jdbcURL);
            dataSource.setJdbcUrl(jdbcURL);

            dataSource.setUsername("sa");
            dataSource.setPassword("sa");
            dataSource.setDriverClassName(org.h2.Driver.class.getName());

            return dataSource;
        } catch (Exception e) {
            throw SingularException.rethrow(e.getMessage(), e);
        }
    }

}
