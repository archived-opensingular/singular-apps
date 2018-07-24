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

import org.opensingular.form.wicket.mapper.attachment.upload.servlet.FileUploadServlet;
import org.opensingular.form.wicket.mapper.attachment.upload.servlet.strategy.SimplePostFilesStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate4.support.OpenSessionInViewFilter;

import javax.servlet.*;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;


/**
 * Configura os filtros, servlets e listeners default do singular pet server
 * e as configurações básicas do spring e spring-security
 */
public abstract class WebInitializer {

    public static final Logger logger = LoggerFactory.getLogger(WebInitializer.class);

    public void init(ServletContext ctx) throws ServletException {
        onStartup(ctx);
    }

    protected void onStartup(ServletContext ctx) throws ServletException {
        addSessionListener(ctx);
        addOpenSessionInView(ctx);
        addPublicUploadServlet(ctx);
    }

    private void addOpenSessionInView(ServletContext servletContext) {
        FilterRegistration.Dynamic opensessioninview = servletContext.addFilter("opensessioninview", OpenSessionInViewFilter.class);
        opensessioninview.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
    }

    private void addPublicUploadServlet(ServletContext servletContext) {
        ServletRegistration.Dynamic uploadServlet = servletContext.addServlet(SimplePostFilesStrategy.class.getName(), FileUploadServlet.class);
        uploadServlet.addMapping(SimplePostFilesStrategy.URL_PATTERN);
    }

    public String[] getDefaultPublicUrls() {
        List<String> urls = new ArrayList<>();
        urls.add("/rest/*");
        urls.add("/resources/*");
        urls.add("/public/*");
        urls.add("/index.html");
        return urls.toArray(new String[0]);
    }

    /**
     * Configura o timeout da sessão web em minutos
     * <p>
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


}
