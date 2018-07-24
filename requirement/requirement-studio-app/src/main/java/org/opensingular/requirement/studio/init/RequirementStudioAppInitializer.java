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
import org.opensingular.requirement.module.config.AbstractSingularInitializer;
import org.opensingular.requirement.module.config.SingularSpringWebMVCConfig;
import org.opensingular.requirement.module.config.SpringSecurityInitializer;
import org.opensingular.requirement.module.config.WebInitializer;
import org.opensingular.requirement.studio.spring.RequirementStudioBeanFactory;
import org.opensingular.requirement.studio.spring.RequirementStudioSpringSecurityInitializer;
import org.opensingular.requirement.studio.spring.RequirementStudioWebMVCConfig;

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
            public void onStartup(ServletContext servletContext) throws ServletException {
                String contextPath = servletContext.getContextPath();//NOSONAR
                servletContext.setAttribute(SkinnableApplication.INITSKIN_CONSUMER_PARAM,
                        (IConsumer<SkinOptions>) skinOptions -> initSkins(contextPath, skinOptions));
                super.onStartup(servletContext);
            }
        };
    }

    @Override
    public Class<? extends SingularSpringWebMVCConfig> getSingularSpringWebMVCConfig() {
        return RequirementStudioWebMVCConfig.class;
    }

    @Override
    public SpringSecurityInitializer springSecurityConfiguration() {
        return new RequirementStudioSpringSecurityInitializer();
    }
}