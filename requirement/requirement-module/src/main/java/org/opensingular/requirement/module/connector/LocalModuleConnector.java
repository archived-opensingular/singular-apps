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
import javax.inject.Inject;

import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.requirement.module.ModuleConnector;
import org.opensingular.requirement.module.WorkspaceConfigurationMetadata;
import org.opensingular.requirement.module.box.BoxItemDataList;
import org.opensingular.requirement.module.box.action.ActionRequest;
import org.opensingular.requirement.module.box.action.ActionResponse;
import org.opensingular.requirement.module.persistence.filter.QuickFilter;
import org.opensingular.requirement.module.rest.ModuleBackstageService;

public class LocalModuleConnector implements ModuleConnector {

    @Inject
    private ModuleBackstageService moduleBackstageService;

    @Override
    public Long count(String boxId, QuickFilter filter) {
        return moduleBackstageService.count(boxId, filter);
    }

    @Override
    public BoxItemDataList search(String boxId, QuickFilter filter) {
        return moduleBackstageService.search(boxId, filter);
    }

    @Override
    public ActionResponse execute(Long id, ActionRequest actionRequest) {
        return moduleBackstageService.executar(id, actionRequest);
    }

    @Override
    public WorkspaceConfigurationMetadata loadWorkspaceConfiguration(String context, String user) {
        return new WorkspaceConfigurationMetadata(moduleBackstageService.listMenu(context, user));
    }

    @Override
    public List<Actor> listUsers(Map<String, Object> selectedTask) {
        return moduleBackstageService.listAllowedUsers(selectedTask);
    }

}