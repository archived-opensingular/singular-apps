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

import org.opensingular.lib.commons.context.SingularContextSetup;
import org.opensingular.requirement.module.WorkspaceInitializer;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Optional;

public class SingularWebApplicationInitializer implements WebApplicationInitializer {

    private static Logger logger = LoggerFactory.getLogger(SingularWebApplicationInitializer.class);

    public static String SINGULAR = "[SINGULAR] {}";
    public static String SERVLET_ATTRIBUTE_WEB_CONFIGURATION = "Singular-webInitializer";
    public static String SERVLET_ATTRIBUTE_SPRING_HIBERNATE_CONFIGURATION = "Singular-springHibernateInitializer";
    public static String SERVLET_ATTRIBUTE_FORM_CONFIGURATION_CONFIGURATION = "Singular-formInitializer";
    public static String SERVLET_ATTRIBUTE_FLOW_CONFIGURATION_CONFIGURATION = "Singular-flowInitializer";
    public static String SERVLET_ATTRIBUTE_SECURITY_CONFIGURATION_CONFIGURATION = "Singular-Security";

    private SingularInitializer singularInitializer;

    public SingularWebApplicationInitializer() {
        this(SingularServerInitializerProvider.get().retrieve());
    }

    public SingularWebApplicationInitializer(SingularInitializer singularInitializer) {
        this.singularInitializer = singularInitializer;
    }

    @Override
    public void onStartup(ServletContext ctx) throws ServletException {
        SingularContextSetup.reset();
        logger.info(SINGULAR, " Initializing Singular.... ");

        logger.info(SINGULAR, " Initializing SpringHibernateConfiguration ");
        SpringHibernateInitializer springHibernateInitializer = singularInitializer.springHibernateConfiguration();
        AnnotationConfigWebApplicationContext applicationContext;
        if (springHibernateInitializer != null) {
            applicationContext = springHibernateInitializer.init(ctx);
            ctx.setAttribute(SERVLET_ATTRIBUTE_SPRING_HIBERNATE_CONFIGURATION, springHibernateInitializer);
        } else {
            throw new SingularServerException("Não foi possivel configurar o ApplicationContext");
        }

        applicationContext.register(singularInitializer.getSingularSpringWebMVCConfig());

        logger.info(SINGULAR, " Initializing FormConfiguration ");
        FormInitializer formInitializer = singularInitializer.formConfiguration();
        if (formInitializer != null) {
            formInitializer.init(ctx, applicationContext);
            ctx.setAttribute(SERVLET_ATTRIBUTE_FORM_CONFIGURATION_CONFIGURATION, formInitializer);
        } else {
            logger.info(SINGULAR, " Null formInitializer, skipping Singular Form configuration");
        }

        logger.info(SINGULAR, " Initializing FlowConfiguration ");
        FlowInitializer flowInitializer = singularInitializer.flowConfiguration();
        if (flowInitializer != null) {
            flowInitializer.init(ctx, applicationContext);
            ctx.setAttribute(SERVLET_ATTRIBUTE_FLOW_CONFIGURATION_CONFIGURATION, flowInitializer);
        } else {
            logger.info(SINGULAR, " Null flowInitializer, skipping Singular Flow configuration");
        }

        logger.info(SINGULAR, " Initializing WebConfiguration ");
        WebInitializer webInitializer = singularInitializer.webConfiguration();
        if (webInitializer != null) {
            webInitializer.init(ctx);
            ctx.setAttribute(SERVLET_ATTRIBUTE_WEB_CONFIGURATION, webInitializer);
        } else {
            logger.info(SINGULAR, " Null webInitializer, skipping web configuration");
        }

        logger.info(SINGULAR, " Initializing SpringSecurity ");
        SpringSecurityInitializer springSecurityInitializer = singularInitializer.springSecurityConfiguration();
        if (springSecurityInitializer != null) {
            springSecurityInitializer.init(ctx, applicationContext, Optional.of(springHibernateInitializer)
                    .map(SpringHibernateInitializer::springMVCServletMapping)
                    .orElse(null));
            ctx.setAttribute(SERVLET_ATTRIBUTE_SECURITY_CONFIGURATION_CONFIGURATION, springSecurityInitializer);
        } else {
            logger.info(SINGULAR, " Null springSecurityInitializer, skipping Spring Security configuration");
        }

        logger.info(SINGULAR, " Initializing WorkspaceInitializer ");
        WorkspaceInitializer workspaceInitializer = singularInitializer.workspaceConfiguration();
        if (workspaceInitializer != null) {
            workspaceInitializer.init(ctx, applicationContext);
        } else {
            logger.info(SINGULAR, " Null WorkspaceInitializer, skipping Spring Workspace configuration");
        }

    }
}