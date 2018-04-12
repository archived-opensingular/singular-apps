/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.requirement.module.admin.auth;

import net.vidageek.mirror.dsl.Mirror;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.opensingular.requirement.commons.auth.AdminCredentialChecker;
import org.opensingular.requirement.commons.auth.AdministrationAuthenticationProvider;
import org.opensingular.requirement.commons.config.ServerContext;
import org.opensingular.requirement.commons.persistence.entity.parameter.ParameterEntity;
import org.opensingular.requirement.commons.service.ParameterService;
import org.opensingular.requirement.module.SingularModule;
import org.opensingular.requirement.module.SingularModuleConfiguration;
import org.opensingular.requirement.module.admin.auth.DatabaseAdminCredentialChecker;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class AdministrationAuthenticationProviderTest {

    private AdministrationAuthenticationProvider administrationAuthenticationProvider;

    @Mock
    SingularModuleConfiguration moduleConfiguration;

    @Mock
    SingularModule module;

    @Mock
    ParameterService       parameterService;

    @InjectMocks
    AdminCredentialChecker credentialChecker = new DatabaseAdminCredentialChecker();

    @Before
    public void setUp() {
        when(module.abbreviation()).thenReturn("FooCategory");
        when(moduleConfiguration.getModule()).thenReturn(module);
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