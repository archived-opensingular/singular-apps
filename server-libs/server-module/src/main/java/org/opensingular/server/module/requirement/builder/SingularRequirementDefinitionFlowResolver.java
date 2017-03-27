package org.opensingular.server.module.requirement.builder;

import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.server.commons.flow.FlowResolver;
import org.opensingular.server.module.requirement.BoundedFlowResolver;
import org.opensingular.server.module.requirement.DynamicFormFlowSingularRequirement;

public class SingularRequirementDefinitionFlowResolver {

    private SingularRequirementBuilderContext builderContext;

    public SingularRequirementDefinitionFlowResolver(SingularRequirementBuilderContext builderContext) {
        this.builderContext = builderContext;
    }

    public SingularRequirementDefinitionFlowResolver allowedFlow(Class<? extends ProcessDefinition> flowClass) {
        builderContext.addFlowClass(flowClass);
        return this;
    }

    public SingularRequirementDefinitionFlow flowResolver(FlowResolver resolver) {
        return new SingularRequirementDefinitionFlow(new DynamicFormFlowSingularRequirement(
                builderContext.getName(),
                builderContext.getMainForm(),
                new BoundedFlowResolver(resolver, builderContext.getFlowClasses()),
                builderContext.getInitPage()));
    }

}