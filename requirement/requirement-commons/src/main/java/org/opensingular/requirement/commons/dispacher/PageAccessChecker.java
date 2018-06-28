package org.opensingular.requirement.commons.dispacher;

import org.opensingular.requirement.commons.wicket.view.util.ActionContext;

/**
 * Check if the current ActionContext is allowed
 */
public interface PageAccessChecker {

    /**
     * Check if the requested page is accessible
     * @param context the ActionContext of the current request
     * @return true if is accessible
     */
    boolean hasAccess(ActionContext context);
}