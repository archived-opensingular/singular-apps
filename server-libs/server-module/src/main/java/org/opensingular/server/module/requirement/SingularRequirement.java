package org.opensingular.server.module.requirement;

import org.opensingular.form.SType;
import org.opensingular.lib.commons.lambda.ISupplier;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.module.wicket.view.util.form.FormPage;

/**
 * Singular requirement specification.
 * This class groups the main Singular components needed to orchestrate the requirement.
 */
public interface SingularRequirement {

    /**
     * The requirement descriptive name
     *
     * @return
     */
    String getName();

    /**
     * A {@link SType} representing the main form for this requirment.
     * The main form is always the initial application form
     *
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


    /**
     * Returns a custom initial form page.
     * Defaults to {@link FormPage}
     *
     * @param <P>
     * @return
     */
    default <P extends FormPage> ISupplier<P> getInitialPage() {
        return () -> {
            try {
                return (P) FormPage.class.newInstance();
            } catch (Exception e) {
                throw SingularServerException.rethrow(e.getMessage(), e);
            }
        };
    }

}
