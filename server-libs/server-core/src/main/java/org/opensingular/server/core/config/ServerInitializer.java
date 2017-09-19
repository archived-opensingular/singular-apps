package org.opensingular.server.core.config;

import org.apache.wicket.Page;
import org.opensingular.lib.commons.lambda.IConsumer;
import org.opensingular.lib.wicket.util.template.SkinOptions;
import org.opensingular.server.commons.config.FlowInitializer;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.config.SchedulerInitializer;
import org.opensingular.server.commons.config.SpringHibernateInitializer;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.spring.SingularDefaultBeanFactory;
import org.opensingular.server.commons.spring.SingularDefaultPersistenceConfiguration;
import org.opensingular.server.commons.wicket.SingularServerApplication;
import org.opensingular.server.core.wicket.box.BoxPage;
import org.opensingular.server.p.commons.admin.AdministrationApplication;
import org.opensingular.server.p.commons.config.PServerContext;
import org.opensingular.server.p.commons.config.PSingularInitializer;
import org.opensingular.server.p.commons.config.PWebInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public abstract class ServerInitializer implements PSingularInitializer {

    private static final String INITSKIN_CONSUMER_PARAM = "INITSKIN_CONSUMER_PARAM";

    @Override
    public PWebInitializer webConfiguration() {
        return new PWebInitializer() {

            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                servletContext.setAttribute(INITSKIN_CONSUMER_PARAM, (IConsumer<SkinOptions>) ServerInitializer.this::initSkins);
                super.onStartup(servletContext);
            }

            @Override
            protected Class<? extends SingularServerApplication> getWicketApplicationClass(IServerContext iServerContext) {
                return ServerInitializer.this.wicketApplicationClass(iServerContext);
            }

        };
    }

    protected Class<? extends SingularServerApplication> wicketApplicationClass(IServerContext iServerContext) {
        if (PServerContext.WORKLIST.isSameContext(iServerContext)) {
            return AnalysisApplication.class;
        } else if (PServerContext.REQUIREMENT.isSameContext(iServerContext)) {
            return RequirementApplication.class;
        } else if (PServerContext.ADMINISTRATION.isSameContext(iServerContext)) {
            return AdministrationApplication.class;
        }
        throw new SingularServerException("Contexto inválido");
    }

    @Override
    public SpringHibernateInitializer springHibernateConfiguration() {
        return new SpringHibernateInitializer() {
            @Override
            protected AnnotationConfigWebApplicationContext newApplicationContext() {
                AnnotationConfigWebApplicationContext context = super.newApplicationContext();
                context.scan(ServerInitializer.this.springPackagesToScan());
                return context;
            }

            @Override
            protected Class<? extends SingularDefaultPersistenceConfiguration> persistenceConfiguration() {
                return ServerInitializer.this.persistenceConfiguration();
            }

            @Override
            protected Class<? extends SingularDefaultBeanFactory> beanFactory() {
                return ServerInitializer.this.beanFactory();
            }
        };
    }

    protected Class<? extends SingularDefaultPersistenceConfiguration> persistenceConfiguration() {
        return SingularDefaultPersistenceConfiguration.class;
    }


    protected Class<? extends SingularDefaultBeanFactory> beanFactory() {
        return SingularDefaultBeanFactory.class;
    }


    protected abstract String[] springPackagesToScan();

    @Override
    public FlowInitializer flowConfiguration() {
        return null;
    }

    @Override
    public SchedulerInitializer schedulerConfiguration() {
        return new SchedulerInitializer() {
            @Override
            public Class<?> mailConfiguration() {
                return MailSenderSchedulerInitializer.class;
            }

            @Override
            public Class<?> attachmentGCConfiguration() {
                return AttachmentGCSchedulerInitializer.class;
            }
        };
    }

    public void initSkins(SkinOptions skinOptions) {

    }


    public static class AnalysisApplication extends SingularServerApplication {

        @Override
        public Class<? extends Page> getHomePage() {
            return BoxPage.class;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void initSkins(SkinOptions skinOptions) {
            IConsumer<SkinOptions> initSKin = (IConsumer<SkinOptions>) this.getServletContext().getAttribute(INITSKIN_CONSUMER_PARAM);
            initSKin.accept(skinOptions);
        }
    }

    public static class RequirementApplication extends SingularServerApplication {

        @Override
        public Class<? extends Page> getHomePage() {
            return BoxPage.class;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void initSkins(SkinOptions skinOptions) {
            IConsumer<SkinOptions> initSKin = (IConsumer<SkinOptions>) this.getServletContext().getAttribute(INITSKIN_CONSUMER_PARAM);
            initSKin.accept(skinOptions);
        }

    }
}
