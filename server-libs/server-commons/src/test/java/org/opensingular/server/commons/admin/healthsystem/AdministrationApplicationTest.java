package org.opensingular.server.commons.admin.healthsystem;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.server.p.commons.admin.AdministrationApplication;
import org.opensingular.server.p.commons.admin.healthsystem.HealthSystemPage;

public class AdministrationApplicationTest {
    @Test
    public void getHomePageTest(){
        Assert.assertEquals(HealthSystemPage.class, new AdministrationApplication().getHomePage());
    }
}
