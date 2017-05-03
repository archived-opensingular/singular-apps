package org.opensingular.server.module.requirement.builder;

import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.server.commons.service.PetitionSender;

public class SingularRequirementDefinitionForms {

    private SingularRequirementBuilderContext builderContext;

    SingularRequirementDefinitionForms(SingularRequirementBuilderContext builderContext) {
        this.builderContext = builderContext;
    }

    public SingularRequirementDefinitionFlows allowedFlow(Class<? extends ProcessDefinition> flowClass) {
        return new SingularRequirementDefinitionFlows(builderContext.addFlowClass(flowClass));
    }

    public SingularRequirementDefinitionForms petitionSenderBeanClass(Class<? extends PetitionSender> petitionSender) {
        builderContext.setPetitionSenderBeanClass(petitionSender);
        return this;
    }
}