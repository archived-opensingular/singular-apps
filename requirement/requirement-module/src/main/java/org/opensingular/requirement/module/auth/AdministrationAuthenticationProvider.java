/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.requirement.module.auth;

import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.spring.security.DefaultUserDetails;
import org.opensingular.requirement.module.spring.security.SingularPermission;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

public class AdministrationAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final AdminCredentialChecker credentialChecker;
    private final IServerContext serverContext;

    public AdministrationAuthenticationProvider(AdminCredentialChecker credentialChecker,
                                                IServerContext serverContext) {
        this.credentialChecker = credentialChecker;
        this.serverContext = serverContext;
    }

    @Override
    public void additionalAuthenticationChecks(UserDetails userDetails,
                                               UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {

    }

    @Override
    public UserDetails retrieveUser(String principal, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        if (credentialChecker.check(principal, authentication.getCredentials().toString())) {
            return new DefaultUserDetails(principal, null,
                    Collections.singletonList(new SingularPermission("ADMIN", null)),
                    Collections.singletonList(serverContext.getClass()));
        }
        throw new BadCredentialsException("Não foi possivel autenticar o usuario informado");
    }

}