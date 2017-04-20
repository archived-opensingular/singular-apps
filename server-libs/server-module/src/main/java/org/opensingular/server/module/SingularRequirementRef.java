package org.opensingular.server.module;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.opensingular.lib.commons.lambda.IFunction;
import org.opensingular.server.commons.requirement.SingularRequirement;
import org.opensingular.server.module.requirement.builder.SingularRequirementBuilder;

import java.util.UUID;

/**
 * Requirement Reference to check equality against same requirements provided many times in configuration classes
 * Two requirementes are considered equal if it have the same name.
 */
public class SingularRequirementRef {

    private SingularRequirement                                        requirement;
    private IFunction<SingularRequirementBuilder, SingularRequirement> requirementProvider;
    private String                                                     id;

    SingularRequirementRef(SingularRequirement requirement) {
        this.requirement = requirement;
    }

    SingularRequirementRef(IFunction<SingularRequirementBuilder, SingularRequirement> requirementProvider) {
        this.requirementProvider = requirementProvider;

    }

    //TODO vinicius.nunes modifiquei para public
    public SingularRequirement getRequirement() {
        if (requirement == null) {
            this.requirement = requirementProvider.apply(new SingularRequirementBuilder());
        }
        return requirement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SingularRequirementRef that = (SingularRequirementRef) o;
        if (this.getRequirement() != null && that.getRequirement() != null) {
            return this.getRequirement().getName().equals(that.getRequirement().getName());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(requirement)
                .append(requirementProvider)
                .toHashCode();
    }

    String getRequirementDescription() {
        return requirement.getName();
    }


    public String getId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }
}
