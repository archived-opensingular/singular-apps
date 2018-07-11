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
import org.opensingular.requirement.module.exception.SingularServerException;
import org.opensingular.requirement.module.wicket.SingularRequirementApplication;
import org.opensingular.requirement.module.admin.AdministrationApplication;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.function.Predicate;

public abstract class RequirementStudioAppInitializer extends AbstractSingularInitializer {

    public IServerContext STUDIO = new ServerContext("STUDIO", "/*", "singular.studio");

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

            @Override
            protected Class<? extends SingularRequirementApplication> getWicketApplicationClass(IServerContext currentContext) {
                return getWicketApplicationByContext(currentContext);
            }
        };
    }

    protected Class<? extends SingularRequirementApplication> getWicketApplicationByContext(IServerContext currentContext) {
        Predicate<IServerContext> sameContextCheck = (i) -> i.isSameContext(currentContext);
        if (sameContextCheck.test(ServerContext.WORKLIST)) {
            return getWorklistWicketApplication();
        }
        if (sameContextCheck.test(ServerContext.REQUIREMENT)) {
            return getRequirementWicketApplication();
        }
        if (sameContextCheck.test(ServerContext.ADMINISTRATION)) {
            return getAdministrationWicketApplication();
        }
        if (sameContextCheck.test(STUDIO)) {
            return getStudioWicketApplication();
        }
        throw new SingularServerException("Contexto inválido");
    }

    protected Class<? extends SingularRequirementApplication> getStudioWicketApplication() {
        return RequirementStudioApplication.class;
    }

    protected Class<? extends SingularRequirementApplication> getAdministrationWicketApplication() {
        return AdministrationApplication.class;
    }

    protected Class<? extends SingularRequirementApplication> getRequirementWicketApplication() {
        return RequirementApplication.class;
    }

    protected Class<? extends SingularRequirementApplication> getWorklistWicketApplication() {
        return AnalysisApplication.class;
    }

    protected IServerContext[] getServerContexts() {
        return new IServerContext[]{ServerContext.REQUIREMENT, ServerContext.WORKLIST, ServerContext.ADMINISTRATION, STUDIO};
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
