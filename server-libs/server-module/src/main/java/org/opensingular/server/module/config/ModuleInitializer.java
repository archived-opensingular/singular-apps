package org.opensingular.server.module.config;

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
import org.opensingular.server.module.wicket.view.util.dispatcher.DispatcherPage;
import org.opensingular.server.p.commons.admin.AdministrationApplication;
import org.opensingular.server.p.commons.config.PServerContext;
import org.opensingular.server.p.commons.config.PSingularInitializer;
import org.opensingular.server.p.commons.config.PWebInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public interface ModuleInitializer extends PSingularInitializer {


    String INITSKIN_CONSUMER_PARAM = "INITSKIN_CONSUMER_PARAM";

    @Override
    default PWebInitializer webConfiguration() {
        return new PWebInitializer() {

            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                servletContext.setAttribute(INITSKIN_CONSUMER_PARAM, (IConsumer<SkinOptions>) ModuleInitializer.this::initSkins);
                super.onStartup(servletContext);
            }

            @Override
            protected Class<? extends SingularServerApplication> getWicketApplicationClass(IServerContext iServerContext) {
                if (PServerContext.WORKLIST.isSameContext(iServerContext)
                        || PServerContext.REQUIREMENT.isSameContext(iServerContext)) {
                    return WorklistApplication.class;
                } else if (PServerContext.ADMINISTRATION.isSameContext(iServerContext)) {
                    return AdministrationApplication.class;
                }
                throw new SingularServerException("Contexto inválido");
            }
        };
    }

    @Override
    default SpringHibernateInitializer springHibernateConfiguration() {
        return new SpringHibernateInitializer() {
            @Override
            protected AnnotationConfigWebApplicationContext newApplicationContext() {
                AnnotationConfigWebApplicationContext context = super.newApplicationContext();
                context.scan(ModuleInitializer.this.springPackagesToScan());
                return context;
            }

            @Override
            protected Class<? extends SingularDefaultPersistenceConfiguration> persistenceConfiguration() {
                return ModuleInitializer.this.persistenceConfiguration();
            }

            @Override
            protected Class<? extends SingularDefaultBeanFactory> beanFactory() {
                return ModuleInitializer.this.beanFactory();
            }
        };
    }

    @Override
    default FlowInitializer flowConfiguration() {
        return new FlowInitializer() {
            @Override
            public String moduleCod() {
                return ModuleInitializer.this.moduleCod();
            }
        };
    }

    @Override
    default SchedulerInitializer schedulerConfiguration() {
        return null;
    }

    String moduleCod();


    default Class<? extends SingularDefaultPersistenceConfiguration> persistenceConfiguration() {
        return SingularDefaultPersistenceConfiguration.class;
    }


    default Class<? extends SingularDefaultBeanFactory> beanFactory() {
        return SingularDefaultBeanFactory.class;
    }


    String[] springPackagesToScan();

    default void initSkins(SkinOptions skinOptions) {

    }

    class WorklistApplication extends SingularServerApplication {

        @Override
        public Class<? extends Page> getHomePage() {
            return DispatcherPage.class;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void initSkins(SkinOptions skinOptions) {
            IConsumer<SkinOptions> initSKin = (IConsumer<SkinOptions>) this.getServletContext().getAttribute(INITSKIN_CONSUMER_PARAM);
            initSKin.accept(skinOptions);
        }

    }
}
