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

package org.opensingular.server.commons.config;

import org.opensingular.server.commons.spring.security.config.SingularLogoutFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import java.util.EnumSet;

public abstract class SpringSecurityInitializer {

    static final        String SINGULAR_SECURITY = "[SINGULAR][SECURITY] {} {}";
    public static final Logger logger            = LoggerFactory.getLogger(SpringSecurityInitializer.class);

    public void init(ServletContext ctx, AnnotationConfigWebApplicationContext applicationContext, String springMVCServletMapping, IServerContext[] serverContexts) {
        addRestSecurity(applicationContext);
        addSpringSecurityFilter(ctx, applicationContext, springMVCServletMapping);
        for (IServerContext context : serverContexts) {
            logger.info(SINGULAR_SECURITY, "Securing (Spring Security) context:", context.getContextPath());
            applicationContext.register(getSpringSecurityConfigClass(context));
            addLogoutFilter(ctx, applicationContext, springMVCServletMapping, context);
        }
    }

    protected void addRestSecurity(AnnotationConfigWebApplicationContext applicationContext) {
        applicationContext.register(DefaultRestSecurity.class);
    }

    protected void addLogoutFilter(ServletContext ctx, AnnotationConfigWebApplicationContext applicationContext, String springMVCServletMapping, IServerContext context) {
        FilterRegistration.Dynamic singularLogoutFilter = ctx.addFilter("singularLogoutFilter" + System.identityHashCode(context), SingularLogoutFilter.class);
        if (singularLogoutFilter != null) {
            singularLogoutFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, context.getUrlPath() + "/logout");
        }
    }

    protected void addSpringSecurityFilter(ServletContext ctx, AnnotationConfigWebApplicationContext applicationContext, String springMVCServletMapping) {
        FilterRegistration.Dynamic springSecurityFilterChain = ctx.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class);
        if (springSecurityFilterChain != null) {
            springSecurityFilterChain.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, springMVCServletMapping);
        }
    }

    protected abstract <T extends WebSecurityConfigurerAdapter> Class<T> getSpringSecurityConfigClass(IServerContext context);

}