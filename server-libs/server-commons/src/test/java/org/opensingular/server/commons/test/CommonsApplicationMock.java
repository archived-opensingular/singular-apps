package org.opensingular.server.commons.test;


import org.apache.wicket.Page;
import org.opensingular.server.commons.wicket.SingularApplication;

import javax.inject.Named;

@Named
public class CommonsApplicationMock extends SingularApplication {

    @Override
    public void init() {
        super.init();
        getRequestCycleListeners().add(new SingularTestRequestCycleListener());
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return null;
    }
}