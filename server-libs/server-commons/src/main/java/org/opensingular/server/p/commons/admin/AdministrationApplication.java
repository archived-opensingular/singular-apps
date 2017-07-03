package org.opensingular.server.p.commons.admin;

import org.apache.wicket.Page;
import org.opensingular.server.commons.wicket.SingularApplication;
import org.opensingular.server.p.commons.admin.healthsystem.HealthSystemPage;


public class AdministrationApplication extends SingularApplication {


    @Override
    public void init() {
        super.init();
        mountPage(HealthSystemPage.HEALTH_SYSTEM_MOUNT_PATH, HealthSystemPage.class);
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return HealthSystemPage.class;
    }

}