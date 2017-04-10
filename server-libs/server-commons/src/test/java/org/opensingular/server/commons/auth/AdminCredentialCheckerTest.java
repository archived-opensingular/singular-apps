package org.opensingular.server.commons.auth;

import net.vidageek.mirror.dsl.Mirror;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.opensingular.server.commons.persistence.entity.parameter.ParameterEntity;
import org.opensingular.server.commons.service.ParameterService;

import java.util.Optional;


public class AdminCredentialCheckerTest {

    @Test
    public void testCheckWithNullProcessGroup() {
        AdminCredentialChecker credentialChecker = new DatabaseAdminCredentialChecker(null);
        Assert.assertFalse(credentialChecker.check("FooUser", "BarPass"));
    }

    @Test
    public void testCheckWithProcessGroup() {
        AdminCredentialChecker credentialChecker   = new DatabaseAdminCredentialChecker("FooCategory");
        ParameterService       parameterService    = Mockito.mock(ParameterService.class);
        ParameterEntity        userParameterEntity = new ParameterEntity();
        userParameterEntity.setValue("FooUser");
        Mockito.when(parameterService.findByNameAndProcessGroup(DatabaseAdminCredentialChecker.PARAM_ADMINUSERNAME, "FooCategory")).thenReturn(Optional.of(userParameterEntity));
        ParameterEntity passParameterEntity = new ParameterEntity();
        passParameterEntity.setValue(credentialChecker.getSHA1("BarPass"));
        Mockito.when(parameterService.findByNameAndProcessGroup(DatabaseAdminCredentialChecker.PARAM_PASSHASHADMIN, "FooCategory")).thenReturn(Optional.of(passParameterEntity));
        new Mirror().on(credentialChecker).set().field("parameterService").withValue(parameterService);

        Assert.assertTrue(credentialChecker.check("FooUser", "BarPass"));
    }
}
