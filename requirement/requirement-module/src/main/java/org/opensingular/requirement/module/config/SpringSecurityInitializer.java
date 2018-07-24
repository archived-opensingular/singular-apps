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

import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.support.spring.security.DefaultRestSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import java.util.EnumSet;

public class SpringSecurityInitializer {

    public static final Logger logger = LoggerFactory.getLogger(SpringSecurityInitializer.class);

    public void init(ServletContext ctx, AnnotationConfigWebApplicationContext applicationContext, String springMVCServletMapping) {
        addRestSecurity(applicationContext);
        addSpringSecurityFilter(ctx, springMVCServletMapping);
        addSingleSignOutListener(ctx);
    }

    protected void addRestSecurity(AnnotationConfigWebApplicationContext applicationContext) {
        applicationContext.register(DefaultRestSecurity.class);
    }

    protected void addSpringSecurityFilter(ServletContext ctx, String springMVCServletMapping) {
        ctx
                .addFilter("springSecurityFilterChain", DelegatingFilterProxy.class)
                .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, springMVCServletMapping);
    }

    @SuppressWarnings("unchecked")
    public Class<? extends WebSecurityConfigurerAdapter> getSpringSecurityConfigClass(IServerContext context) {
        return context.getSpringSecurityConfigClass();
    }

    protected void addSingleSignOutListener(ServletContext servletContext) {
        if (SingularProperties.get().isTrue(SingularProperties.DEFAULT_CAS_ENABLED)) {
            servletContext.addListener(SingleSignOutHttpSessionListener.class);
        }
    }

}