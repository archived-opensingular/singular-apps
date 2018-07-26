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

package org.opensingular.requirement.module.spring.security.config.cas;


import com.google.common.base.Joiner;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.support.spring.util.AutoScanDisabled;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.opensingular.requirement.module.spring.security.AbstractSingularSpringSecurityAdapter;
import org.opensingular.requirement.module.spring.security.SingularUserDetailsService;
import org.opensingular.requirement.module.spring.security.config.SingularLogoutHandler;
import org.opensingular.requirement.module.spring.security.config.cas.util.SSOConfigurableFilter;
import org.opensingular.requirement.module.spring.security.config.cas.util.SSOFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.j2ee.J2eePreAuthenticatedProcessingFilter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Configuration
@AutoScanDisabled
public abstract class SingularCASSpringSecurityConfig extends AbstractSingularSpringSecurityAdapter {

    @Inject
    @Named("peticionamentoUserDetailService")
    protected Optional<SingularUserDetailsService> peticionamentoUserDetailService;

    @Inject
    private ServletContext servletContext;

    @Bean
    public SingularLogoutHandler singularLogoutHandler() {
        return new SingularCASLogoutHandler(getCASLogoutURL());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        if (SingularProperties.get().isTrue(SingularProperties.SINGULAR_DEV_MODE)) {
//            web.debug(true);
        }
        super.configure(web);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        PreAuthenticatedAuthenticationProvider casAuthenticationProvider = new PreAuthenticatedAuthenticationProvider();
        casAuthenticationProvider.setPreAuthenticatedUserDetailsService(
                new UserDetailsByNameServiceWrapper<>(peticionamentoUserDetailService.orElseThrow(() ->
                        SingularServerException.rethrow(
                                String.format("Bean %s do tipo %s não pode ser nulo. Para utilizar a configuração de segurança %s é preciso declarar um bean do tipo %s identificado pelo nome %s .",
                                        UserDetailsService.class.getName(),
                                        "peticionamentoUserDetailService",
                                        SingularCASSpringSecurityConfig.class.getName(),
                                        UserDetailsService.class.getName(),
                                        "peticionamentoUserDetailService"
                                ))
                )
                )
        );

        ProviderManager authenticationManager = new ProviderManager(Arrays.asList(new AuthenticationProvider[]{casAuthenticationProvider}));

        J2eePreAuthenticatedProcessingFilter j2eeFilter = new J2eePreAuthenticatedProcessingFilter();
        j2eeFilter.setAuthenticationManager(authenticationManager);

        http
                .addFilterBefore(newSSOFilter(), J2eePreAuthenticatedProcessingFilter.class)
                .regexMatcher(getContext().getPathRegex())
                .exceptionHandling().authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/login"))
                .and()
                .csrf().disable()
                .headers().frameOptions().sameOrigin()
                .and()
                .jee().j2eePreAuthenticatedProcessingFilter(j2eeFilter)
                .and()
                .authorizeRequests()
                .antMatchers(getContext().getContextPath()).authenticated();



    }

    protected SSOFilter newSSOFilter() throws ServletException {
        IServerContext context = getContext();
        SSOFilter ssoFilter = new SSOFilter();
        String filterName = "SSOFilter" + context.getName();
        servletContext.setAttribute(filterName, context);

        Map<String, String> map = new HashMap<>();
        map.put(SSOConfigurableFilter.SINGULAR_CONTEXT_ATTRIBUTE, filterName);
        map.put("logoutUrl", context.getUrlPath() + "/logout");
        map.put("urlExcludePattern", getExcludeUrlRegex());

        ssoFilter.init(new FilterConfig() {
            @Override
            public String getFilterName() {
                return filterName;
            }

            @Override
            public ServletContext getServletContext() {
                return servletContext;
            }

            @Override
            public String getInitParameter(String name) {
                return map.get(name);
            }

            @Override
            public Enumeration<String> getInitParameterNames() {
                return Collections.enumeration(map.keySet());
            }
        });
        return ssoFilter;
    }

    public abstract String getCASLogoutURL();

    protected final String getExcludeUrlRegex() {
        return Joiner.on(",").join(singularServerConfiguration.getPublicUrls()).replaceAll("\\*", ".*");
    }

}
