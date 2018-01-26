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

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.persistence.entity.SingularEntityInterceptor;
import org.opensingular.lib.support.persistence.util.SqlUtil;
import org.opensingular.server.commons.exception.SingularServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Properties;

import static org.opensingular.lib.commons.base.SingularProperties.CUSTOM_SCHEMA_NAME;
import static org.opensingular.lib.commons.base.SingularProperties.JNDI_DATASOURCE;

@EnableTransactionManagement(proxyTargetClass = true)
public class SingularDefaultPersistenceConfiguration implements Loggable {

    @Value("classpath:db/ddl/drops.sql")
    private Resource drops;

    @Value("classpath:db/ddl/create-tables-form.sql")
    private Resource sqlCreateTablesForm;

    @Value("classpath:db/ddl/create-tables.sql")
    private Resource sqlCreateTables;

    @Value("classpath:db/ddl/create-tables-emails.sql")
    private Resource sqlCreateTablesEmail;

    @Value("classpath:db/ddl/create-constraints.sql")
    private Resource sqlCreateConstraints;

    @Value("classpath:db/ddl/create-constraints-form.sql")
    private Resource sqlCreateConstraintsForm;

    @Value("classpath:db/ddl/create-sequences-form.sql")
    private Resource sqlCreateSequencesForm;

    @Value("classpath:db/ddl/create-function.sql")
    private Resource sqlCreateFunction;

    @Value("classpath:db/ddl/create-tables-actor.sql")
    private Resource sqlCreateTablesActor;

    @Value("classpath:db/ddl/create-sequences-server.sql")
    private Resource sqlCreateSequencesServer;

    @Value("classpath:db/ddl/create-email-sequences.sql")
    private Resource sqlCreateSequencesEmail;

    @Value("classpath:db/dml/insert-flow-data.sql")
    private Resource insertSingularData;

    protected ResourceDatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.setSqlScriptEncoding(StandardCharsets.UTF_8.name());
        populator.addScript(drops);
        populator.addScript(sqlCreateTablesForm);
        populator.addScript(sqlCreateTables);
        populator.addScript(sqlCreateTablesEmail);
        populator.addScript(sqlCreateTablesActor);
        populator.addScript(sqlCreateSequencesServer);
        populator.addScript(sqlCreateSequencesForm);
        populator.addScript(sqlCreateSequencesEmail);
        populator.addScript(sqlCreateConstraints);
        populator.addScript(sqlCreateConstraintsForm);
        populator.addScript(insertSingularData);
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
        if (SqlUtil.useEmbeddedDatabase()) {
            return embeddedDataSourceConfiguration();
        } else {
            return jndiDataSourceConfiguration();
        }
    }

    protected DataSource jndiDataSourceConfiguration() {
        getLogger().info("Usando datasource configurado via JNDI");
        DataSource   dataSource     = null;
        JndiTemplate jndi           = new JndiTemplate();
        String       dataSourceName = SingularProperties.get(JNDI_DATASOURCE, "java:jboss/datasources/singular");
        try {
            dataSource = (DataSource) jndi.lookup(dataSourceName);
        } catch (NamingException e) {
            getLogger().error(String.format("Datasource %s not found.", dataSourceName), e);
        }
        return dataSource;
    }

    protected DataSource embeddedDataSourceConfiguration() {
        try {
            getLogger().warn("Usando datasource banco embarcado H2");
            HikariDataSource dataSource = new HikariDataSource();//NOSONAR
            dataSource.setJdbcUrl(getUrlConnection());

            dataSource.setUsername("sa");
            dataSource.setPassword("sa");
            dataSource.setDriverClassName("org.h2.Driver");

            return dataSource;
        } catch (Exception e) {
            throw SingularServerException.rethrow(e.getMessage(), e);
        }
    }

    protected String getUrlConnection() {
        return "jdbc:h2:./singularserverdb;AUTO_SERVER=TRUE;mode=ORACLE;CACHE_SIZE=4096;EARLY_FILTER=1;MULTI_THREADED=1;LOCK_TIMEOUT=15000;";
    }

    @DependsOn("createFunctionInitializer")
    @Bean
    public LocalSessionFactoryBean sessionFactory(final DataSource dataSource) {
        final LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setHibernateProperties(hibernateProperties());
        sessionFactoryBean.setPackagesToScan(hibernatePackagesToScan());
        Optional<String> schemaName = SingularProperties.getOpt(CUSTOM_SCHEMA_NAME);
        if (schemaName.isPresent()) {
            sessionFactoryBean.setEntityInterceptor(new SingularEntityInterceptor());
            getLogger().info("Utilizando schema customizado: {}", schemaName.get());
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
                "org.opensingular.app.commons.persistence.entity",
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
