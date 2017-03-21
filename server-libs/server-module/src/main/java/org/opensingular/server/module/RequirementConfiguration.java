package org.opensingular.server.module;

import org.opensingular.lib.commons.lambda.IFunction;
import org.opensingular.server.module.requirement.SingularRequirement;
import org.opensingular.server.module.requirement.builder.SingularRequirementBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration object for module {@link SingularRequirement} registration.
 *
 */
public class RequirementConfiguration {

    private List<SingularRequirement> requirements = new ArrayList<>();

    /**
     * Register a  {@link SingularRequirement}
     * @param requirement
     *  the {@link SingularRequirement} instance.
     * @return
     */
    public RequirementConfiguration addRequirement(SingularRequirement requirement) {
        requirements.add(requirement);
        return this;
    }

    /**
     * Register a  {@link SingularRequirement}
     * @param requirementProvider
     *  a {@link IFunction<SingularRequirementBuilder, SingularRequirement> } lambda to build the requirement definition
     *  through an fluent interface builder.
     * @return
     */
    public RequirementConfiguration addRequirement(IFunction<SingularRequirementBuilder, SingularRequirement> requirementProvider) {
        requirements.add(requirementProvider.apply(new SingularRequirementBuilder()));
        return this;
    }

    List<SingularRequirement> getRequirements() {
        return requirements;
    }
}
