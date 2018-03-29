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

import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.requirement.commons.box.BoxItemDataList;
import org.opensingular.requirement.commons.box.action.ActionRequest;
import org.opensingular.requirement.commons.box.action.ActionResponse;
import org.opensingular.requirement.commons.persistence.filter.QuickFilter;

import java.util.List;
import java.util.Map;

/**
 * Module connector interface for server communication
 */
public interface ModuleConnector {

    /**
     * Invoke count method for the box with the corresponding {@param boxId}
     *
     * @param boxId the box id
     * @param filter the filter
     * @return the count
     */
    Long count(String boxId, QuickFilter filter);


    /**
     * Invoke search method for the box with the corresponding {@param boxId}
     * Return the results in the ItemBoxDataList format
     *
     * @param boxId the box id
     * @param filter the filter
     * @return the count
     */
    BoxItemDataList search(String boxId, QuickFilter filter);

    /**
     * Executes custom actions defined in the {@link BoxItemDataList}
     *
     * @param id the id
     * @param actionRequest the actionRequest
     * @return the value
     */
    ActionResponse execute(Long id, ActionRequest actionRequest);

    /**
     *
     * @param context
     * @param user
     * @return
     */
    WorkspaceConfigurationMetadata loadWorkspaceConfiguration(String context, String user);

    /**
     *
     * @param selectedTask
     * @return
     */
    List<Actor> listUsers(Map<String, Object> selectedTask);
}
