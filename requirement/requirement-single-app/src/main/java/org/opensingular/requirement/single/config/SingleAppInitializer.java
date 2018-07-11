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

package org.opensingular.requirement.single.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.wicket.Page;
import org.opensingular.app.commons.spring.persistence.SingularPersistenceDefaultBeanFactory;
import org.opensingular.lib.commons.lambda.IConsumer;
import org.opensingular.lib.wicket.util.application.SkinnableApplication;
import org.opensingular.lib.wicket.util.template.SkinOptions;
import org.opensingular.requirement.module.admin.AdministrationApplication;
import org.opensingular.requirement.module.config.AttachmentGCSchedulerInitializer;
import org.opensingular.requirement.module.config.FlowInitializer;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.config.MailSenderSchedulerInitializer;
import org.opensingular.requirement.module.config.PServerContext;
import org.opensingular.requirement.module.config.PSingularInitializer;
import org.opensingular.requirement.module.config.PWebInitializer;
import org.opensingular.requirement.module.config.SchedulerInitializer;
import org.opensingular.requirement.module.config.SpringHibernateInitializer;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.opensingular.requirement.module.spring.SingularDefaultBeanFactory;
import org.opensingular.requirement.module.wicket.SingularRequirementApplication;
import org.opensingular.requirement.single.page.SingleAppPage;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;


public interface SingleAppInitializer extends PSingularInitializer {
    String moduleCod();

    String[] springPackagesToScan();

    @Override
    default PWebInitializer webConfiguration() {
        return new PWebInitializer() {

            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                String contextPath = servletContext.getContextPath();//NOSONAR
                servletContext.setAttribute(SkinnableApplication.INITSKIN_CONSUMER_PARAM, (IConsumer<SkinOptions>) skinOptions -> initSkins(contextPath, skinOptions));
                super.onStartup(servletContext);
            }

            @Override
            protected Class<? extends SingularRequirementApplication> getWicketApplicationClass(IServerContext iServerContext) {
                if (PServerContext.WORKLIST.isSameContext(iServerContext)) {
                    return AnalysisApplication.class;
                }
                else if (PServerContext.REQUIREMENT.isSameContext(iServerContext)) {
                    return RequirementApplication.class;
                }
                else if (PServerContext.ADMINISTRATION.isSameContext(iServerContext)) {
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
                context.scan(SingleAppInitializer.this.springPackagesToScan());
                return context;
            }

            @Override
            protected Class<? extends SingularPersistenceDefaultBeanFactory> persistenceConfiguration() {
                return SingleAppInitializer.this.persistenceConfiguration();
            }

            @Override
            protected Class<? extends SingularDefaultBeanFactory> beanFactory() {
                return SingleAppInitializer.this.beanFactory();
            }
        };
    }

    @Override
    default FlowInitializer flowConfiguration() {
        return new FlowInitializer() {
            @Override
            public String moduleCod() {
                return SingleAppInitializer.this.moduleCod();
            }
        };
    }

    default Class<? extends SingularDefaultBeanFactory> beanFactory() {
        return SingleAppBeanFactory.class;
    }

    default void initSkins(String contextPath, SkinOptions skinOptions) {
    }

    default Class<? extends SingularPersistenceDefaultBeanFactory> persistenceConfiguration() {
        return SingularPersistenceDefaultBeanFactory.class;
    }

    @Override
    default SchedulerInitializer schedulerConfiguration() {
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

    class AnalysisApplication extends SingularRequirementApplication {
        @Override
        public Class<? extends Page> getHomePage() {
            return SingleAppPage.class;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void initSkins(SkinOptions skinOptions) {
            IConsumer<SkinOptions> initSKin = (IConsumer<SkinOptions>) this.getServletContext().getAttribute(SkinnableApplication.INITSKIN_CONSUMER_PARAM);
            initSKin.accept(skinOptions);
        }
    }

    class RequirementApplication extends SingularRequirementApplication {
        @Override
        public Class<? extends Page> getHomePage() {
            return SingleAppPage.class;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void initSkins(SkinOptions skinOptions) {
            IConsumer<SkinOptions> initSKin = (IConsumer<SkinOptions>) this.getServletContext().getAttribute(SkinnableApplication.INITSKIN_CONSUMER_PARAM);
            initSKin.accept(skinOptions);
        }
    }
}