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

package org.opensingular.requirement.module;

import org.opensingular.form.SInstance;
import org.opensingular.requirement.module.exception.SingularRequirementException;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;

import org.opensingular.form.SInstance;
import org.opensingular.requirement.module.exception.SingularRequirementException;

/**
 * Decide wich requirement should be presented based
 * on a SInstance filled by the user.
 * See {@link SingularRequirementResolver}.
 */
public abstract class RequirementResolver<SI extends SInstance> {

    private List<RequirementDefinition> requirements;



    public RequirementResolver(RequirementDefinition... requirements) {
        this.requirements = Collections.unmodifiableList(Arrays.asList(requirements));
    }


    RequirementDefinition resolve(SI instance) {
        RequirementDefinition requirement = resolve(instance, requirements);
        if (!requirements.contains(requirement)) {
            throw new SingularRequirementException("The resolved requirement: \"" + Optional.ofNullable(requirement).map(RequirementDefinition::getName).orElse(null) + "\" is not available in the given requirement list.");
        }
        return requirement;
    }


    /**
     * Resolve an requirement definition using provided {@param instance}. The resolved instance must be
     * present in the given {@param availableRequirements} list
     * @param instance
     * @param availableRequirements
     * @return
     */
    @Nonnull
    protected abstract RequirementDefinition resolve(SI instance, List<RequirementDefinition> availableRequirements);


}
