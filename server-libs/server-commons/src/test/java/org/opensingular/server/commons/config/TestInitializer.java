package org.opensingular.server.commons.config;

import org.apache.wicket.Page;
import org.opensingular.server.commons.wicket.SingularApplication;
import org.opensingular.server.p.commons.config.PFlowInitializer;
import org.opensingular.server.p.commons.config.PSingularInitializer;
import org.opensingular.server.p.commons.config.PSpringHibernateInitializer;
import org.opensingular.server.p.commons.config.PWebInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockServletContext;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;


@Configuration
public class TestInitializer implements PSingularInitializer {

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
    public PSpringHibernateInitializer springHibernateConfiguration() {return new PSpringHibernateInitializer() {};
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
