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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.google.common.base.Joiner;
import org.apache.wicket.protocol.http.WicketFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.opensingular.form.wicket.mapper.attachment.upload.servlet.FileUploadServlet;
import org.opensingular.form.wicket.mapper.attachment.upload.servlet.strategy.SimplePostFilesStrategy;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.requirement.module.spring.security.config.cas.util.SSOConfigurableFilter;
import org.opensingular.requirement.module.spring.security.config.cas.util.SSOFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate4.support.OpenSessionInViewFilter;

import org.opensingular.requirement.module.wicket.SingularRequirementApplication;

import static org.opensingular.requirement.module.config.ServerContext.ADMINISTRATION;
import static org.opensingular.requirement.module.config.ServerContext.REQUIREMENT;
import static org.opensingular.requirement.module.config.ServerContext.WORKLIST;

/**
 * Configura os filtros, servlets e listeners default do singular pet server
 * e as configurações básicas do spring e spring-security
 */
public abstract class WebInitializer {

    private static final String SINGULAR_SECURITY = "[SINGULAR][WEB] {} {}";

    public static final Logger logger = LoggerFactory.getLogger(WebInitializer.class);

    public void init(ServletContext ctx) throws ServletException {
        onStartup(ctx);
    }

    protected void onStartup(ServletContext ctx) throws ServletException {
        addSessionListener(ctx);
        addOpenSessionInView(ctx);
        addPublicUploadServlet(ctx);
        for (IServerContext context : serverContexts()) {
            logger.info(SINGULAR_SECURITY, "Setting up web context:", context.getContextPath());
            addWicketFilter(ctx, context);
        }
        configureCAS(ctx);
    }

    public IServerContext[] serverContexts() {
        return new IServerContext[]{REQUIREMENT, WORKLIST, ADMINISTRATION};
    }

    protected void addWicketFilter(ServletContext ctx, IServerContext context) {
        FilterRegistration.Dynamic wicketFilter = ctx.addFilter(context.getName() + System.identityHashCode(context), WicketFilter.class);
        wicketFilter.setInitParameter("applicationClassName", getWicketApplicationClass(context).getName());
        wicketFilter.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, context.getContextPath());
        wicketFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, context.getContextPath());

    }

    protected abstract Class<? extends SingularRequirementApplication> getWicketApplicationClass(IServerContext context);

    private void addOpenSessionInView(ServletContext servletContext) {
        FilterRegistration.Dynamic opensessioninview = servletContext.addFilter("opensessioninview", OpenSessionInViewFilter.class);
        opensessioninview.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
    }

    private void addPublicUploadServlet(ServletContext servletContext) {
        ServletRegistration.Dynamic uploadServlet = servletContext.addServlet(SimplePostFilesStrategy.class.getName(), FileUploadServlet.class);
        uploadServlet.addMapping(SimplePostFilesStrategy.URL_PATTERN);
    }

    protected String[] getDefaultPublicUrls() {
        List<String> urls = new ArrayList<>();
        urls.add("/rest/*");
        urls.add("/resources/*");
        urls.add("/public/*");
        urls.add("/index.html");
        for (IServerContext ctx : serverContexts()) {
            urls.add(ctx.getUrlPath() + "/wicket/resource/*");
            urls.add(ctx.getUrlPath() + "/public/*");
        }
        return urls.toArray(new String[urls.size()]);
    }


    /**
     * Configura o timeout da sessão web em minutos
     *
     * x@return
     */
    protected int getSessionTimeoutMinutes() {
        return 15;//15 minutos
    }

    /**
     * Configura o session timeout da aplicação
     * Criado para permitir a remoção completa do web.xml
     *
     * @param servletContext
     */
    protected final void addSessionListener(ServletContext servletContext) {
        servletContext.addListener(new HttpSessionListener() {
            @Override
            public void sessionCreated(HttpSessionEvent se) {
                se.getSession().setMaxInactiveInterval(60 * getSessionTimeoutMinutes());
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent se) {
            }
        });
    }

    protected void configureCAS(ServletContext servletContext){
        if (SingularProperties.get().isTrue(SingularProperties.DEFAULT_CAS_ENABLED)) {
            addCASFilter(servletContext, WORKLIST);
            addCASFilter(servletContext, REQUIREMENT);
            addSingleSignOutListener(servletContext);
        }
    }


    protected void addCASFilter(ServletContext servletContext, IServerContext context) {
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