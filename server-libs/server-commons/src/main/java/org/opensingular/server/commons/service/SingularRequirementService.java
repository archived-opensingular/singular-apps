package org.opensingular.server.commons.service;

import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.requirement.SingularRequirement;
import org.opensingular.server.commons.wicket.view.util.ActionContext;

public interface SingularRequirementService {

    SingularRequirement getSingularRequirement(ActionContext context);

    static SingularRequirementService get() {
        return ApplicationContextProvider.get().getBean(SingularRequirementService.class);
    }

}