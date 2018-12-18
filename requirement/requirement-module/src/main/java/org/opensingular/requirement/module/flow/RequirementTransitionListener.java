package org.opensingular.requirement.module.flow;

import org.opensingular.flow.core.ITransitionListener;

/**
 * Listener para ser executado durante a realização de uma transição do fluxo.
 */
public interface RequirementTransitionListener extends ITransitionListener<RequirementTransitionContext> {

    /**
     * Possibilita interceptar a execução de uma transição, no caso do singular-server
     * isso ocorre após a consolidação dos rascunhos.
     *
     * @param requirementTransitionContext
     */
    @Override
    default void beforeTransition(RequirementTransitionContext requirementTransitionContext){

    }

    /**
     * Possibilita interceptar a execução de uma transição antes da consolidação dos rascunhos,
     * caso seja necessário alterar o conteúdo de um formulátio que esteja como rascunho antes que
     * ele seja persistido como um versão.
     *
     * @param requirementTransitionContext
     */
    default void beforeConsolidateDrafts(RequirementTransitionContext requirementTransitionContext) {

    }
}
