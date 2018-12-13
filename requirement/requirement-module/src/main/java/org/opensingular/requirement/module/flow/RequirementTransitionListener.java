package org.opensingular.requirement.module.flow;

import org.opensingular.flow.core.ITransitionListener;

/**
 * Listener para ser executado durante a realização de uma transição do fluxo.
 */
public interface RequirementTransitionListener extends ITransitionListener<RequirementTransitionContext> {

    @Override
    void beforeTransition(RequirementTransitionContext requirementTransitionContext);
}
