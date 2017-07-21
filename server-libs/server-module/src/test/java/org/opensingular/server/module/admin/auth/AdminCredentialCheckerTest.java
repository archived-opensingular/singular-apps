package org.opensingular.server.module.admin.auth;

import net.vidageek.mirror.dsl.Mirror;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.opensingular.server.commons.auth.AdminCredentialChecker;
import org.opensingular.server.commons.persistence.entity.parameter.ParameterEntity;
import org.opensingular.server.commons.service.ParameterService;
import org.opensingular.server.module.SingularModule;
import org.opensingular.server.module.SingularModuleConfiguration;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class AdminCredentialCheckerTest {

    @Mock
    SingularModuleConfiguration moduleConfiguration;

    @Mock
    SingularModule module;

    @Mock
    ParameterService parameterService;

    @InjectMocks
    AdminCredentialChecker credentialChecker = new DatabaseAdminCredentialChecker();

    @Before
    public void setup(){
        when(moduleConfiguration.getModule()).thenReturn(module);
    }

    @Test
    public void testCheckWithNullModule() {
        when(module.abbreviation()).thenReturn(null);
        Assert.assertFalse(credentialChecker.check("FooUser", "BarPass"));
    }

    @Test
    public void testCheckWithModule() {
        when(module.abbreviation()).thenReturn("FooCategory");

        ParameterEntity  userParameterEntity = new ParameterEntity();
        userParameterEntity.setValue("FooUser");
        Mockito.when(parameterService.findByNameAndModule(DatabaseAdminCredentialChecker.PARAM_ADMINUSERNAME, "FooCategory")).thenReturn(Optional.of(userParameterEntity));
        ParameterEntity passParameterEntity = new ParameterEntity();
        passParameterEntity.setValue(credentialChecker.getSHA1("BarPass"));
        Mockito.when(parameterService.findByNameAndModule(DatabaseAdminCredentialChecker.PARAM_PASSHASHADMIN, "FooCategory")).thenReturn(Optional.of(passParameterEntity));
        new Mirror().on(credentialChecker).set().field("parameterService").withValue(parameterService);

        Assert.assertTrue(credentialChecker.check("FooUser", "BarPass"));
    }
}
