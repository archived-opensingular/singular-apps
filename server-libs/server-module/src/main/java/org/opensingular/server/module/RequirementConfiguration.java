package org.opensingular.server.module;

import org.opensingular.lib.commons.lambda.IFunction;
import org.opensingular.server.module.requirement.SingularRequirement;
import org.opensingular.server.module.requirement.builder.SingularRequirementBuilder;

import java.util.ArrayList;
import java.util.List;

public class RequirementConfiguration {

    private List<SingularRequirement> requirements = new ArrayList<>();

    public RequirementConfiguration add(SingularRequirement requirement) {
        requirements.add(requirement);
        return this;
    }

    public RequirementConfiguration add(IFunction<SingularRequirementBuilder, SingularRequirement> requirementProvider) {
        requirements.add(requirementProvider.apply(new SingularRequirementBuilder()));
        return this;
    }

    List<SingularRequirement> getRequirements() {
        return requirements;
    }
}
