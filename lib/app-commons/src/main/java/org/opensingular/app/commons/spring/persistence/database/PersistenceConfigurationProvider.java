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

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.dialect.Dialect;
import org.opensingular.lib.commons.base.SingularException;
import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.persistence.DatabaseObjectNameReplacement;
import org.opensingular.lib.support.persistence.JTDSHibernateDataSourceWrapper;
import org.opensingular.lib.support.persistence.util.SqlUtil;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class PersistenceConfigurationProvider implements Loggable {

    @Deprecated
    private Boolean isSingularModule;

    {
        try {
            Properties p = new Properties();
            p.load(Thread.currentThread().getContextClassLoader().getResource("/_singular_core_server.properties").openStream());
            isSingularModule = !BooleanUtils.toBoolean(p.getProperty("singular.server"));
        } catch (Exception e) {
            isSingularModule = true;
            getLogger().trace(e.getMessage(), e);
        }
    }


    /**
     * @return
     * @deprecated para ser removido tão logo seja eliminada a solução multi módulo em favor do single-app
     */
    @Deprecated
    private boolean isCreateDrop() {
        return isSingularModule && SqlUtil.isDropCreateDatabase();
    }

    private SingularPersistenceConfiguration persistenceConfiguration;

    public PersistenceConfigurationProvider(SingularPersistenceConfiguration configuration) {
        this.persistenceConfiguration = configuration;
    }

    public PersistenceConfigurationProvider() {
        try {
            Set<Class<? extends SingularPersistenceConfiguration>> configs = SingularClassPathScanner.get().findSubclassesOf(SingularPersistenceConfiguration.class);
            if (configs.size() < 1) {
                throw new SingularException(String.format("Implementation of  %s not found. It is not possible to configure persistence properly.", SingularPersistenceConfiguration.class));
            } else if (configs.size() > 1) {
                throw new SingularException(String.format("One or more implementation of  %s was found: %s. It is not possible to configure persistence properly.", SingularPersistenceConfiguration.class, Arrays.toString(configs.toArray())));
            }
            this.persistenceConfiguration = configs.stream().findFirst().get().newInstance();
        } catch (Exception e) {
            throw SingularException.rethrow(e);
        }
    }

    public String[] getPackagesToScan() {
        List<String> packagesToScan = Lists.newArrayList("org.opensingular", "com.opensingular");
        persistenceConfiguration.configureHibernatePackagesToScan(packagesToScan);
        return packagesToScan.toArray(new String[packagesToScan.size()]);
    }

    public Properties getHibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", getDialect().getName());
        hibernateProperties.setProperty("hibernate.connection.isolation", "2");
        hibernateProperties.setProperty("hibernate.jdbc.batch_size", "30");
        hibernateProperties.setProperty("hibernate.show_sql", "false");
        hibernateProperties.setProperty("hibernate.format_sql", "true");
        hibernateProperties.setProperty("hibernate.enable_lazy_load_no_trans", "true");
        hibernateProperties.setProperty("hibernate.jdbc.use_get_generated_keys", "true");
        hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", "true");
        hibernateProperties.setProperty("hibernate.cache.use_query_cache", "true");
        hibernateProperties.setProperty("hibernate.hbm2ddl.import_files", Joiner.on(", ").join(getSQLScritps()));
        hibernateProperties.setProperty("hibernate.hbm2ddl.import_files_sql_extractor", "org.hibernate.tool.hbm2ddl.MultipleLinesSqlCommandExtractor");
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", isCreateDrop() ? "create" : "none");
        hibernateProperties.setProperty("net.sf.ehcache.configurationResourceName", "/default-singular-ehcache.xml");
        hibernateProperties.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        persistenceConfiguration.configureHibernateProperties(hibernateProperties);
        return hibernateProperties;
    }

    public Class<? extends Dialect> getDialect() {
        return persistenceConfiguration.getHibernateDialect();
    }

    public List<String> getSQLScritps() {
        List<String> scripts = new ArrayList<>();
        if (SqlUtil.useEmbeddedDatabase()) {
            scripts.addAll(SingularDataBaseEnum.H2.getScripts());
        } else {
            scripts.addAll(persistenceConfiguration.getDatabaseSupport().getScripts());
        }
        persistenceConfiguration.configureInitSQLScripts(scripts);
        scripts.add(persistenceConfiguration.getActorTableScript());
        return scripts;
    }

    public List<DatabaseObjectNameReplacement> getSchemaReplacements() {
        return persistenceConfiguration.getSchemaReplacements();
    }

    public DataSource getDataSource() {
        DataSource dataSource;
        if (SqlUtil.useEmbeddedDatabase()) {
            dataSource = persistenceConfiguration.getEmbeddedDataSource();
            EmbeddedDataSource embeddedDataSource = (EmbeddedDataSource) dataSource;
            if (embeddedDataSource.isCreateDropSet() && !isSingularModule) {
                embeddedDataSource.setCreateDrop(false);
            } else if (!embeddedDataSource.isCreateDropSet()) {
                embeddedDataSource.setCreateDrop(isCreateDrop());
            }
        } else {
            dataSource = persistenceConfiguration.getNonEmbeddedDataSource();
        }
        if (SingularDataBaseEnum.MSSQL.isDialectSupported(getDialect())) {
            dataSource = new JTDSHibernateDataSourceWrapper(dataSource);
        }
        return dataSource;
    }
}
