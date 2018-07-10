/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.requirement.module.config;

import org.apache.wicket.Page;
import org.opensingular.lib.commons.lambda.IConsumer;
import org.opensingular.lib.wicket.util.template.SkinOptions;
import org.opensingular.requirement.module.config.FlowInitializer;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.config.SchedulerInitializer;
import org.opensingular.requirement.module.config.SpringHibernateInitializer;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.opensingular.requirement.module.spring.SingularDefaultBeanFactory;
import org.opensingular.app.commons.spring.persistence.SingularPersistenceDefaultBeanFactory;
import org.opensingular.requirement.module.wicket.SingularRequirementApplication;
import org.opensingular.requirement.module.wicket.box.BoxPage;
import org.opensingular.requirement.module.admin.AdministrationApplication;
import org.opensingular.requirement.module.config.PServerContext;
import org.opensingular.requirement.module.config.PSingularInitializer;
import org.opensingular.requirement.module.config.PWebInitializer;
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
            protected Class<? extends SingularRequirementApplication> getWicketApplicationClass(IServerContext iServerContext) {
                return ServerInitializer.this.wicketApplicationClass(iServerContext);
            }

        };
    }

    protected Class<? extends SingularRequirementApplication> wicketApplicationClass(IServerContext iServerContext) {
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
            protected Class<? extends SingularPersistenceDefaultBeanFactory> persistenceConfiguration() {
                return ServerInitializer.this.persistenceConfiguration();
            }

            @Override
            protected Class<? extends SingularDefaultBeanFactory> beanFactory() {
                return ServerInitializer.this.beanFactory();
            }
        };
    }

    protected Class<? extends SingularPersistenceDefaultBeanFactory> persistenceConfiguration() {
        return SingularPersistenceDefaultBeanFactory.class;
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


    public static class AnalysisApplication extends SingularRequirementApplication {

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

    public static class RequirementApplication extends SingularRequirementApplication {

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
