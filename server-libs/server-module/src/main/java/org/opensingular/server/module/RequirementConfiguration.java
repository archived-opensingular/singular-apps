package org.opensingular.server.module;

import org.opensingular.form.SingularFormException;
import org.opensingular.lib.commons.lambda.IFunction;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.requirement.SingularRequirement;
import org.opensingular.server.module.requirement.builder.SingularRequirementBuilder;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Configuration object for module {@link SingularRequirement} registration.
 */
public class RequirementConfiguration {

    private Set<SingularRequirementRef> requirements = new LinkedHashSet<>();

    /**
     * Register a  {@link SingularRequirement}
     *
     * @param requirement the {@link SingularRequirement} instance.
     * @return
     */
    public RequirementConfiguration addRequirement(SingularRequirement requirement) {
        requirements.add(new SingularRequirementRef(requirement));
        return this;
    }

    /**
     * Register a  {@link SingularRequirement}
     *
     * @param requirementProvider a {@link IFunction<SingularRequirementBuilder, SingularRequirement> } lambda to build the requirement definition
     *                            through an fluent interface builder.
     * @return
     */
    public RequirementConfiguration addRequirement(IFunction<SingularRequirementBuilder, SingularRequirement> requirementProvider) {
        if (!requirements.add(new SingularRequirementRef(requirementProvider))) {
            throw new SingularServerException("O mesmo " + SingularRequirement.class.getName() + " não pode ser configurado duas vezes no módulo");
        }
        return this;
    }


    public SingularRequirementRef getRequirementRef(SingularRequirement requirement) {
        return requirements
                .stream()
                .filter(ref -> ref.equals(new SingularRequirementRef(requirement)))
                .findFirst()
                .orElseThrow(() -> new SingularFormException("Não foi possível encontrar referência registrada para o requerimento informado"));
    }

    public SingularRequirementRef getRequirementRef(IFunction<SingularRequirementBuilder, SingularRequirement> requirementProvider) {
        return requirements
                .stream()
                .filter(ref -> ref.equals(new SingularRequirementRef(requirementProvider)))
                .findFirst()
                .orElseThrow(() -> new SingularFormException("Não foi possível encontrar referência registrada para o requerimento informado"));
    }

    List<SingularRequirementRef> getRequirements() {
        return requirements.stream().collect(Collectors.toList());
    }
}
