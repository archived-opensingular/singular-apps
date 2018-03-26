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

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.persistence.entity.SingularEntityInterceptor;
import org.opensingular.lib.support.persistence.util.SqlUtil;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.spring.database.AbstractResourceDatabasePopulator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static org.opensingular.lib.commons.base.SingularProperties.CUSTOM_SCHEMA_NAME;
import static org.opensingular.lib.commons.base.SingularProperties.JNDI_DATASOURCE;

@EnableTransactionManagement(proxyTargetClass = true)
public class SingularDefaultPersistenceConfiguration implements Loggable {


    @Bean
    public DataSourceInitializer scriptsInitializer(final DataSource dataSource,
            final AbstractResourceDatabasePopulator databasePopulator) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        databasePopulator.setSqlScriptEncoding(StandardCharsets.UTF_8.name());
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator);
        initializer.setEnabled(getConfigureDatabaseResource().isDatabaseInitializerEnabled());
        return initializer;
    }

    /**
     * Responsavel por criar um DataBasePopulator de acordo com o dialect informado.
     * Favor olhar o metodo getSupportedDatabases() para maiores informações.
     *
     * @return retorna o dataBasePopulator especifico de acordo com o dialect.
     */
    @Bean
    private AbstractResourceDatabasePopulator databasePopulator() {
        return getConfigureDatabaseResource().databasePopulator();
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
        DataSource dataSource = null;
        JndiTemplate jndi = new JndiTemplate();
        String dataSourceName = SingularProperties.get(JNDI_DATASOURCE, "java:jboss/datasources/singular");
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
            dataSource.setJdbcUrl(getConfigureDatabaseResource().getUrlConnection());

            dataSource.setUsername("sa");
            dataSource.setPassword("sa");
            dataSource.setDriverClassName("org.h2.Driver");

            return dataSource;
        } catch (Exception e) {
            throw SingularServerException.rethrow(e.getMessage(), e);
        }
    }


    @Bean
    @DependsOn("scriptsInitializer")
    public LocalSessionFactoryBean sessionFactory(final DataSource dataSource) {


        final LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setHibernateProperties(getConfigureDatabaseResource().getHibernateProperties());
        sessionFactoryBean.setPackagesToScan(getConfigureDatabaseResource().getHibernatePackagesToScan());
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

    /**
     * Metodo para ser sobrescito, para que cada projeto possa conter seu proprio dataBaseResource com as configurações
     * do hibernate properties especificas.
     * @return
     */
    protected ConfigureDatabaseResource getConfigureDatabaseResource() {
        return new ConfigureDatabaseResource();
    }


}
