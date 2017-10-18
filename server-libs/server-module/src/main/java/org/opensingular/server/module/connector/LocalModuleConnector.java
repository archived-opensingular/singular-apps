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

package org.opensingular.server.module.connector;

import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.server.commons.ModuleConnector;
import org.opensingular.server.commons.WorkspaceConfigurationMetadata;
import org.opensingular.server.commons.box.BoxItemDataList;
import org.opensingular.server.commons.box.action.ActionRequest;
import org.opensingular.server.commons.box.action.ActionResponse;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.module.rest.ModuleBackstageService;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

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
        return moduleBackstageService.listAllocableUsers(selectedTask);
    }

}