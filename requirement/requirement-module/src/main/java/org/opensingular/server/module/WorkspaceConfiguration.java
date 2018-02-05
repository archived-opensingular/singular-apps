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

package org.opensingular.server.module;

import org.opensingular.lib.commons.lambda.IFunction;
import org.opensingular.server.commons.requirement.SingularRequirement;
import org.opensingular.server.module.requirement.builder.SingularRequirementBuilder;
import org.opensingular.server.module.workspace.BoxDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Configuration object for module {@link BoxDefinition} registrations.
 */
public class WorkspaceConfiguration {

    private List<BoxController> itemBoxes = new ArrayList<>();
    private RequirementConfiguration requirementConfiguration;

    WorkspaceConfiguration(RequirementConfiguration requirementConfiguration) {
        this.requirementConfiguration = requirementConfiguration;
    }

    /**
     * Register a single {@link BoxDefinition}
     *
     * @param itemBox the
     * @return
     */
    public WorkspaceConfiguration addBox(BoxDefinition itemBox) {
        itemBoxes.add(new BoxController(itemBox));
        return this;
    }

    List<BoxController> getItemBoxes() {
        return itemBoxes;
    }


    public WorkspaceConfiguration newFor(RequirementProvider... requirementProvider) {
        Arrays
                .stream(requirementProvider)
                .map(requirementConfiguration::getRequirementRef)
                .forEach(r -> getCurrent().addRequirementRefs(r));
        return this;

    }

    public WorkspaceConfiguration newFor(SingularRequirement... requirement) {
        Arrays
                .stream(requirement)
                .map(requirementConfiguration::getRequirementRef)
                .forEach(r -> getCurrent().addRequirementRefs(r));
        return this;
    }


    private BoxController getCurrent() {
        return itemBoxes.get(itemBoxes.size() - 1);
    }

    public static interface RequirementProvider extends IFunction<SingularRequirementBuilder, SingularRequirement> {
    }
}
