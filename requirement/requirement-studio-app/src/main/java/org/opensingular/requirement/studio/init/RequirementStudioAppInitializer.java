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

package org.opensingular.requirement.studio.init;

import org.opensingular.lib.commons.lambda.IConsumer;
import org.opensingular.lib.wicket.util.application.SkinnableApplication;
import org.opensingular.lib.wicket.util.template.SkinOptions;
import org.opensingular.requirement.module.config.*;
import org.opensingular.requirement.studio.spring.RequirementStudioBeanFactory;
import org.opensingular.requirement.studio.spring.RequirementStudioSpringSecurityInitializer;
import org.opensingular.requirement.studio.spring.RequirementStudioWebMVCConfig;
import org.opensingular.requirement.studio.wicket.RequirementStudioApplication;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public abstract class RequirementStudioAppInitializer extends AbstractSingularInitializer {
    @Override
    protected Class<? extends RequirementStudioBeanFactory> beanFactory() {
        return RequirementStudioBeanFactory.class;
    }

    @Override
    public WebInitializer webConfiguration() {
        return new WebInitializer() {

            @Override
            public IServerContext[] serverContexts() {
                return getServerContexts();
            }

            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                String contextPath = servletContext.getContextPath();//NOSONAR
                servletContext.setAttribute(SkinnableApplication.INITSKIN_CONSUMER_PARAM,
                        (IConsumer<SkinOptions>) skinOptions -> initSkins(contextPath, skinOptions));
                super.onStartup(servletContext);
            }
        };
    }

    protected IServerContext[] getServerContexts() {
        return new IServerContext[]{new DefaultContexts.RequirementContext(), new DefaultContexts.WorklistContext(),
                new DefaultContexts.AdministrationContext(), new StudioContext()};
    }

    @Override
    public Class<? extends SingularSpringWebMVCConfig> getSingularSpringWebMVCConfig() {
        return RequirementStudioWebMVCConfig.class;
    }

    @Override
    public SpringSecurityInitializer springSecurityConfiguration() {
        return new RequirementStudioSpringSecurityInitializer();
    }

    public static class StudioContext extends ServerContext {
        public static final String NAME = "STUDIO";

        public StudioContext() {
            super(NAME, "/*", "singular.studio");
        }

        @Override
        public Class<? extends RequirementStudioApplication> getWicketApplicationClass() {
            return RequirementStudioApplication.class;
        }

        @Override
        public Class<? extends WebSecurityConfigurerAdapter> getSpringSecurityConfigClass() {
            return null;
        }
    }
}