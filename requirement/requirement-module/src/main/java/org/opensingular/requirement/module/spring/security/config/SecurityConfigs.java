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

package org.opensingular.requirement.module.spring.security.config;


import java.util.Optional;
import javax.inject.Inject;

import org.opensingular.lib.support.spring.util.AutoScanDisabled;
import org.opensingular.requirement.module.auth.AdminCredentialChecker;
import org.opensingular.requirement.module.auth.AdministrationAuthenticationProvider;
import org.opensingular.requirement.module.config.DefaultContexts;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.spring.security.AbstractSingularSpringSecurityAdapter;
import org.opensingular.requirement.module.spring.security.AbstractSingularSpringSecurityAdapter;
import org.opensingular.requirement.module.spring.security.config.cas.SingularAdministrationLogoutHandler;
import org.opensingular.requirement.module.spring.security.config.cas.SingularCASSpringSecurityConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

public interface SecurityConfigs {

    @Order(103)
    @Configuration
    @AutoScanDisabled
    class CASPeticionamento extends SingularCASSpringSecurityConfig {
        @Override
        protected IServerContext getContext() {
            return singularServerConfiguration.findContextByName(DefaultContexts.RequirementContext.NAME);
        }

        @Override
        public String getCASLogoutURL() {
            return "";
        }
    }

    @Order(104)
    @Configuration
    @AutoScanDisabled
    class CASAnalise extends SingularCASSpringSecurityConfig {
        @Override
        protected IServerContext getContext() {
            return singularServerConfiguration.findContextByName(DefaultContexts.WorklistContext.NAME);
        }

        @Override
        public String getCASLogoutURL() {
            return "";
        }
    }

    @Order(105)
    @Configuration
    @AutoScanDisabled
    class AdministrationSecurity extends AbstractSingularSpringSecurityAdapter {
        @Inject
        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        private Optional<AdminCredentialChecker> credentialChecker;

        protected IServerContext getContext() {
            return singularServerConfiguration.findContextByName(DefaultContexts.AdministrationContext.NAME);
        }

        @Bean
        public SingularLogoutHandler singularLogoutHandler() {
            return new SingularAdministrationLogoutHandler();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .regexMatcher(getContext().getPathRegex())
                    .authorizeRequests()
                    .antMatchers(ServerContext.ADMINISTRATION.getContextPath()).hasRole("ADMIN")
                    .and()
                    .exceptionHandling().accessDeniedPage("/public/error/403")
                    .and()
                    .csrf().disable()
                    .httpBasic();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) {
            credentialChecker.ifPresent(cc ->
                    auth.authenticationProvider(new AdministrationAuthenticationProvider(cc, getContext())));
        }
    }

}