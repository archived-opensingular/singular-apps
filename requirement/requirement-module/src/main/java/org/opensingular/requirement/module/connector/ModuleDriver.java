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

import java.util.List;
import java.util.Map;

import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.requirement.module.WorkspaceConfigurationMetadata;
import org.opensingular.requirement.module.box.BoxItemDataMap;
import org.opensingular.requirement.module.box.action.ActionRequest;
import org.opensingular.requirement.module.box.action.ActionResponse;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.persistence.filter.QuickFilter;
import org.opensingular.requirement.module.service.dto.BoxItemAction;
import org.opensingular.requirement.module.service.dto.ItemActionConfirmation;
import org.opensingular.requirement.module.service.dto.ItemBox;

public interface ModuleDriver {

    public static final String REST_FLOW = "/rest/flow";

    /**
     * Retrieve the module workspace
     */
    WorkspaceConfigurationMetadata retrieveModuleWorkspace(ModuleEntity module, IServerContext serverContext);

    /**
     * Count all elements inside a box
     */
    String countAll(ModuleEntity module, ItemBox box, List<String> flowNames, String loggedUser);

    /**
     * Count elements inside a box, applying the filter
     */
    long countFiltered(ModuleEntity module, ItemBox box, QuickFilter filter);

    /**
     * Searchelements inside a box, applying the filter
     */
    List<BoxItemDataMap> searchFiltered(ModuleEntity module, ItemBox box, QuickFilter filter);

    /**
     * Find users that can execute the confirmAction
     */
    List<Actor> findEligibleUsers(ModuleEntity module, BoxItemDataMap rowItemData, ItemActionConfirmation confirmAction);

    /**
     * Execute a action
     */
    ActionResponse executeAction(ModuleEntity module, BoxItemAction rowAction, Map<String, String> params, ActionRequest actionRequest);

    /**
     * Build a static endpoint
     */
    String buildUrlToBeRedirected(BoxItemDataMap rowItemData, BoxItemAction rowAction, Map<String, String> params, String baseURI);

}