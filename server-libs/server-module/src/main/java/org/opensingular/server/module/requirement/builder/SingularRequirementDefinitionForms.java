package org.opensingular.server.module.requirement.builder;

import org.opensingular.flow.core.ProcessDefinition;

public class SingularRequirementDefinitionForms {

    private SingularRequirementBuilderContext builderContext;

    SingularRequirementDefinitionForms(SingularRequirementBuilderContext builderContext) {
        this.builderContext = builderContext;
    }

    public SingularRequirementDefinitionFlows allowedFlow(Class<? extends ProcessDefinition> flowClass) {
        return new SingularRequirementDefinitionFlows(builderContext.addFlowClass(flowClass));
    }

}