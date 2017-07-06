package org.opensingular.server.commons.auth;

import java.util.Optional;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.opensingular.server.commons.config.ServerContext;
import org.opensingular.server.commons.persistence.entity.parameter.ParameterEntity;
import org.opensingular.server.commons.service.ParameterService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import net.vidageek.mirror.dsl.Mirror;

import static org.junit.Assert.*;

public class AdministrationAuthenticationProviderTest {

    private AdministrationAuthenticationProvider administrationAuthenticationProvider;

    @Before
    public void setUp() {
        AdminCredentialChecker credentialChecker = new DatabaseAdminCredentialChecker("FooCategory");
        ParameterService       parameterService  = Mockito.mock(ParameterService.class);
        ParameterEntity        userParameterEntity = new ParameterEntity();
        userParameterEntity.setValue("USER");
        Mockito.when(parameterService.findByNameAndModule(DatabaseAdminCredentialChecker.PARAM_ADMINUSERNAME, "FooCategory")).thenReturn(Optional.of(userParameterEntity));
        ParameterEntity passParameterEntity = new ParameterEntity();
        passParameterEntity.setValue(credentialChecker.getSHA1("123456"));
        Mockito.when(parameterService.findByNameAndModule(DatabaseAdminCredentialChecker.PARAM_PASSHASHADMIN, "FooCategory")).thenReturn(Optional.of(passParameterEntity));
        new Mirror().on(credentialChecker).set().field("parameterService").withValue(parameterService);

        administrationAuthenticationProvider = new AdministrationAuthenticationProvider(credentialChecker, ServerContext.WORKLIST);
    }

    @Test
    public void additionalAuthenticationChecks() throws Exception {
        administrationAuthenticationProvider.additionalAuthenticationChecks(null, null);
    }

    @Test
    public void retrieveUser() throws Exception {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("USER", "123456");
        UserDetails                         user  = administrationAuthenticationProvider.retrieveUser("USER", token);

        Assert.assertEquals("Usuário retornado é incorreto.", "USER", user.getUsername());
    }

    @Test(expected = BadCredentialsException.class)
    public void badUser() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("USER2", "123456");
        administrationAuthenticationProvider.retrieveUser("USER2", token);
    }

    @Test(expected = BadCredentialsException.class)
    public void badPassword() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("USER", "654321");
        administrationAuthenticationProvider.retrieveUser("USER", token);
    }

    @Test(expected = BadCredentialsException.class)
    public void badUserAndPassword() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("USER2", "654321");
        administrationAuthenticationProvider.retrieveUser("USER2", token);
    }

}