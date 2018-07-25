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

package org.opensingular.requirement.module.spring.security;

import java.util.List;
import javax.annotation.Nullable;

import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.box.action.BoxItemActionList;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.service.dto.BoxConfigurationData;

/**
 * Service responsible every authorization check in a Singular Requirement Application.
 */
public interface AuthorizationService extends Loggable {
    String LIST_TASKS_PERMISSION_PREFIX = "LIST_TASKS";
    String SEPARATOR                    = "|$|";


    void filterBoxWithPermissions(List<BoxConfigurationData> groupDTOs, String idUsuario);

    void filterActions(String formType, Long requirementId, BoxItemActionList actions, String idUsuario);

    List<SingularPermission> filterListTaskPermissions(List<SingularPermission> permissions);

    boolean hasPermission(Long requirementId, String formType, String idUsuario, String action);

    /**
     * Checks if the current user has the permission needed to do perform the given {@param action} and then perform the following checks:
     * 1) If the user is the representative assigned to the the given requirement the access will be granted (external user scenario).
     * 2) If the access is readonly ({@param readonly}), the access will be granted (external or internal scenario).
     * 3) Otherwise it will be checked if the current user is the one assigned (allocated) to the given requirement (internal user scenario).
     * *
     *
     * @param requirementId current requirement id
     * @param formType      form type name
     * @param userId        logged in user
     * @param applicantId   representation selected by current user
     * @param action        action whose access must be checked
     * @param readonly      inform if the action is a readonly action
     * @return
     */
    boolean hasPermission(Long requirementId, String formType, String userId, @Nullable String applicantId, String action, IServerContext context, boolean readonly);
}
