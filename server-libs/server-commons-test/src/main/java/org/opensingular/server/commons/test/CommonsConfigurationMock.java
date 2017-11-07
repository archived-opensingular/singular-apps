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

package org.opensingular.server.commons.test;

import org.opensingular.lib.support.spring.util.AutoScanDisabled;
import org.opensingular.server.commons.admin.healthsystem.validation.database.IValidatorDatabase;
import org.opensingular.server.commons.spring.SingularServerSpringAppConfig;
import org.opensingular.server.commons.spring.security.AuthorizationService;
import org.opensingular.server.commons.wicket.view.template.MenuService;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.inject.Inject;

import static org.mockito.Mockito.spy;

@EnableTransactionManagement(proxyTargetClass = true)
@EnableCaching
@EnableWebMvc
@EnableWebSecurity
@Configuration
@ComponentScan(
        basePackages = {"org.opensingular"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION,
                        value = AutoScanDisabled.class)
        })
public class CommonsConfigurationMock {

    @Inject
    private SingularServerSpringAppConfig singularServerSpringAppConfig;


    @Bean
    public MenuService menuService() {
        return new MenuServiceMock();
    }

    @Primary
    @Bean
    public AuthorizationService authorizationService() {
        return spy(AuthorizationService.class);
    }

    @Primary
    @Bean
    public IValidatorDatabase validatorDatabase() {
        return spy(ValidatorOracleMock.class);
    }
}
