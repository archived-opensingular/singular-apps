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
import org.opensingular.requirement.module.BoundedFlowResolver;
import org.opensingular.requirement.module.DynamicFormFlowSingularRequirement;
import org.opensingular.requirement.module.flow.FlowResolver;

public class SingularRequirementDefinitionFlowResolver {

    private SingularRequirementBuilderContext builderContext;

    public SingularRequirementDefinitionFlowResolver(SingularRequirementBuilderContext builderContext) {
        this.builderContext = builderContext;
    }

    public SingularRequirementDefinitionFlowResolver allowedFlow(Class<? extends FlowDefinition> flowClass) {
        builderContext.addFlowClass(flowClass);
        return this;
    }

    public SingularRequirementDefinitionFlow flowResolver(FlowResolver resolver) {
        return new SingularRequirementDefinitionFlow(new DynamicFormFlowSingularRequirement(
                builderContext.getName(),
                builderContext.getMainForm(),
                new BoundedFlowResolver(resolver, builderContext.getFlowClasses()),
                builderContext.getDefaultExecutionPage(),
                builderContext.getRequirementSenderBeanClass()));
    }

}