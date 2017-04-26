package org.opensingular.server.core.config;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.wicket.Page;
import org.opensingular.lib.commons.lambda.IConsumer;
import org.opensingular.lib.wicket.util.template.SkinOptions;
import org.opensingular.server.commons.config.FlowInitializer;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.config.SchedulerInitializer;
import org.opensingular.server.commons.config.SpringHibernateInitializer;
import org.opensingular.server.commons.spring.SingularDefaultBeanFactory;
import org.opensingular.server.commons.spring.SingularDefaultPersistenceConfiguration;
import org.opensingular.server.commons.wicket.SingularApplication;
import org.opensingular.server.core.wicket.box.BoxPage;
import org.opensingular.server.p.commons.admin.AdministrationApplication;
import org.opensingular.server.p.commons.config.PServerContext;
import org.opensingular.server.p.commons.config.PSingularInitializer;
import org.opensingular.server.p.commons.config.PWebInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public abstract class ServerInitializer implements PSingularInitializer {

    /**
     * Está classe é um singleton, apenas uma instância por módulo war
     */
    private static IConsumer<SkinOptions> skinOptionsIConsumer;

    public PWebInitializer webConfiguration() {
        skinOptionsIConsumer = this::initSkins;
        return new PWebInitializer() {

            @Override
            protected Class<? extends SingularApplication> getWicketApplicationClass(IServerContext iServerContext) {
                if (PServerContext.WORKLIST.equals(iServerContext)) {
                    return AnalysisApplication.class;
                } else if (PServerContext.PETITION.equals(iServerContext)) {
                    return PetitionApplication.class;
                } else if (PServerContext.ADMINISTRATION.equals(iServerContext)) {
                    return AdministrationApplication.class;
                }
                return null;
            }
        };
    }

    @Override
    public SpringHibernateInitializer springHibernateConfiguration() {
        return new SpringHibernateInitializer() {
            @Override
            protected AnnotationConfigWebApplicationContext newApplicationContext() {
                AnnotationConfigWebApplicationContext context = super.newApplicationContext();
                context.scan(ArrayUtils.add(ServerInitializer.this.springPackagesToScan(), "org.opensingular"));
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


    public static class AnalysisApplication extends SingularApplication {

        @Override
        public Class<? extends Page> getHomePage() {
            return BoxPage.class;
        }

        @Override
        public void initSkins(SkinOptions skinOptions) {
            skinOptionsIConsumer.accept(skinOptions);
        }
    }

    public static class PetitionApplication extends SingularApplication {

        @Override
        public Class<? extends Page> getHomePage() {
            return BoxPage.class;
        }

        @Override
        public void initSkins(SkinOptions skinOptions) {
            skinOptionsIConsumer.accept(skinOptions);
        }

    }
}
