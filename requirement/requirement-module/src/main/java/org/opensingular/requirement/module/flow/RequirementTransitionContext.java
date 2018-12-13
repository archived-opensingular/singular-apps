package org.opensingular.requirement.module.flow;

import org.opensingular.flow.core.ITransitionContext;
import org.opensingular.requirement.module.service.RequirementInstance;
import org.opensingular.requirement.module.service.Variable;

import java.util.List;

/**
 * Classe de contexto com informações de contexto para a execução de um {@link RequirementTransitionListener}
 */
public class RequirementTransitionContext implements ITransitionContext {

    private final RequirementInstance<?, ?> requirement;
    private final String transitionName;
    private final List<Variable> flowVariables;
    private final List<Variable> transitionVariables;

    public <RI extends RequirementInstance> RequirementTransitionContext(RI requirement, String transitionName,
                                                                         List<Variable> flowVariables,
                                                                         List<Variable> transitionVariables) {
        this.requirement = requirement;
        this.transitionName = transitionName;
        this.flowVariables = flowVariables;
        this.transitionVariables = transitionVariables;
    }

    public RequirementInstance<?, ?> getRequirement() {
        return requirement;
    }

    public String getTransitionName() {
        return transitionName;
    }

    public List<Variable> getFlowVariables() {
        return flowVariables;
    }

    public List<Variable> getTransitionVariables() {
        return transitionVariables;
    }
}
