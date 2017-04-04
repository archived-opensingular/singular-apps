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

package org.opensingular.server.commons.spring;

import org.hibernate.SessionFactory;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.support.persistence.entity.EntityInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static org.opensingular.lib.commons.base.SingularProperties.CUSTOM_SCHEMA_NAME;
import static org.opensingular.lib.commons.base.SingularProperties.JNDI_DATASOURCE;
import static org.opensingular.lib.commons.base.SingularProperties.USE_EMBEDDED_DATABASE;

@EnableTransactionManagement(proxyTargetClass = true)
public class SingularDefaultPersistenceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingularDefaultPersistenceConfiguration.class);

    @Value("classpath:db/ddl/drops.sql")
    protected Resource drops;
    @Value("classpath:db/ddl/create-tables-form.sql")
    protected Resource sqlCreateTablesForm;
    @Value("classpath:db/ddl/create-tables.sql")
    protected Resource sqlCreateTables;
    @Value("classpath:db/ddl/create-constraints.sql")
    protected Resource sqlCreateConstraints;
    @Value("classpath:db/ddl/create-constraints-form.sql")
    protected Resource sqlCreateConstraintsForm;
    @Value("classpath:db/ddl/create-sequences-form.sql")
    protected Resource sqlCreateSequencesForm;
    @Value("classpath:db/ddl/create-function.sql")
    private   Resource sqlCreateFunction;
    @Value("classpath:db/ddl/create-tables-actor.sql")
    private   Resource sqlCreateTablesActor;
    @Value("classpath:db/ddl/create-sequences-server.sql")
    private   Resource sqlCreateSequencesServer;
    @Value("classpath:db/dml/insert-flow-data.sql")
    private   Resource insertDadosSingular;

    protected ResourceDatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.setSqlScriptEncoding(StandardCharsets.UTF_8.name());
        populator.addScript(drops);
        populator.addScript(sqlCreateTablesForm);
        populator.addScript(sqlCreateTables);
        populator.addScript(sqlCreateTablesActor);
        populator.addScript(sqlCreateSequencesServer);
        populator.addScript(sqlCreateSequencesForm);
        populator.addScript(sqlCreateConstraints);
        populator.addScript(sqlCreateConstraintsForm);
        populator.addScript(insertDadosSingular);
        return populator;
    }

    @Bean
    public DataSourceInitializer scriptsInitializer(final DataSource dataSource) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator());
        initializer.setEnabled(isDatabaseInitializerEnabled());
        return initializer;
    }

    @Bean
    @DependsOn("scriptsInitializer")
    public DataSourceInitializer createFunctionInitializer(final DataSource dataSource) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.setSeparator("#");
        populator.setSqlScriptEncoding(StandardCharsets.UTF_8.name());
        populator.addScript(sqlCreateFunction);
        initializer.setDatabasePopulator(populator);
        initializer.setEnabled(isDatabaseInitializerEnabled());
        return initializer;
    }

    @Bean
    public DataSource dataSource() {
        if (SingularProperties.get().isTrue(USE_EMBEDDED_DATABASE)) {
            return embeddedDataSourceConfiguration();
        } else if (SingularProperties.get().isFalse(USE_EMBEDDED_DATABASE) || SingularProperties.get().isFalse(SingularProperties.SINGULAR_DEV_MODE)) {
            return jndiDataSourceConfiguration();
        } else {
            return embeddedDataSourceConfiguration();
        }
    }

    protected DataSource jndiDataSourceConfiguration() {
        LOGGER.info("Usando datasource configurado via JNDI");
        DataSource   dataSource     = null;
        JndiTemplate jndi           = new JndiTemplate();
        String       dataSourceName = SingularProperties.get().getProperty(JNDI_DATASOURCE, "java:jboss/datasources/singular");
        try {
            dataSource = (DataSource) jndi.lookup(dataSourceName);
        } catch (NamingException e) {
            LOGGER.error(String.format("Datasource %s not found.", dataSourceName), e);
        }
        return dataSource;
    }

    protected DataSource embeddedDataSourceConfiguration() {
        LOGGER.warn("Usando datasource banco embarcado H2");
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(getUrlConnection());

        dataSource.setUsername("sa");
        dataSource.setPassword("sa");
        dataSource.setDriverClassName("org.h2.Driver");

        final Properties connectionProperties = new Properties();
        connectionProperties.setProperty("removeAbandoned", "true");
        connectionProperties.setProperty("initialSize", "5");
        connectionProperties.setProperty("maxActive", "10");
        connectionProperties.setProperty("minIdle", "1");
        dataSource.setConnectionProperties(connectionProperties);
        return dataSource;
    }

    protected String getUrlConnection() {
        return "jdbc:h2:./singularserverdb;AUTO_SERVER=TRUE;mode=ORACLE;CACHE_SIZE=4096;EARLY_FILTER=1;MULTI_THREADED=1;LOCK_TIMEOUT=15000;";
    }

    @DependsOn("scriptsInitializer")
    @Bean
    public LocalSessionFactoryBean sessionFactory(final DataSource dataSource) {
        final LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setHibernateProperties(hibernateProperties());
        sessionFactoryBean.setPackagesToScan(hibernatePackagesToScan());
        if (SingularProperties.get().containsKey(CUSTOM_SCHEMA_NAME)) {
            LOGGER.info("Utilizando schema customizado: {}", SingularProperties.get().getProperty(CUSTOM_SCHEMA_NAME));
            sessionFactoryBean.setEntityInterceptor(new EntityInterceptor());
        }
        return sessionFactoryBean;
    }

    @Bean
    public HibernateTransactionManager transactionManager(final SessionFactory sessionFactory, final DataSource dataSource) {
        final HibernateTransactionManager tx = new HibernateTransactionManager(sessionFactory);
        tx.setDataSource(dataSource);
        return tx;
    }


    protected String[] hibernatePackagesToScan() {
        return new String[]{
                "org.opensingular.flow.persistence.entity",
                "org.opensingular.server.commons.persistence.entity",
                "org.opensingular.form.persistence.entity"};
    }

    protected Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
        hibernateProperties.setProperty("hibernate.connection.isolation", "2");
        hibernateProperties.setProperty("hibernate.jdbc.batch_size", "30");
        hibernateProperties.setProperty("hibernate.show_sql", "false");
        hibernateProperties.setProperty("hibernate.format_sql", "true");
        hibernateProperties.setProperty("hibernate.enable_lazy_load_no_trans", "true");
        hibernateProperties.setProperty("hibernate.jdbc.use_get_generated_keys", "true");
        hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", "true");
        hibernateProperties.setProperty("hibernate.cache.use_query_cache", "true");
        /*não utilizar a singleton region factory para não conflitar com o cache do singular-server */
        hibernateProperties.setProperty("net.sf.ehcache.configurationResourceName", "/default-singular-ehcache.xml");
        hibernateProperties.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        return hibernateProperties;
    }

    protected boolean isDatabaseInitializerEnabled() {
        return !SingularProperties.get().isFalse("singular.enabled.h2.inserts");
    }
}
