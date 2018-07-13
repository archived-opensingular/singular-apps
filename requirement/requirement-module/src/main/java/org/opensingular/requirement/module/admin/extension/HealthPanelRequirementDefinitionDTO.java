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

package org.opensingular.requirement.module.admin.extension;

import org.opensingular.form.SFormUtil;
import org.opensingular.form.SType;
import org.opensingular.requirement.module.RequirementDefinition;
import org.opensingular.requirement.module.SingularRequirementRef;

import java.io.Serializable;

public class HealthPanelRequirementDefinitionDTO implements Serializable {

    private final Long                   id;
    private final String                 name;
    private final Class<? extends SType> mainForm;

    public HealthPanelRequirementDefinitionDTO(SingularRequirementRef singularRequirementRef) {
        RequirementDefinition requirement = singularRequirementRef.getRequirement();
        id = singularRequirementRef.getId();
        name = requirement.getName();
        mainForm = requirement.getMainForm();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Class<? extends SType> getMainForm() {
        return mainForm;
    }

    public String getMainFormName() {
        return SFormUtil.getTypeName((Class<? extends SType<?>>) mainForm);
    }
}
