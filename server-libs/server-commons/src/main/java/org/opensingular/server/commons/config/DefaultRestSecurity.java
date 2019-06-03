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

package org.opensingular.server.commons.config;

import org.apache.commons.lang3.StringUtils;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.support.spring.util.AutoScanDisabled;
import org.opensingular.server.commons.exception.SingularServerException;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@AutoScanDisabled
public class DefaultRestSecurity extends WebSecurityConfigurerAdapter {
    public static final String REST_ANT_PATTERN = "/rest/**";

    public static final String SINGULAR_MODULE_USERNAME = "singular.module.username";
    public static final String SINGULAR_MODULE_PASSWORD = "singular.module.password";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher(REST_ANT_PATTERN)
                .csrf().disable()
                .authorizeRequests()
                .anyRequest()
                .hasRole("REST")
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        final String u = SingularProperties.get().getProperty(SINGULAR_MODULE_USERNAME);
        final String p = SingularProperties.get().getProperty(SINGULAR_MODULE_PASSWORD);
        if (StringUtils.isAnyBlank(u, p)) {
            throw new SingularServerException("Não foi definido a senha ou o password da segurança rest");
        }
        auth.inMemoryAuthentication()
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser(u)
                .password(p)
                .roles("USER", "REST");
    }
}