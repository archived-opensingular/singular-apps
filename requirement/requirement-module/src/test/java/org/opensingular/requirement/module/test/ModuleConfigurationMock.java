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

package org.opensingular.requirement.module.test;

import org.opensingular.lib.support.spring.util.AutoScanDisabled;
import org.opensingular.requirement.module.admin.healthsystem.validation.database.IValidatorDatabase;
import org.opensingular.requirement.module.service.ServerMenuService;
import org.opensingular.requirement.module.service.SingularRequirementService;
import org.opensingular.requirement.module.service.SingularRequirementServiceImpl;
import org.opensingular.requirement.module.spring.security.AuthorizationService;
import org.opensingular.requirement.module.spring.security.AuthorizationServiceImpl;
import org.opensingular.requirement.module.wicket.view.template.MenuService;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

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
public class ModuleConfigurationMock {

    @Primary
    @Bean
    public AuthorizationService authorizationService() {
        return spy(AuthorizationServiceImpl.class);
    }

    @Primary
    @Bean
    public SingularRequirementService requirementService(){
        return new SingularRequirementServiceImpl();
    }


    @Primary
    @Bean
    @Scope("session")
    public MenuService menuService() {
        return new ServerMenuService();
    }


    @Primary
    @Bean
    public IValidatorDatabase validatorDatabase() {
        return spy(ValidatorOracleMock.class);
    }
}
