package org.opensingular.server.core.test;


import org.apache.wicket.Page;
import org.opensingular.server.commons.test.SingularTestRequestCycleListener;
import org.opensingular.server.commons.wicket.SingularApplication;
import org.opensingular.server.core.wicket.box.BoxPage;

import javax.inject.Named;

@Named
public class ServerApplicationMock extends SingularApplication {

    @Override
    public void init() {
        super.init();
        getRequestCycleListeners().add(new SingularTestRequestCycleListener());
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return BoxPage.class;
    }
}