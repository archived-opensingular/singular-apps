package org.opensingular.requirement.module.wicket.application;

import org.apache.wicket.Page;
import org.opensingular.requirement.module.wicket.SingleAppPage;
import org.opensingular.requirement.module.wicket.SingularRequirementApplication;

public class WorklistWicketApplication extends SingularRequirementApplication {
    @Override
    public Class<? extends Page> getHomePage() {
        return SingleAppPage.class;
    }
}