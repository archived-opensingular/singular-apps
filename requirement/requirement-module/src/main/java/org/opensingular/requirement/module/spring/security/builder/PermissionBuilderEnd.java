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

package org.opensingular.requirement.module.spring.security.builder;

import org.opensingular.requirement.module.persistence.entity.FeaturePermissionEntity;
import org.opensingular.requirement.module.persistence.entity.FeaturePermissionEntityPK;
import org.opensingular.requirement.module.spring.security.SingularPermission;

import java.util.ArrayList;
import java.util.List;

public class PermissionBuilderEnd {


    private final List<String> actions;
    private final List<String> forms;
    private final List<String> flows;
    private final List<String> taskDefinitions;

    PermissionBuilderEnd(List<String> actions, List<String> forms, List<String> flows, List<String> taskDefinitions) {
        this.actions = actions;
        this.forms = forms;
        this.flows = flows;
        this.taskDefinitions = taskDefinitions;
    }

    public List<SingularPermission> list() {
        List<SingularPermission> permissions = new ArrayList<>();
        for (String action : actions) {
            for (String form : forms) {
                for (String flow : flows) {
                    for (String taskDefinition : taskDefinitions) {
                        permissions.add(new SingularPermission(action, form, flow, taskDefinition));
                    }
                }
            }
        }
        return permissions;
    }

    public List<FeaturePermissionEntity> listEntities(String moduleKey, String clientRoleId) {
        List<FeaturePermissionEntity> fpes = new ArrayList<>();
        for (SingularPermission singularPermission : list()) {
            fpes.add(new FeaturePermissionEntity(new FeaturePermissionEntityPK(singularPermission.getSingularId(), clientRoleId, moduleKey)));
        }
        return fpes;
    }
}
