package org.opensingular.server.module.requirement.builder;

import org.opensingular.server.commons.requirement.SingularRequirement;

public class SingularRequirementDefinitionFlow {

    private SingularRequirement requirement;

    SingularRequirementDefinitionFlow(SingularRequirement requirement) {
        this.requirement = requirement;
    }

    public SingularRequirement build(){
        return requirement;
    }

}
