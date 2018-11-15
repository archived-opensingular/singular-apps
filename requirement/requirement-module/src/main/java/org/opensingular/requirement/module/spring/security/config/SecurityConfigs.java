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

package org.opensingular.requirement.module.spring.security.config;


import org.apache.commons.lang3.StringUtils;
import org.opensingular.lib.support.spring.util.AutoScanDisabled;
import org.opensingular.requirement.module.auth.AdminCredentialChecker;
import org.opensingular.requirement.module.auth.AdministrationAuthenticationProvider;
import org.opensingular.requirement.module.spring.security.AbstractSingularSpringSecurityAdapter;
import org.opensingular.requirement.module.spring.security.DefaultUserDetails;
import org.opensingular.requirement.module.spring.security.config.cas.SingularCASSpringSecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Optional;

public interface SecurityConfigs {

    @Order(103)
    @Configuration
    @AutoScanDisabled
    class CASPeticionamento extends SingularCASSpringSecurityConfig {
    }

    @Order(104)
    @Configuration
    @AutoScanDisabled
    @EnableWebSecurity
    class CASAnalise extends SingularCASSpringSecurityConfig {
    }

    @Order(105)
    @Configuration
    @AutoScanDisabled
    @EnableWebSecurity
    class AdministrationSecurity extends AbstractSingularSpringSecurityAdapter {
        @Inject
        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        private Optional<AdminCredentialChecker> credentialChecker;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .regexMatcher(getContext().getSettings().getPathRegex())
                    .authorizeRequests()
                    .regexMatchers(getContext().getSettings().getUrlPath() + "/login.*")
                    .permitAll()
                    .antMatchers(getContext().getSettings().getContextPath()).hasRole("ADMIN")
                    .and()
                    .exceptionHandling().accessDeniedPage("/public/error/403")
                    .and()
                    .csrf().disable()
                    .formLogin().permitAll().loginPage(getContext().getSettings().getUrlPath() + "/login")
                    .and()
                    .logout()
                    .logoutRequestMatcher(new RegexRequestMatcher("/.*logout\\?{0,1}.*", HttpMethod.GET.name()))
                    .logoutSuccessUrl("/");
        }


        @Override
        protected void configure(AuthenticationManagerBuilder auth) {
            credentialChecker.ifPresent(cc ->
                    auth.authenticationProvider(new AdministrationAuthenticationProvider(cc, getContext())));
        }
    }

    @Order(106)
    @Configuration
    @AutoScanDisabled
    @EnableWebSecurity
    class RequirementSecurity extends AllowAllSecurity {

    }

    @Order(107)
    @Configuration
    @AutoScanDisabled
    @EnableWebSecurity
    class WorklistSecurity extends AllowAllSecurity {

    }

    abstract class AllowAllSecurity extends AbstractSingularSpringSecurityAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.regexMatcher(getContext().getSettings().getPathRegex())
                    .requiresChannel()
                    .anyRequest()
                    .requiresSecure()
                    .and()
                    .headers()
                    .frameOptions()
                    .sameOrigin()
                    .and()
                    .csrf().disable()
                    .authorizeRequests()
                    .regexMatchers(getContext().getSettings().getUrlPath() + getLoginPagePath() + ".*")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .formLogin().loginPage(getContext().getSettings().getUrlPath() + getLoginPagePath())
                    .and()
                    .logout()
                    .logoutRequestMatcher(new RegexRequestMatcher("/.*logout\\?{0,1}.*", HttpMethod.GET.name()))
                    .logoutSuccessUrl(getLogoutSuccessUrl());

        }

        protected String getLogoutSuccessUrl() {
            return "/";
        }

        protected String getLoginPagePath() {
            return "/login";
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) {
            auth.authenticationProvider(getAuthenticationProvider());
        }

        protected AbstractUserDetailsAuthenticationProvider getAuthenticationProvider() {
            return new AbstractUserDetailsAuthenticationProvider() {
                @Override
                protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
                }

                @Override
                protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
                    if (StringUtils.isNotBlank(username)) {
                        return new DefaultUserDetails(username, username, Collections.emptyList(), Collections.singletonList(getContext().getClass()));
                    }
                    throw new BadCredentialsException("Não foi possivel autenticar o usuario informado");
                }
            };
        }
    }

}