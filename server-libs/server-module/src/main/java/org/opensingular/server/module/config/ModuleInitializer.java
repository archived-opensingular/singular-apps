package org.opensingular.server.module.config;

import org.apache.commons.lang3.ArrayUtils;
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
import org.opensingular.server.commons.wicket.SingularApplication;
import org.opensingular.server.module.wicket.view.util.dispatcher.DispatcherPage;
import org.opensingular.server.p.commons.admin.AdministrationApplication;
import org.opensingular.server.p.commons.config.PServerContext;
import org.opensingular.server.p.commons.config.PSingularInitializer;
import org.opensingular.server.p.commons.config.PWebInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public abstract class ModuleInitializer implements PSingularInitializer {

    /**
     * Está classe é um singleton, apenas uma instância por módulo war
     */
    private IConsumer<SkinOptions> skinOptionsIConsumer;

    @Override
    public PWebInitializer webConfiguration() {
        skinOptionsIConsumer = this::initSkins;
        return new PWebInitializer() {
            @Override
            protected Class<? extends SingularApplication> getWicketApplicationClass(IServerContext iServerContext) {
                if (PServerContext.WORKLIST.isSameContext(iServerContext)
                        || PServerContext.PETITION.isSameContext(iServerContext)) {
                    return WorklistApplication.class;
                } else if (PServerContext.ADMINISTRATION.isSameContext(iServerContext)) {
                    return AdministrationApplication.class;
                }
                throw new SingularServerException("Contexto inválido");
            }
        };
    }

    @Override
    public SpringHibernateInitializer springHibernateConfiguration() {
        return new SpringHibernateInitializer() {
            @Override
            protected AnnotationConfigWebApplicationContext newApplicationContext() {
                AnnotationConfigWebApplicationContext context = super.newApplicationContext();
                context.scan(ArrayUtils.add(ModuleInitializer.this.springPackagesToScan(), "org.opensingular"));
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
    public FlowInitializer flowConfiguration() {
        return new FlowInitializer() {
            @Override
            public String processGroupCod() {
                return ModuleInitializer.this.processGroupCod();
            }
        };
    }

    @Override
    public SchedulerInitializer schedulerConfiguration() {
        return null;
    }

    protected abstract String processGroupCod();


    protected Class<? extends SingularDefaultPersistenceConfiguration> persistenceConfiguration() {
        return SingularDefaultPersistenceConfiguration.class;
    }


    protected Class<? extends SingularDefaultBeanFactory> beanFactory() {
        return SingularDefaultBeanFactory.class;
    }


    protected abstract String[] springPackagesToScan();

    public void initSkins(SkinOptions skinOptions) {

    }

    public class WorklistApplication extends SingularApplication {

        @Override
        public Class<? extends Page> getHomePage() {
            return DispatcherPage.class;
        }

        @Override
        public void initSkins(SkinOptions skinOptions) {
            skinOptionsIConsumer.accept(skinOptions);
        }

    }
}
