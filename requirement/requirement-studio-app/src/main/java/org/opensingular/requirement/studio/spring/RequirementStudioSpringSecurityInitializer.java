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

package org.opensingular.requirement.studio.spring;

import java.util.Collections;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.opensingular.lib.support.spring.util.AutoScanDisabled;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.config.SpringSecurityInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

public class RequirementStudioSpringSecurityInitializer extends SpringSecurityInitializer {

    @Override
    protected <T extends WebSecurityConfigurerAdapter> Class<T> getSpringSecurityConfigClass(IServerContext context) {
        Class<T> securityConfig = super.getSpringSecurityConfigClass(context);
        return Optional.ofNullable(securityConfig).orElse(((Class<T>) StudioSecurity.class));
    }

    @AutoScanDisabled
    @Configuration
    @EnableWebMvc
    @Order(106)
    public static class StudioSecurity extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.requiresChannel()
                    .anyRequest()
                    .requiresSecure()
                    .and()
                    .headers()
                    .frameOptions()
                    .sameOrigin()
                    .and()
                    .csrf().disable()
                    .authorizeRequests()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .formLogin().permitAll().loginPage("/login")
                    .and()
                    .logout()
                    .logoutRequestMatcher(new RegexRequestMatcher("/.*logout\\?{0,1}.*",  HttpMethod.GET.name()))
                    .logoutSuccessUrl("/");


        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(new AbstractUserDetailsAuthenticationProvider() {
                @Override
                protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

                }

                @Override
                protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
                    if (StringUtils.isNotBlank(username)) {
                        return new RequirementStudioUserDetails(username, Collections.emptyList(), username);
                    }
                    throw new BadCredentialsException("Não foi possivel autenticar o usuario informado");
                }
            });
        }

    }


}
