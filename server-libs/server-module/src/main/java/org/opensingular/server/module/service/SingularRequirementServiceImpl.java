package org.opensingular.server.module.service;

import org.opensingular.server.commons.service.SingularRequirementService;
import org.opensingular.server.commons.wicket.view.util.ActionContext;
import org.opensingular.server.module.SingularModuleConfiguration;
import org.opensingular.server.commons.requirement.SingularRequirement;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class SingularRequirementServiceImpl implements SingularRequirementService {

    @Inject
    private SingularModuleConfiguration moduleConfiguration;

    @Override
    public SingularRequirement getSingularRequirement(ActionContext context) {
        return moduleConfiguration.getRequirementById(context.getRequirementId().orElse(null));
    }

}