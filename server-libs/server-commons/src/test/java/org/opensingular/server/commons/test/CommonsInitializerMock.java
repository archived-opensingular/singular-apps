package org.opensingular.server.commons.test;

import org.opensingular.flow.core.service.IFlowMetadataREST;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.config.SchedulerInitializer;
import org.opensingular.server.commons.wicket.SingularApplication;
import org.opensingular.server.p.commons.config.PFlowInitializer;
import org.opensingular.server.p.commons.config.PSingularInitializer;
import org.opensingular.server.p.commons.config.PSpringHibernateInitializer;
import org.opensingular.server.p.commons.config.PWebInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;


public class CommonsInitializerMock implements PSingularInitializer {

    public static final String   TESTE                      = "Teste";
    public static final String[] DEFINITIONS_PACKS_ARRAY    = new String[]{"org.opensingular.server.commons.test"};
    public static final String   SPRING_MVC_SERVLET_MAPPING = "/*";
    private AnnotationConfigWebApplicationContext applicationContext;

    public CommonsInitializerMock(AnnotationConfigWebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public CommonsInitializerMock() {
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
    public PSpringHibernateInitializer springHibernateConfiguration() {
        return new PSpringHibernateInitializer() {
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
    public PFlowInitializer flowConfiguration() {
        return new PFlowInitializer() {

            @Override
            public String[] definitionsBasePackage() {
                return DEFINITIONS_PACKS_ARRAY;
            }

            @Override
            public String processGroupCod() {
                return TESTE;
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
