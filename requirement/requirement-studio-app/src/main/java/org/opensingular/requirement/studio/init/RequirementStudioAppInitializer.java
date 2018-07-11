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

import java.util.function.Predicate;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.opensingular.lib.commons.lambda.IConsumer;
import org.opensingular.lib.wicket.util.application.SkinnableApplication;
import org.opensingular.lib.wicket.util.template.SkinOptions;
import org.opensingular.requirement.module.admin.AdministrationApplication;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.config.PServerContext;
import org.opensingular.requirement.module.config.PSpringSecurityInitializer;
import org.opensingular.requirement.module.config.PWebInitializer;
import org.opensingular.requirement.module.config.SingularSpringWebMVCConfig;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.opensingular.requirement.module.wicket.SingularRequirementApplication;
import org.opensingular.requirement.single.config.SingleAppInitializer;
import org.opensingular.requirement.studio.spring.RequirementStudioBeanFactory;
import org.opensingular.requirement.studio.spring.RequirementStudioSpringSecurityInitializer;
import org.opensingular.requirement.studio.spring.RequirementStudioWebMVCConfig;
import org.opensingular.requirement.studio.wicket.RequirementStudioApplication;

public interface RequirementStudioAppInitializer extends SingleAppInitializer {

    PServerContext STUDIO = new PServerContext("STUDIO", "/*", "singular.studio");

    @Override
    default Class<? extends RequirementStudioBeanFactory> beanFactory() {
        return RequirementStudioBeanFactory.class;
    }

    @Override
    default PWebInitializer webConfiguration() {
        return new PWebInitializer() {

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

    default Class<? extends SingularRequirementApplication> getWicketApplicationByContext(IServerContext currentContext) {
        Predicate<IServerContext> sameContextCheck = (i) -> i.isSameContext(currentContext);
        if (sameContextCheck.test(PServerContext.WORKLIST)) {
            return getWorklistWicketApplication();
        }
        if (sameContextCheck.test(PServerContext.REQUIREMENT)) {
            return getRequirementWicketApplication();
        }
        if (sameContextCheck.test(PServerContext.ADMINISTRATION)) {
            return getAdministrationWicketApplication();
        }
        if (sameContextCheck.test(STUDIO)) {
            return getStudioWicketApplication();
        }
        throw new SingularServerException("Contexto inválido");
    }

    default Class<? extends SingularRequirementApplication> getStudioWicketApplication() {
        return RequirementStudioApplication.class;
    }

    default Class<? extends SingularRequirementApplication> getAdministrationWicketApplication() {
        return AdministrationApplication.class;
    }

    default Class<? extends SingularRequirementApplication> getRequirementWicketApplication() {
        return RequirementApplication.class;
    }

    default Class<? extends SingularRequirementApplication> getWorklistWicketApplication() {
        return AnalysisApplication.class;
    }

    default IServerContext[] getServerContexts() {
        return new IServerContext[]{PServerContext.REQUIREMENT, PServerContext.WORKLIST, PServerContext.ADMINISTRATION, STUDIO};
    }

    @Override
    default Class<? extends SingularSpringWebMVCConfig> getSingularSpringWebMVCConfig() {
        return RequirementStudioWebMVCConfig.class;
    }

    @Override
    default PSpringSecurityInitializer springSecurityConfiguration() {
        return new RequirementStudioSpringSecurityInitializer();
    }
}
