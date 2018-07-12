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
import org.opensingular.app.commons.spring.persistence.SingularPersistenceDefaultBeanFactory;
import org.opensingular.lib.commons.lambda.IConsumer;
import org.opensingular.lib.wicket.util.application.SkinnableApplication;
import org.opensingular.lib.wicket.util.template.SkinOptions;
import org.opensingular.requirement.module.admin.AdministrationApplication;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.opensingular.requirement.module.spring.SingularDefaultBeanFactory;
import org.opensingular.requirement.module.wicket.SingleAppPage;
import org.opensingular.requirement.module.wicket.SingularRequirementApplication;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public abstract class AbstractSingularInitializer implements SingularInitializer {

    protected abstract String moduleCod();

    protected abstract String[] springPackagesToScan();

    @Override
    public FlowInitializer flowConfiguration() {
        return new FlowInitializer() {
            @Override
            public String moduleCod() {
                return AbstractSingularInitializer.this.moduleCod();
            }
        };
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

    @Override
    public WebInitializer webConfiguration() {
        return new WebInitializer() {

            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                String contextPath = servletContext.getContextPath();//NOSONAR
                servletContext.setAttribute(SkinnableApplication.INITSKIN_CONSUMER_PARAM, (IConsumer<SkinOptions>) skinOptions -> initSkins(contextPath, skinOptions));
                super.onStartup(servletContext);
            }

            @Override
            protected Class<? extends SingularRequirementApplication> getWicketApplicationClass(IServerContext iServerContext) {
                if (ServerContext.WORKLIST.isSameContext(iServerContext)) {
                    return AnalysisApplication.class;
                } else if (ServerContext.REQUIREMENT.isSameContext(iServerContext)) {
                    return RequirementApplication.class;
                } else if (ServerContext.ADMINISTRATION.isSameContext(iServerContext)) {
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
                context.scan(AbstractSingularInitializer.this.springPackagesToScan());
                return context;
            }

            @Override
            protected Class<? extends SingularPersistenceDefaultBeanFactory> persistenceConfiguration() {
                return AbstractSingularInitializer.this.persistenceConfiguration();
            }

            @Override
            protected Class<? extends SingularDefaultBeanFactory> beanFactory() {
                return AbstractSingularInitializer.this.beanFactory();
            }
        };
    }

    @Override
    public Class<? extends SingularSpringWebMVCConfig> getSingularSpringWebMVCConfig() {
        return SingularSpringWebMVCConfig.class;
    }

    @Override
    public FormInitializer formConfiguration() {
        return new FormInitializer();
    }

    @Override
    public SpringSecurityInitializer springSecurityConfiguration() {
        return new SpringSecurityInitializer();
    }

    protected Class<? extends SingularDefaultBeanFactory> beanFactory() {
        return SingularDefaultBeanFactory.class;
    }

    protected void initSkins(String contextPath, SkinOptions skinOptions) {
    }

    protected Class<? extends SingularPersistenceDefaultBeanFactory> persistenceConfiguration() {
        return SingularPersistenceDefaultBeanFactory.class;
    }

    public static class AnalysisApplication extends SingularRequirementApplication {
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

    public static class RequirementApplication extends SingularRequirementApplication {
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