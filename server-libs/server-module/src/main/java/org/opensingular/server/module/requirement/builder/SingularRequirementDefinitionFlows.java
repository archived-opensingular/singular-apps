package org.opensingular.server.module.requirement.builder;

import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.server.module.requirement.BoundedFlowResolver;
import org.opensingular.server.module.requirement.DynamicFormFlowSingularRequirement;
import org.opensingular.server.commons.requirement.SingularRequirement;

import java.util.Set;

public class SingularRequirementDefinitionFlows {

    private SingularRequirementBuilderContext builderContext;

    public SingularRequirementDefinitionFlows(SingularRequirementBuilderContext builderContext) {
        this.builderContext = builderContext;
    }

    public SingularRequirementDefinitionFlowResolver allowedFlow(Class<? extends ProcessDefinition> flowClass) {
        return new SingularRequirementDefinitionFlowResolver(builderContext.addFlowClass(flowClass));
    }

    public SingularRequirement build() {
        Set<Class<? extends ProcessDefinition>> flowClasses = builderContext.getFlowClasses();
        return new DynamicFormFlowSingularRequirement(
                builderContext.getName(),
                builderContext.getMainForm(),
                new BoundedFlowResolver((s, c) -> flowClasses.stream().findFirst(), flowClasses),
                builderContext.getDefaultExecutionPage());
    }

}
