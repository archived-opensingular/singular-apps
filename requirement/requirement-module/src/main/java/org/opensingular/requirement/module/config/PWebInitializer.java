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

import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import com.google.common.base.Joiner;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.requirement.module.spring.security.config.cas.util.SSOConfigurableFilter;
import org.opensingular.requirement.module.spring.security.config.cas.util.SSOFilter;

import static org.opensingular.requirement.module.config.PServerContext.ADMINISTRATION;
import static org.opensingular.requirement.module.config.PServerContext.REQUIREMENT;
import static org.opensingular.requirement.module.config.PServerContext.WORKLIST;

/**
 * Configura os filtros, servlets e listeners default do singular pet server
 * e as configurações básicas do spring e spring-security
 */
public abstract class PWebInitializer extends WebInitializer {

    @Override
    public IServerContext[] serverContexts() {
        return new IServerContext[]{REQUIREMENT, WORKLIST, ADMINISTRATION};
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        configureCAS(servletContext);
    }

    protected void configureCAS(ServletContext servletContext){
        if (SingularProperties.get().isTrue(SingularProperties.DEFAULT_CAS_ENABLED)) {
            addCASFilter(servletContext, WORKLIST);
            addCASFilter(servletContext, REQUIREMENT);
            addSingleSignOutListener(servletContext);
        }
    }


    protected void addCASFilter(ServletContext servletContext, PServerContext context) {
        configureSSO(servletContext, "SSOFilter" + context.getName(), context);
    }

    protected void addSingleSignOutListener(ServletContext servletContext) {
        servletContext.addListener(SingleSignOutHttpSessionListener.class);
    }

    protected void configureSSO(ServletContext servletContext, String filterName, IServerContext context) {
        FilterRegistration.Dynamic ssoFilter = servletContext.addFilter(filterName, SSOFilter.class);
        servletContext.setAttribute(filterName, context);
        ssoFilter.setInitParameter(SSOConfigurableFilter.SINGULAR_CONTEXT_ATTRIBUTE, filterName);
        ssoFilter.setInitParameter("logoutUrl", context.getUrlPath() + "/logout");
        ssoFilter.setInitParameter("urlExcludePattern", getExcludeUrlRegex());
        ssoFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, context.getContextPath());
    }

    /**
     * Transforma as expressões de urls públicas em regex simples
     *
     * @return
     */
    protected final String getExcludeUrlRegex() {
        return Joiner.on(",").join(getDefaultPublicUrls()).replaceAll("\\*", ".*");
    }

}
