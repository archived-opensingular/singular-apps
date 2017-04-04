package org.opensingular.server.commons.test;

import org.apache.wicket.Page;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.config.SchedulerInitializer;
import org.opensingular.server.commons.wicket.SingularApplication;
import org.opensingular.server.p.commons.config.PFlowInitializer;
import org.opensingular.server.p.commons.config.PSingularInitializer;
import org.opensingular.server.p.commons.config.PSpringHibernateInitializer;
import org.opensingular.server.p.commons.config.PWebInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;


public class TestInitializer implements PSingularInitializer {

    private AnnotationConfigWebApplicationContext applicationContext;

    public TestInitializer(AnnotationConfigWebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public TestInitializer() {
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
                return applicationContext;
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

    public static class TestSingularApplication extends SingularApplication {

        @Override
        public Class<? extends Page> getHomePage() {
            return null;
        }
    }
}
