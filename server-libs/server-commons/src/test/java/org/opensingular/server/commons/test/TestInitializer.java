package org.opensingular.server.commons.test;

import org.apache.wicket.Page;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.config.SchedulerInitializer;
import org.opensingular.server.commons.wicket.SingularApplication;
import org.opensingular.server.p.commons.config.PFlowInitializer;
import org.opensingular.server.p.commons.config.PSingularInitializer;
import org.opensingular.server.p.commons.config.PSpringHibernateInitializer;
import org.opensingular.server.p.commons.config.PWebInitializer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;


@Configuration
public class TestInitializer implements PSingularInitializer, ApplicationContextAware {

    private AnnotationConfigWebApplicationContext annotationConfigWebApplicationContext = new AnnotationConfigWebApplicationContext();

    @PostConstruct
    public void init() throws ServletException {
        onStartup(new SingularMockServletContext());
    }

    @Override
    public PWebInitializer webConfiguration() {
        return new PWebInitializer() {
            @Override
            protected Class<? extends SingularApplication> getWicketApplicationClass(IServerContext context) {
                return TestSingularApplication.class;
            }
        };
    }

    @Override
    public PSpringHibernateInitializer springHibernateConfiguration() {
        return new PSpringHibernateInitializer() {
            @Override
            protected AnnotationConfigWebApplicationContext newApplicationContext() {
                return annotationConfigWebApplicationContext;
            }
        };
    }

    @Override
    public PFlowInitializer flowConfiguration() {
        return null;
    }

    @Override
    public SchedulerInitializer schedulerConfiguration() {
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        annotationConfigWebApplicationContext.setParent(applicationContext);
//        AbstractApplicationContext abstractApplicationContext = (AbstractApplicationContext) applicationContext;
//
//        abstractApplicationContext.setParent(annotationConfigWebApplicationContext);
//        abstractApplicationContext.setClassLoader(applicationContext.getClassLoader());
//        abstractApplicationContext.refresh();
//        abstractApplicationContext.registerShutdownHook();
    }

    public static class TestSingularApplication extends SingularApplication {

        @Override
        public Class<? extends Page> getHomePage() {
            return null;
        }
    }
}
