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

import org.opensingular.requirement.commons.admin.healthsystem.validation.database.IValidatorDatabase;
import org.opensingular.requirement.commons.service.SingularRequirementService;
import org.opensingular.requirement.commons.spring.security.AuthorizationService;
import org.opensingular.requirement.commons.test.ValidatorOracleMock;
import org.opensingular.requirement.commons.wicket.view.template.MenuService;
import org.opensingular.requirement.module.service.ServerMenuService;
import org.opensingular.requirement.module.service.SingularRequirementServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import static org.mockito.Mockito.*;

@Configuration
public class ModuleConfigurationMock {

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
    public AuthorizationService authorizationService() {
        return spy(mock(AuthorizationService.class));
    }

    @Primary
    @Bean
    public IValidatorDatabase validatorDatabase() {
        return spy(ValidatorOracleMock.class);
    }
}
