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

public abstract class ModuleInitializer implements PSingularInitializer {

    private static final String INITSKIN_CONSUMER_PARAM = "INITSKIN_CONSUMER_PARAM";

    @Override
    public PWebInitializer webConfiguration() {
        return new PWebInitializer() {

            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                servletContext.setAttribute(INITSKIN_CONSUMER_PARAM, (IConsumer<SkinOptions>) ModuleInitializer.this::initSkins);
                super.onStartup(servletContext);
            }

            @Override
            protected Class<? extends SingularServerApplication> getWicketApplicationClass(IServerContext iServerContext) {
                return ModuleInitializer.this.wicketApplicationClass(iServerContext);
            }
        };
    }

    protected Class<? extends SingularServerApplication> wicketApplicationClass(IServerContext iServerContext) {
        if (PServerContext.WORKLIST.isSameContext(iServerContext)
                || PServerContext.REQUIREMENT.isSameContext(iServerContext)) {
            return WorklistApplication.class;
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
    public FlowInitializer flowConfiguration() {
        return new FlowInitializer() {
            @Override
            public String moduleCod() {
                return ModuleInitializer.this.moduleCod();
            }
        };
    }

    @Override
    public SchedulerInitializer schedulerConfiguration() {
        return null;
    }

    protected abstract String moduleCod();


    protected Class<? extends SingularDefaultPersistenceConfiguration> persistenceConfiguration() {
        return SingularDefaultPersistenceConfiguration.class;
    }


    protected Class<? extends SingularDefaultBeanFactory> beanFactory() {
        return SingularDefaultBeanFactory.class;
    }


    protected abstract String[] springPackagesToScan();

    public void initSkins(SkinOptions skinOptions) {

    }

    public static class WorklistApplication extends SingularServerApplication {

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
