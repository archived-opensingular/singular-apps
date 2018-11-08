/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.requirement.module.config;

import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.opensingular.form.wicket.mapper.attachment.upload.servlet.FileUploadServlet;
import org.opensingular.form.wicket.mapper.attachment.upload.servlet.strategy.SimplePostFilesStrategy;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.requirement.module.SessionTimeoutHttpSessionListener;
import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.EnumSet;

/**
 * Faz a configuração do {@link ServletContext}, registrando o {@link DispatcherServlet} e listeners necessarios para
 * execução do Singular Requerimentos
 */
public class ServletContextSetupSingularWebAppInitializerListener implements SingularWebAppInitializerListener {
    private final int sessionTimeoutMinutes;

    public ServletContextSetupSingularWebAppInitializerListener(int sessionTimeoutMinutes) {
        this.sessionTimeoutMinutes = sessionTimeoutMinutes;
    }

    @Override
    public void onInitialize(ServletContext servletContext, AnnotationConfigWebApplicationContext applicationContext) {
        ServletRegistration.Dynamic dispatcherServlet = servletContext
                .addServlet("Spring MVC Dispatcher Servlet", new DispatcherServlet(applicationContext));
        dispatcherServlet.setLoadOnStartup(1);
        dispatcherServlet.addMapping("/*");

        servletContext
                .addFilter("springSecurityFilterChain", new DelegatingFilterProxy(
                        "springSecurityFilterChain", applicationContext))
                .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");

        servletContext
                .addFilter("opensessioninview", OpenSessionInViewFilter.class)
                .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");

        servletContext
                .addServlet(SimplePostFilesStrategy.class.getName(), FileUploadServlet.class)
                .addMapping(SimplePostFilesStrategy.URL_PATTERN);

        servletContext
                .addListener(new SessionTimeoutHttpSessionListener(sessionTimeoutMinutes));

        servletContext
                .addListener(new ContextLoaderListener(applicationContext));

        servletContext
                .addListener(RequestContextListener.class);

        if (SingularProperties.get().isTrue(SingularProperties.DEFAULT_CAS_ENABLED)) {
            servletContext.addListener(SingleSignOutHttpSessionListener.class);
        }
    }
}
