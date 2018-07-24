package org.opensingular.requirement.module.wicket.application;

import org.apache.wicket.Page;
import org.opensingular.lib.commons.lambda.IConsumer;
import org.opensingular.lib.wicket.util.application.SkinnableApplication;
import org.opensingular.lib.wicket.util.template.SkinOptions;
import org.opensingular.requirement.module.wicket.SingleAppPage;
import org.opensingular.requirement.module.wicket.SingularRequirementApplication;

public class WorklistWicketApplication extends SingularRequirementApplication {
    @Override
    public Class<? extends Page> getHomePage() {
        return SingleAppPage.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initSkins(SkinOptions skinOptions) {
        IConsumer<SkinOptions> initSKin = (IConsumer<SkinOptions>) this.getServletContext().getAttribute(SkinnableApplication.INITSKIN_CONSUMER_PARAM);
        initSKin.accept(skinOptions);
    }
}