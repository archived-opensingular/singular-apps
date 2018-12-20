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

package org.opensingular.app.commons.test;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.opensingular.app.commons.mail.persistence.dao.EmailAddresseeDao;
import org.opensingular.app.commons.mail.persistence.dao.EmailDao;
import org.opensingular.app.commons.mail.schedule.SingularSchedulerBean;
import org.opensingular.app.commons.mail.schedule.TransactionalQuartzScheduledService;
import org.opensingular.app.commons.mail.service.dto.Email;
import org.opensingular.app.commons.mail.service.email.EmailPersistenceService;
import org.opensingular.app.commons.mail.service.email.EmailSender;
import org.opensingular.app.commons.mail.service.email.EmailSenderScheduledJob;
import org.opensingular.app.commons.mail.service.email.IEmailService;
import org.opensingular.form.persistence.dao.AttachmentContentDao;
import org.opensingular.form.persistence.dao.AttachmentDao;
import org.opensingular.form.persistence.service.AttachmentPersistenceService;
import org.opensingular.lib.commons.base.SingularException;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.persistence.SessionLocator;
import org.opensingular.lib.support.persistence.SingularEntityInterceptor;
import org.opensingular.lib.support.persistence.SingularOracle10gDialect;
import org.opensingular.lib.support.spring.util.AutoScanDisabled;
import org.opensingular.schedule.IScheduleService;
import org.opensingular.schedule.ScheduleDataBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import static org.opensingular.lib.commons.base.SingularProperties.CUSTOM_SCHEMA_NAME;

@EnableTransactionManagement(proxyTargetClass = true)
@Configuration
@ComponentScan(
        basePackages = {"org.opensingular.app.commons"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION,
                        value = AutoScanDisabled.class)
        })
public class ApplicationContextConfiguration implements Loggable {


    @Bean
    public SessionLocator sessionLocator(SessionFactory sessionFactory) {
        return () -> sessionFactory.getCurrentSession();
    }


    @Bean
    @DependsOn("scriptsInitializer")
    public LocalSessionFactoryBean sessionFactory(final DataSource dataSource) {
        final LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setHibernateProperties(hibernateProperties());
        sessionFactoryBean.setPackagesToScan(
                "org.opensingular.flow.persistence.entity",
                "org.opensingular.app.commons",
                "org.opensingular.form.persistence.entity");
        Optional<String> schemaName = SingularProperties.getOpt(CUSTOM_SCHEMA_NAME);
        if (schemaName.isPresent()) {
            sessionFactoryBean.setEntityInterceptor(new SingularEntityInterceptor());
            getLogger().info("Utilizando schema customizado: {}", schemaName.get());
        }
        return sessionFactoryBean;
    }

    protected Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", SingularOracle10gDialect.class.getName());
        hibernateProperties.setProperty("hibernate.connection.isolation", "2");
        hibernateProperties.setProperty("hibernate.jdbc.batch_size", "30");
        hibernateProperties.setProperty("hibernate.show_sql", "false");
        hibernateProperties.setProperty("hibernate.format_sql", "true");
        hibernateProperties.setProperty("hibernate.enable_lazy_load_no_trans", "true");
        hibernateProperties.setProperty("hibernate.jdbc.use_get_generated_keys", "true");
        hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", "false");
        hibernateProperties.setProperty("hibernate.cache.use_query_cache", "false");
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        return hibernateProperties;
    }

    @Bean
    public HibernateTransactionManager transactionManager(final SessionFactory sessionFactory, final DataSource dataSource) {
        final HibernateTransactionManager tx = new HibernateTransactionManager(sessionFactory);
        tx.setDataSource(dataSource);
        return tx;
    }

    @Bean
    protected DataSource dataSource() {
        try {
            getLogger().warn("Usando datasource banco embarcado H2");
            HikariDataSource dataSource = new HikariDataSource();//NOSONAR
            dataSource.setJdbcUrl("jdbc:h2:mem:maildb" + UUID.randomUUID().toString() + ";mode=ORACLE;CACHE_SIZE=4096;EARLY_FILTER=1;MULTI_THREADED=1;LOCK_TIMEOUT=15000;");
            dataSource.setUsername("sa");
            dataSource.setPassword("sa");
            dataSource.setDriverClassName("org.h2.Driver");

            return dataSource;
        } catch (Exception e) {
            throw SingularException.rethrow(e.getMessage(), e);
        }
    }

    @Value("classpath:create-scrips-email-test.sql")
    private Resource sqlScriptsEmailTest;


    protected ResourceDatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.setSqlScriptEncoding(StandardCharsets.UTF_8.name());
        populator.addScript(sqlScriptsEmailTest);
        return populator;
    }

    @Bean
    public DataSourceInitializer scriptsInitializer(final DataSource dataSource) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator());
        return initializer;
    }

    @Bean
    public AttachmentDao attachmentDao() {
        return new AttachmentDao();
    }

    @Bean
    public AttachmentContentDao attachmentContentDao() {
        return new AttachmentContentDao<>();
    }

    @Bean
    public AttachmentPersistenceService filePersistence() {
        return new AttachmentPersistenceService();
    }

    @Bean
    public EmailDao emailDao() {
        return new EmailDao();
    }

    @Bean
    public EmailAddresseeDao emailAddresseeDao() {
        return new EmailAddresseeDao();
    }

    @Bean
    public EmailSender emailSender() {
        return new EmailSender();
    }

    @Bean
    public IEmailService<Email> emailService() {
        return new EmailPersistenceService();
    }

    @Bean
    @DependsOn({"emailSender", "scheduleService", "emailService"})
    public EmailSenderScheduledJob scheduleEmailSenderJob(IScheduleService scheduleService) {
        EmailSenderScheduledJob emailSenderScheduledJob = new EmailSenderScheduledJob(ScheduleDataBuilder.buildMinutely(1));
        scheduleService.schedule(emailSenderScheduledJob);
        return emailSenderScheduledJob;
    }

    // ######### Beans for Quartz ##########
    @Bean
    @DependsOn("schedulerFactoryBean")
    public IScheduleService scheduleService(SingularSchedulerBean schedulerFactoryBean) {
        return new TransactionalQuartzScheduledService(schedulerFactoryBean);
    }

    /**
     * Configure the SchedulerBean for Singular.
     * This bean have to implents InitializingBean to work properly.
     *
     * @return SingularSchedulerBean instance.
     */
    @Bean
    public SingularSchedulerBean schedulerFactoryBean(DataSource dataSource) {
        return new SingularSchedulerBean(dataSource);
    }

    @Bean
    public SessionLocator sessionProvider(SessionFactory sessionFactory){
        return () -> sessionFactory.getCurrentSession();
    }

}



