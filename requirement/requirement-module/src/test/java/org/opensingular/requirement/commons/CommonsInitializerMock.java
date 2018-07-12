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

package org.opensingular.requirement.commons;

import org.opensingular.app.commons.spring.persistence.SingularPersistenceDefaultBeanFactory;
import org.opensingular.requirement.module.config.*;

import org.opensingular.requirement.module.wicket.SingularRequirementApplication;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;


public class CommonsInitializerMock implements SingularInitializer {
    
    public static final String   TESTE                      = "GRUPO_TESTE";
    public static final String   SPRING_MVC_SERVLET_MAPPING = "/*";
    private AnnotationConfigWebApplicationContext applicationContext;

    public CommonsInitializerMock(AnnotationConfigWebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public CommonsInitializerMock() {
    }

    @Override
    public WebInitializer webConfiguration() {
        return new WebInitializer() {
            @Override
            protected Class<? extends SingularRequirementApplication> getWicketApplicationClass(IServerContext context) {
                return CommonsApplicationMock.class;
            }
        };
    }

    @Override
    public Class<? extends SingularSpringWebMVCConfig> getSingularSpringWebMVCConfig() {
        return SingularSpringWebMVCConfig.class;
    }

    @Override
    public FormInitializer formConfiguration() {
        return new FormInitializer();
    }

    @Override
    public SpringSecurityInitializer springSecurityConfiguration() {
        return new SpringSecurityInitializer();
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

            @Override
            protected Class<? extends SingularPersistenceDefaultBeanFactory> persistenceConfiguration() {
              return persistenceConfiguration();
            }
        };
    }


    protected Class<? extends SingularPersistenceDefaultBeanFactory> persistenceConfiguration() {
        return SingularPersistenceDefaultBeanFactory.class;
    }


    @Override
    public FlowInitializer flowConfiguration() {
        return new FlowInitializer() {

            @Override
            public String moduleCod() {
                return TESTE;
            }
        };
    }

    @Override
    public SchedulerInitializer schedulerConfiguration() {
        return new SchedulerInitializer() {
            @Override
            public Class<?> mailConfiguration() {
                return Object.class;
            }

            @Override
            public Class<?> attachmentGCConfiguration() {
                return Object.class;
            }
        };
    }


}