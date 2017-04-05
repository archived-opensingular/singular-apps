package org.opensingular.server.commons.test;


import org.apache.wicket.Page;
import org.opensingular.server.commons.wicket.SingularApplication;

import javax.inject.Named;

@Named
public class SingularApplicationMock extends SingularApplication {

    @Override
    public Class<? extends Page> getHomePage() {
        return null;
    }
}