package org.opensingular.server.module.requirement;

import org.opensingular.form.SType;

/**
 *
 */
public interface SingularRequirement {

    /**
     * The requirement descriptive name
     * @return
     */
    String getName();

    /**
     * A {@link SType} representing the main form for this requirment.
     * The main form is always the initial application form
     * @return
     */
    Class<? extends SType> getMainForm();

    /**
     * Returns an {@link BoundedFlowResolver} which is responsible to
     * select the start flow for this requirement based on {@link #getMainForm} SType properly filled.
     *
     * @return
     */
    BoundedFlowResolver getFlowResolver();

}
