package org.opensingular.requirement.commons.dispacher;

import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.commons.wicket.view.util.ActionContext;

/**
 * Allows all access
 */
public class AllowAllPageAccessChecker implements PageAccessChecker, Loggable {
    @Override
    public boolean hasAccess(ActionContext context) {
        return true;
    }
}