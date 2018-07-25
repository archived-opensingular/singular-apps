package org.opensingular.app.commons.mail.schedule;

import java.util.Properties;
import javax.sql.DataSource;

import org.opensingular.flow.schedule.IScheduleService;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.support.spring.util.AutoScanDisabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import static org.opensingular.lib.commons.base.SingularProperties.SINGULAR_QUARTZ_JOBSTORE_ENABLED;

/**
 * Bean Factory just for the Quartz.
 */
@AutoScanDisabled
public class SingularQuartzBeanFactory {

    @Autowired
    private DataSource dataSource;

    @Bean
    @DependsOn("schedulerFactoryBean")
    public IScheduleService scheduleService() {
        return new TransactionalQuartzScheduledService(schedulerFactoryBean());
    }

    /**
     * Configure the SchedulerBean for Singular.
     * This bean have to implents InitializingBean to work properly.
     *
     * @return SingularSchedulerBean instance.
     */
    @Bean
    public SingularSchedulerBean schedulerFactoryBean() {
        SingularSchedulerBean factory = new SingularSchedulerBean();
        Properties quartzProperties = new Properties();
        quartzProperties.setProperty("org.quartz.scheduler.instanceName", "SINGULARID");
        quartzProperties.setProperty("org.quartz.scheduler.instanceId", "AUTO");
        if (SingularProperties.get().isTrue(SINGULAR_QUARTZ_JOBSTORE_ENABLED)) {
            quartzProperties.put("org.quartz.jobStore.useProperties", "false");
            quartzProperties.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
            quartzProperties.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.MSSQLDelegate");
            quartzProperties.put("org.quartz.jobStore.tablePrefix", "DBSINGULAR.qrtz_");
            quartzProperties.put("org.quartz.jobStore.isClustered", "true");
            factory.setQuartzProperties(quartzProperties);
            factory.setDataSource(dataSource);
            factory.setOverwriteExistingJobs(true);
        } else {
            quartzProperties.put("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
        }

        return factory;
    }

}
