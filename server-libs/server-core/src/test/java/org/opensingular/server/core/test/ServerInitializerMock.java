package org.opensingular.server.core.test;

import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.config.SchedulerInitializer;
import org.opensingular.server.commons.config.SpringHibernateInitializer;
import org.opensingular.server.commons.test.CommonsApplicationMock;
import org.opensingular.server.commons.test.CommonsInitializerMock;
import org.opensingular.server.commons.wicket.SingularApplication;
import org.opensingular.server.p.commons.config.PWebInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;


public class ServerInitializerMock extends CommonsInitializerMock {

    public static final String   TESTE                      = "Teste";
    public static final String[] DEFINITIONS_PACKS_ARRAY    = new String[]{"org.opensingular.server.commons.test"};
    public static final String   SPRING_MVC_SERVLET_MAPPING = "/*";
    private AnnotationConfigWebApplicationContext applicationContext;

    public ServerInitializerMock(AnnotationConfigWebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ServerInitializerMock() {
    }

    @Override
    public PWebInitializer webConfiguration() {
        return new PWebInitializer() {
            @Override
            protected Class<? extends SingularApplication> getWicketApplicationClass(IServerContext context) {
                return CommonsApplicationMock.class;
            }
        };
    }

    @Override
    public SpringHibernateInitializer springHibernateConfiguration() {
        return new SpringHibernateInitializer() {
            @Override
            protected AnnotationConfigWebApplicationContext newApplicationContext() {
                return applicationContext;
            }

            @Override
            protected String springMVCServletMapping() {
                return SPRING_MVC_SERVLET_MAPPING;
            }
        };
    }

    @Override
    public SchedulerInitializer schedulerConfiguration() {
        return new SchedulerInitializer() {
            @Override
            public Class<?> mailConfiguration() {
                return Object.class;
            }

            @Override
            public Class<?> attachmentGCConfiguration() {
                return Object.class;
            }
        };
    }


}
