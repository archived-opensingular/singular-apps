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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.sql.DataSource;

import com.google.common.base.Joiner;
import org.hibernate.dialect.Dialect;
import org.opensingular.lib.commons.base.SingularException;
import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.persistence.DatabaseObjectNameReplacement;
import org.opensingular.lib.support.persistence.JTDSHibernateDataSourceWrapper;
import org.opensingular.lib.support.persistence.util.SqlUtil;

public class PersistenceConfigurationProvider implements Loggable {

    protected SingularPersistenceConfiguration persistenceConfiguration;

    public PersistenceConfigurationProvider(SingularPersistenceConfiguration configuration) {
        this.persistenceConfiguration = configuration;
    }

    /**
     * This method will create the SingularPersistenceConfiguration using the implementation of the project.
     * If find more than one, or none implementantion this method will return a error.
     */
    public PersistenceConfigurationProvider() {
        try {
            Set<Class<? extends SingularPersistenceConfiguration>> configs = SingularClassPathScanner.get().findSubclassesOf(SingularPersistenceConfiguration.class);
            if (configs.isEmpty()) {
                throw new SingularException(String.format("Implementation of  %s not found. It is not possible to configure persistence properly.", SingularPersistenceConfiguration.class));
            } else if (configs.size() > 1) {
                throw new SingularException(String.format("One or more implementation of  %s was found: %s. It is not possible to configure persistence properly.", SingularPersistenceConfiguration.class, Arrays.toString(configs.toArray())));
            }
            this.persistenceConfiguration = configs.stream().findFirst().get().newInstance();//NOSONAR
        } catch (Exception e) {
            throw SingularException.rethrow(e);
        }
    }

    /**
     * This method will return the packages to Scan witch is implements SingularPersistenceConfiguration.
     * By default all the packages in the Singular will included in the packages to Scan.
     *
     * @param retrieveAll True for retrieveAll packages that will be used in HQL querys;
     *                    False for just the packages that have to create the tables.
     * @return String Array containing the packages to Scan.
     */
    public String[] getPackagesToScan(boolean retrieveAll) {
        PackageScanConfiguration packageScanConfiguration = new PackageScanConfiguration();
        packageScanConfiguration.addPackageToScan("org.opensingular", true);
        packageScanConfiguration.addPackageToScan("com.opensingular", true);
        persistenceConfiguration.configureHibernatePackagesToScan(packageScanConfiguration);
        Set<String> packages;
        if (retrieveAll) {
            packages = packageScanConfiguration.getAllPackagesToScan();
        } else {
            packages = packageScanConfiguration.getPackagesToScan();
        }
        return packages.toArray(new String[packages.size()]);
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
        hibernateProperties.setProperty("hibernate.hbm2ddl.import_files", Joiner.on(",").join(getSQLScritps()));
        hibernateProperties.setProperty("hibernate.hbm2ddl.import_files_sql_extractor", "org.hibernate.tool.hbm2ddl.MultipleLinesSqlCommandExtractor");
        hibernateProperties.setProperty("net.sf.ehcache.configurationResourceName", "/default-singular-ehcache.xml");
        hibernateProperties.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        persistenceConfiguration.configureHibernateProperties(hibernateProperties);
        return hibernateProperties;
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
        scripts.add(persistenceConfiguration.getDatabaseSupport().getQuartzScript());
        return scripts;
    }

    public List<DatabaseObjectNameReplacement> getSchemaReplacements() {
        List<DatabaseObjectNameReplacement> replacements = new ArrayList<>();
        persistenceConfiguration.configureQueryReplacements(replacements);
        return replacements;
    }

    public DataSource getDataSource() {
        DataSource dataSource;
        if (SqlUtil.useEmbeddedDatabase()) {
            dataSource = persistenceConfiguration.getEmbeddedDataSource();
        } else if (SingularDataBaseEnum.MSSQL.isDialectSupported(getDialect())) {
            dataSource = new JTDSHibernateDataSourceWrapper(persistenceConfiguration.getNonEmbeddedDataSource());
        } else {
            dataSource = persistenceConfiguration.getNonEmbeddedDataSource();
        }
        return dataSource;
    }

    public Class<? extends Dialect> getDialect() {
        return persistenceConfiguration.getHibernateDialect();
    }

    public boolean isDropAllH2() {
        return SqlUtil.useEmbeddedDatabase() && isCreateDrop();
    }

    public boolean isCreateDrop() {
        return SqlUtil.isDropCreateDatabase();
    }
}
