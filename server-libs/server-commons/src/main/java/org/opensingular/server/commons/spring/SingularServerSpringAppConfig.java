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

package org.opensingular.server.commons.spring;

import org.hibernate.SessionFactory;
import org.opensingular.flow.persistence.service.ProcessRetrieveService;
import org.opensingular.form.context.ServiceRegistry;
import org.opensingular.form.context.ServiceRegistryLocator;
import org.opensingular.form.context.SingularFormContext;
import org.opensingular.form.spring.SpringServiceRegistry;
import org.opensingular.form.wicket.SingularFormConfigWicket;
import org.opensingular.form.wicket.SingularFormConfigWicketImpl;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.context.singleton.SpringBoundedSingletonStrategy;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.lib.support.spring.util.AutoScanDisabled;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;


@EnableTransactionManagement(proxyTargetClass = true)
@EnableCaching
@EnableWebMvc
@EnableWebSecurity
@ComponentScan(
        basePackages = {"org.opensingular", "com.opensingular"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION,
                        value = AutoScanDisabled.class)
        })
public class SingularServerSpringAppConfig implements Loggable {

    @Order(1)
    @Bean
    @Lazy(false)
    public SpringBoundedSingletonStrategy springBoundedSingletonStrategy(){
        return new SpringBoundedSingletonStrategy();
    }

    @Bean
    public SingularFormConfigWicket getSingularFormConfig(SpringServiceRegistry springServiceRegistry) {
        SingularFormConfigWicket singularFormConfigWicket = new SingularFormConfigWicketImpl();
        singularFormConfigWicket.setServiceRegistry(springServiceRegistry);
        return singularFormConfigWicket;
    }

    @Bean
    public SingularFormContext getSingularFormContext(SingularFormConfigWicket singularFormConfigWicket) {
        return singularFormConfigWicket.createContext();
    }

    @Bean
    public ProcessRetrieveService getProcessRetrieveService(SessionFactory sessionFactory) {
        ProcessRetrieveService processRetrieveService = new ProcessRetrieveService();
        processRetrieveService.setSessionLocator(sessionFactory::getCurrentSession);
        return processRetrieveService;
    }

}
