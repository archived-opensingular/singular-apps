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

package org.opensingular.server.studio.init;

import org.opensingular.lib.commons.lambda.IConsumer;
import org.opensingular.lib.wicket.util.application.SkinnableApplication;
import org.opensingular.lib.wicket.util.template.SkinOptions;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.config.SingularSpringWebMVCConfig;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.spring.SingularDefaultBeanFactory;
import org.opensingular.server.commons.wicket.SingularServerApplication;
import org.opensingular.server.commons.admin.AdministrationApplication;
import org.opensingular.server.commons.config.PServerContext;
import org.opensingular.server.commons.config.PSpringSecurityInitializer;
import org.opensingular.server.commons.config.PWebInitializer;
import org.opensingular.server.single.config.SingleAppBeanFactory;
import org.opensingular.server.single.config.SingleAppInitializer;
import org.opensingular.server.studio.spring.ServerStudioRequirementBeanFactory;
import org.opensingular.server.studio.spring.StudioSpringSecurityInitializer;
import org.opensingular.server.studio.spring.ServerStudioWebMVCConfig;
import org.opensingular.server.studio.wicket.ServerStudioApplication;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.function.Predicate;

public interface ServerStudioAppInitializer extends SingleAppInitializer {

    PServerContext STUDIO = new PServerContext("STUDIO", "/*", "singular.studio");

    @Override
    default Class<? extends SingleAppBeanFactory> beanFactory() {
        return ServerStudioRequirementBeanFactory.class;
    }

    @Override
    default PWebInitializer webConfiguration() {
        return new PWebInitializer() {
            @Override
            protected void configureCAS(ServletContext servletContext) {
            }

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
            protected Class<? extends SingularServerApplication> getWicketApplicationClass(IServerContext currentContext) {
                return getWicketApplicationByContext(currentContext);
            }
        };
    }

    default Class<? extends SingularServerApplication> getWicketApplicationByContext(IServerContext currentContext) {
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

    default Class<? extends SingularServerApplication> getStudioWicketApplication() {
        return ServerStudioApplication.class;
    }

    default Class<? extends SingularServerApplication> getAdministrationWicketApplication() {
        return AdministrationApplication.class;
    }

    default Class<? extends SingularServerApplication> getRequirementWicketApplication() {
        return RequirementApplication.class;
    }

    default Class<? extends SingularServerApplication> getWorklistWicketApplication() {
        return AnalysisApplication.class;
    }

    default IServerContext[] getServerContexts() {
        return new IServerContext[]{PServerContext.REQUIREMENT, PServerContext.WORKLIST, PServerContext.ADMINISTRATION, STUDIO};
    }

    @Override
    default Class<? extends SingularSpringWebMVCConfig> getSingularSpringWebMVCConfig() {
        return ServerStudioWebMVCConfig.class;
    }

    @Override
    default PSpringSecurityInitializer springSecurityConfiguration() {
        return new StudioSpringSecurityInitializer();
    }
}
