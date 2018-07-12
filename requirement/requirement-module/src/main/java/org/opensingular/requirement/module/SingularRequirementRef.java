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

package org.opensingular.requirement.module;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.opensingular.lib.commons.lambda.IFunction;
import org.opensingular.requirement.module.persistence.entity.form.RequirementDefinitionEntity;
import org.opensingular.requirement.module.SingularRequirement;
import org.opensingular.requirement.module.builder.SingularRequirementBuilder;

/**
 * Requirement Reference to check equality against same requirements provided many times in configuration classes
 * Two requirementes are considered equal if it have the same name.
 */
public class SingularRequirementRef {

    private SingularRequirement                                        requirement;
    private IFunction<SingularRequirementBuilder, SingularRequirement> requirementProvider;
    private RequirementDefinitionEntity                                requirementDefinitionEntity;

    SingularRequirementRef(SingularRequirement requirement) {
        this.requirement = requirement;
    }

    SingularRequirementRef(IFunction<SingularRequirementBuilder, SingularRequirement> requirementProvider) {
        this.requirementProvider = requirementProvider;

    }

    public SingularRequirement getRequirement() {
        if (requirement == null) {
            this.requirement = requirementProvider.apply(new SingularRequirementBuilder());
        }
        return requirement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SingularRequirementRef that = (SingularRequirementRef) o;
        if (this.getRequirement() != null && that.getRequirement() != null) {
            return this.getRequirement().getName().equals(that.getRequirement().getName());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(requirement)
                .append(requirementProvider)
                .toHashCode();
    }

    String getRequirementDescription() {
        return requirement.getName();
    }

    public Long getId() {
        if (getRequirementDefinitionEntity() == null) {
            return null;
        }
        return getRequirementDefinitionEntity().getCod();
    }

    public RequirementDefinitionEntity getRequirementDefinitionEntity() {
        return requirementDefinitionEntity;
    }

    public void setRequirementDefinitionEntity(RequirementDefinitionEntity requirementDefinitionEntity) {
        this.requirementDefinitionEntity = requirementDefinitionEntity;
    }
}
