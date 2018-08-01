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

package org.opensingular.requirement.module.connector;

import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.form.persistence.entity.FormTypeEntity;
import org.opensingular.requirement.module.SingularRequirement;
import org.opensingular.requirement.module.box.BoxItemDataMap;
import org.opensingular.requirement.module.box.action.ActionRequest;
import org.opensingular.requirement.module.box.action.ActionResponse;
import org.opensingular.requirement.module.persistence.entity.form.RequirementDefinitionEntity;
import org.opensingular.requirement.module.persistence.filter.BoxFilter;
import org.opensingular.requirement.module.service.dto.BoxItemAction;
import org.opensingular.requirement.module.service.dto.ItemActionConfirmation;
import org.opensingular.requirement.module.service.dto.ItemBox;

import java.util.List;
import java.util.Map;

public interface ModuleService {

    /**
     * Count all elements inside a box
     */
    String countAll(ItemBox box);

    /**
     * Count elements inside a box, applying the filter
     */
    long countFiltered(ItemBox box, BoxFilter filter);

    /**
     * Searchelements inside a box, applying the filter
     */
    List<BoxItemDataMap> searchFiltered(ItemBox box, BoxFilter filter);

    /**
     * Find users that can execute the confirmAction
     */
    List<Actor> findEligibleUsers(BoxItemDataMap rowItemData, ItemActionConfirmation confirmAction);

    /**
     * Execute a action
     */
    ActionResponse executeAction(BoxItemAction rowAction, Map<String, String> params, ActionRequest actionRequest);

    /**
     * Build a static endpoint
     */
    String buildUrlToBeRedirected(BoxItemDataMap rowItemData, BoxItemAction rowAction, Map<String, String> params, String baseURI);

    /**
     *
     */
    RequirementDefinitionEntity getOrCreateRequirementDefinition(SingularRequirement singularRequirement, FormTypeEntity formType);

    /**
     *
     */
    ModuleEntity getModule();

    /**
     *
     */
    String getBaseUrl();

    /**
     *
     */
    String getModuleContext();

    /**
     * Persiste se necessário o RequirementDefinitionEntity
     * e atualiza no ref o valor que está em banco.
     *
     * @param singularRequirement - o requerimento do qual o {@link RequirementDefinitionEntity} será criado
     */
    void save(SingularRequirement singularRequirement);

}