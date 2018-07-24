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

package org.opensingular.requirement.module.builder;

import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.requirement.module.SingularRequirement;
import org.opensingular.requirement.module.BoundedFlowResolver;
import org.opensingular.requirement.module.DynamicFormFlowSingularRequirement;

import java.util.Set;

public class SingularRequirementDefinitionFlows {

    private SingularRequirementBuilderContext builderContext;

    public SingularRequirementDefinitionFlows(SingularRequirementBuilderContext builderContext) {
        this.builderContext = builderContext;
    }

    public SingularRequirementDefinitionFlowResolver allowedFlow(Class<? extends FlowDefinition> flowClass) {
        return new SingularRequirementDefinitionFlowResolver(builderContext.addFlowClass(flowClass));
    }

    public SingularRequirement build() {
        Set<Class<? extends FlowDefinition>> flowClasses = builderContext.getFlowClasses();
        return new DynamicFormFlowSingularRequirement(
                builderContext.getName(),
                builderContext.getMainForm(),
                new BoundedFlowResolver((s, c) -> flowClasses.stream().findFirst(), flowClasses),
                builderContext.getDefaultExecutionPage(),
                builderContext.getRequirementSenderBeanClass());
    }

}
