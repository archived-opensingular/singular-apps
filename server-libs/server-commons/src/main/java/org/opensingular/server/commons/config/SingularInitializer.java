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

import org.opensingular.lib.commons.context.SingularContextSetup;
import org.opensingular.server.commons.exception.SingularServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Optional;

public class SingularInitializer implements WebApplicationInitializer {

    static Logger logger                                             = LoggerFactory.getLogger(SingularInitializer.class);
    static String SINGULAR                                           = "[SINGULAR] {}";
    static String SERVLET_ATTRIBUTE_WEB_CONFIGURATION                = "Singular-webInitializer";
    static String SERVLET_ATTRIBUTE_SPRING_HIBERNATE_CONFIGURATION   = "Singular-springHibernateInitializer";
    static String SERVLET_ATTRIBUTE_FORM_CONFIGURATION_CONFIGURATION = "Singular-formInitializer";
    static String SERVLET_ATTRIBUTE_FLOW_CONFIGURATION_CONFIGURATION = "Singular-flowInitializer";


    private PSingularInitializer singularInitializer;

    public SingularInitializer() {
        this(SingularServerInitializerProvider.get().retrieve());
    }

    public SingularInitializer(PSingularInitializer singularInitializer) {
        this.singularInitializer = singularInitializer;
    }

    @Override
    public void onStartup(ServletContext ctx) throws ServletException {
        SingularContextSetup.reset();
        logger.info(SINGULAR, " Initializing Singular.... ");
        logger.info(SINGULAR, " Initializing WebConfiguration ");
        WebInitializer webInitializer = singularInitializer.webConfiguration();
        if (webInitializer != null) {
            webInitializer.init(ctx);
        }
        else {
            logger.info(SINGULAR, " Null webInitializer, skipping web configuration");
        }

        logger.info(SINGULAR, " Initializing SpringHibernateConfiguration ");
        SpringHibernateInitializer            springHibernateInitializer = singularInitializer.springHibernateConfiguration();
        AnnotationConfigWebApplicationContext applicationContext;
        if (springHibernateInitializer != null) {
            applicationContext = springHibernateInitializer.init(ctx);
        }
        else {
            throw new SingularServerException("Não foi possivel configurar o ApplicationContext");
        }

        logger.info(SINGULAR, " Initializing SpringSecurity ");
        SpringSecurityInitializer springSecurityInitializer = singularInitializer.springSecurityConfiguration();
        if (springSecurityInitializer != null) {
            springSecurityInitializer.init(ctx, applicationContext,
                    Optional.of(springHibernateInitializer)
                            .map(SpringHibernateInitializer::springMVCServletMapping)
                            .orElse(null),
                    Optional.ofNullable(webInitializer)
                            .map(WebInitializer::serverContexts)
                            .orElse(ServerContext.values()));
        }
        else {
            logger.info(SINGULAR, " Null springSecurityInitializer, skipping Spring Security configuration");
        }

        logger.info(SINGULAR, " Initializing FormConfiguration ");
        FormInitializer formInitializer = singularInitializer.formConfiguration();
        if (formInitializer != null) {
            formInitializer.init(ctx, applicationContext);
        }
        else {
            logger.info(SINGULAR, " Null formInitializer, skipping Singular Form configuration");
        }

        logger.info(SINGULAR, " Initializing FlowConfiguration ");
        FlowInitializer flowInitializer = singularInitializer.flowConfiguration();
        if (flowInitializer != null) {
            flowInitializer.init(ctx, applicationContext);
        }
        else {
            logger.info(SINGULAR, " Null flowInitializer, skipping Singular Flow configuration");
        }

        logger.info(SINGULAR, " Initializing SchedulerConfiguration ");
        SchedulerInitializer schedulerInitializer = singularInitializer.schedulerConfiguration();
        if (schedulerInitializer != null) {
            schedulerInitializer.init(ctx, applicationContext);
        }
        else {
            logger.info(SINGULAR, " Null SchedulerInitializer, skipping Singular Scheduler configuration");
        }

        if (applicationContext != null) {
            applicationContext.register(SingularServerConfiguration.class);
            applicationContext.register(singularInitializer.getSingularSpringWebMVCConfig());
            ctx.setAttribute(SERVLET_ATTRIBUTE_WEB_CONFIGURATION, webInitializer);
            ctx.setAttribute(SERVLET_ATTRIBUTE_SPRING_HIBERNATE_CONFIGURATION, springHibernateInitializer);
            ctx.setAttribute(SERVLET_ATTRIBUTE_FLOW_CONFIGURATION_CONFIGURATION, flowInitializer);
            ctx.setAttribute(SERVLET_ATTRIBUTE_FORM_CONFIGURATION_CONFIGURATION, formInitializer);
        }
    }
}