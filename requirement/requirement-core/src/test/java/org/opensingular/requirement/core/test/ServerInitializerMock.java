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

package org.opensingular.requirement.core.test;

import org.opensingular.requirement.commons.config.IServerContext;
import org.opensingular.requirement.commons.config.PWebInitializer;
import org.opensingular.requirement.commons.config.SchedulerInitializer;
import org.opensingular.requirement.commons.config.SpringHibernateInitializer;


import org.opensingular.requirement.commons.wicket.SingularRequirementApplication;
import org.opensingular.requirement.core.config.AttachmentGCSchedulerInitializer;
import org.opensingular.requirement.core.config.MailSenderSchedulerInitializer;
import org.opensingular.server.commons.test.CommonsApplicationMock;
import org.opensingular.server.commons.test.CommonsInitializerMock;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;


public class ServerInitializerMock extends CommonsInitializerMock {

    public static final String   TESTE                      = "Teste";
    public static final String[] DEFINITIONS_PACKS_ARRAY    = new String[]{"org.opensingular.server.commons.test"};
    public static final String   SPRING_MVC_SERVLET_MAPPING = "/*";
    private AnnotationConfigWebApplicationContext applicationContext;

    public ServerInitializerMock(AnnotationConfigWebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ServerInitializerMock() {
    }

    @Override
    public PWebInitializer webConfiguration() {
        return new PWebInitializer() {
            @Override
            protected Class<? extends SingularRequirementApplication> getWicketApplicationClass(IServerContext context) {
                return CommonsApplicationMock.class;
            }
        };
    }

    @Override
    public SpringHibernateInitializer springHibernateConfiguration() {
        return new SpringHibernateInitializer() {
            @Override
            protected AnnotationConfigWebApplicationContext newApplicationContext() {
                return applicationContext;
            }

            @Override
            protected String springMVCServletMapping() {
                return SPRING_MVC_SERVLET_MAPPING;
            }
        };
    }

    @Override
    public SchedulerInitializer schedulerConfiguration() {
        return new SchedulerInitializer() {
            @Override
            public Class<?> mailConfiguration() {
                return MailSenderSchedulerInitializer.class;
            }

            @Override
            public Class<?> attachmentGCConfiguration() {
                return AttachmentGCSchedulerInitializer.class;
            }
        };
    }


}
