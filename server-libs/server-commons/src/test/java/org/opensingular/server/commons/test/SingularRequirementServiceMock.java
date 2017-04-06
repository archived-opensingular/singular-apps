package org.opensingular.server.commons.test;

import org.opensingular.server.commons.requirement.SingularRequirement;
import org.opensingular.server.commons.service.SingularRequirementService;
import org.opensingular.server.commons.wicket.view.util.ActionContext;

import javax.inject.Named;

@Named
public class SingularRequirementServiceMock implements SingularRequirementService {

    @Override
    public SingularRequirement getSingularRequirement(ActionContext context) {
        return null;
    }
}
