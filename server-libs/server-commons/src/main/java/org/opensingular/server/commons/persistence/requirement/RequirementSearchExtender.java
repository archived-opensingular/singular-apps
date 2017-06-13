package org.opensingular.server.commons.persistence.requirement;

import org.opensingular.server.commons.persistence.context.RequirementSearchContext;

public interface RequirementSearchExtender {

    void extend(RequirementSearchContext context);

}
