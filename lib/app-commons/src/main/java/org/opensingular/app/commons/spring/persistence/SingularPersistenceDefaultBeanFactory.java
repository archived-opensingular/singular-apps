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

package org.opensingular.app.commons.spring.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.opensingular.app.commons.spring.persistence.database.H2DropAllObjectsPopulator;
import org.opensingular.app.commons.spring.persistence.database.PersistenceConfigurationProvider;
import org.opensingular.app.commons.spring.persistence.database.SingularSchemaExport;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.persistence.DatabaseObjectNameReplacement;
import org.opensingular.lib.support.persistence.SingularEntityInterceptor;
import org.opensingular.lib.support.persistence.util.SqlUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement(proxyTargetClass = true)
public class SingularPersistenceDefaultBeanFactory implements Loggable {

    private PersistenceConfigurationProvider persistenceConfigurationProvider;

    @Bean
    public DataSource dataSource() {
        return getPersistenceConfiguration().getDataSource();
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        dataSourceInitializer.setDatabasePopulator(new H2DropAllObjectsPopulator(dataSource));
        dataSourceInitializer.setEnabled(getPersistenceConfiguration().isDropAllH2());
        return dataSourceInitializer;
    }

    @Bean
    @DependsOn("dataSourceInitializer")
    public LocalSessionFactoryBean sessionFactory(final DataSource dataSource) {
        final LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setHibernateProperties(getPersistenceConfiguration().getHibernateProperties());
        sessionFactoryBean.setPackagesToScan(getPersistenceConfiguration().getPackagesToScan(true));
        sessionFactoryBean.setEntityInterceptor(new SingularEntityInterceptor(getPersistenceConfiguration().getSchemaReplacements()));

        executeTablesOnConnection(dataSource);
        return sessionFactoryBean;
    }

    private void executeTablesOnConnection(DataSource dataSource) {
        if (getPersistenceConfiguration().isCreateDrop()) {
            StringBuilder scripts = SingularSchemaExport.generateScript(
                    persistenceConfigurationProvider.getPackagesToScan(false),
                    persistenceConfigurationProvider.getDialect(),
                    null,
                    persistenceConfigurationProvider.getSQLScritps());
            for (DatabaseObjectNameReplacement schemaReplacement : getPersistenceConfiguration().getSchemaReplacements()) {
                scripts = new StringBuilder(SqlUtil.replaceSchemaName(scripts.toString(),
                        schemaReplacement.getOriginalObjectName(),
                        schemaReplacement.getObjectNameReplacement()));
            }
            try {
                PreparedStatement ps = dataSource.getConnection().prepareStatement(scripts.toString());
                Logger.getLogger(SingularSchemaExport.class.getName()).info(scripts.toString());
                ps.execute();
            } catch (SQLException sqle) {
                getLogger().error("Error in the generation of schemas and tables!", sqle);
            }
        }
    }

    @Bean
    public HibernateTransactionManager transactionManager(final SessionFactory sessionFactory, final DataSource dataSource) {
        final HibernateTransactionManager tx = new HibernateTransactionManager(sessionFactory);
        tx.setDataSource(dataSource);
        return tx;
    }

    protected PersistenceConfigurationProvider getPersistenceConfiguration() {
        if (persistenceConfigurationProvider == null) {
            persistenceConfigurationProvider = new PersistenceConfigurationProvider();
        }
        return persistenceConfigurationProvider;
    }
}
