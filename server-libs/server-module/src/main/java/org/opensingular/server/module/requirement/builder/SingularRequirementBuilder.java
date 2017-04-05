package org.opensingular.server.module.requirement.builder;

public class SingularRequirementBuilder {

    public SingularRequirementBuilder() {
    }

    public SingularRequirementDefinitionForm name(String name) {
        return new SingularRequirementDefinitionForm(new SingularRequirementBuilderContext().setName(name));
    }
}
