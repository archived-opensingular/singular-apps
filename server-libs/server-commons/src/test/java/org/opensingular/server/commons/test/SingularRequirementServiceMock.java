package org.opensingular.server.commons.test;

import java.util.Optional;

import javax.inject.Named;

import org.opensingular.form.SType;
import org.opensingular.server.commons.flow.FlowResolver;
import org.opensingular.server.commons.requirement.SingularRequirement;
import org.opensingular.server.commons.service.SingularRequirementService;
import org.opensingular.server.commons.wicket.view.util.ActionContext;

@Named
public class SingularRequirementServiceMock implements SingularRequirementService {

    @Override
    public SingularRequirement getSingularRequirement(ActionContext context) {
        return new SingularRequirement() {
            @Override
            public String getName() {
                return "FooRequirement";
            }

            @Override
            public Class<? extends SType> getMainForm() {
                return null;
            }

            @Override
            public FlowResolver getFlowResolver() {
                return (FlowResolver) (cfg, iRoot) -> Optional.of(FOOFlow.class);
            }
        };
    }
}
