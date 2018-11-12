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

package org.opensingular.requirement.module.provider;


import org.opensingular.requirement.module.ActionProvider;
import org.opensingular.requirement.module.service.RequirementService;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * TODO Vinicius, devemos conversar sobre a maneira padrão de criar objetos que usam spring beans e ao
 * mesmo tempo possuem estado, acredito que essa abordagem é mais facil de refatorar
 */
@Named
public class RequirementBoxItemDataProviderFactory {
    @Inject
    private RequirementService requirementService;

    public RequirementBoxItemDataProvider create(Boolean evalPermission, ActionProvider actionProvider) {
        return new RequirementBoxItemDataProvider(evalPermission, actionProvider, requirementService);
    }
}