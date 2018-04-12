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

package org.opensingular.requirement.commons.spring.security.config.cas.util;


import org.opensingular.lib.commons.base.SingularProperties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * The type Authentication filter wrapper.
 */
public class AuthenticationFilterWrapper extends SSOConfigurableFilter {

    /**
     * Constante DELEGATE_CLASS_NAME.
     */
    private static final String DELEGATE_CLASS_NAME = "org.jasig.cas.client.authentication.AuthenticationFilter";

    /**
     * Constante CAS_SERVER_LOGIN_URL_PARAM.
     */
    private static final String CAS_SERVER_LOGIN_URL_PARAM = "casServerLoginUrl";

    /**
     * Constante SERVER_NAME_PARAM.
     */
    private static final String SERVER_NAME_PARAM = "serverName";

    /**
     * Constante SERVICE_PARAM.
     */
    private static final String SERVICE_PARAM = "service";

    /**
     * The constant SERVICE_URL_PARAM.
     */
    public static final String SERVICE_URL_PARAM = "serviceUrl";

    /**
     * Campo delegate.
     */
    private Filter delegate = null;

    /**
     * Instantiates a new Authentication filter wrapper.
     */
    public AuthenticationFilterWrapper() {
    }


    /**
     * Inicializa.
     *
     * @param filterConfig um filter config
     * @throws ServletException uma exceção servlet exception
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        final Map<String, String> params = new HashMap<>();
        params.put(CAS_SERVER_LOGIN_URL_PARAM, SingularProperties.getOpt(getSingularContext().getServerPropertyKey(SSOFilter.SSO_LOGIN)).orElse(null));
        Optional<String> serviceUrl = resolveServiceUrl(filterConfig);
        if (serviceUrl.isPresent()) {
            params.put(SERVICE_PARAM, serviceUrl.get());
        } else {
            params.put(SERVER_NAME_PARAM, SingularProperties.getOpt(getSingularContext().getServerPropertyKey(SSOFilter.SSO_CLIENT_SERVER)).orElse(null));
        }

        Enumeration enumeration = filterConfig.getInitParameterNames();
        while (enumeration.hasMoreElements()) {
            String s = (String) enumeration.nextElement();
            params.put(s, filterConfig.getInitParameter(s));
        }


        try {
            FilterConfig newConfig = new FilterConfig() {

                @Override
                public ServletContext getServletContext() {
                    return filterConfig.getServletContext();
                }

                @Override
                public Enumeration<String> getInitParameterNames() {
                    return Collections.enumeration(params.keySet());
                }

                @Override
                public String getInitParameter(String name) {
                    return params.get(name);
                }

                @Override
                public String getFilterName() {
                    return filterConfig.getFilterName();
                }
            };
            delegate = (Filter) Class.forName(DELEGATE_CLASS_NAME).newInstance();
            delegate.init(newConfig);
        } catch (Exception e) {
            throw new ServletException(e.getMessage(), e);
        }
    }

    /**
     * Do filter.
     *
     * @param request  um request
     * @param response um response
     * @param chain    um chain
     * @throws IOException      Métodos subjeito a erros de entrada e saída.
     * @throws ServletException uma exceção servlet exception
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        delegate.doFilter(request, response, chain);
    }

    /**
     * Destroy.
     */
    @Override
    public void destroy() {
        delegate.destroy();
    }

    /**
     * Obtém uma referência de service url.
     *
     * @param filterConfig         filterConfig
     * @return uma referência de service url
     */
    public Optional<String> resolveServiceUrl(FilterConfig filterConfig) {
        String filterServiceURL = filterConfig.getInitParameter(SERVICE_URL_PARAM);
        if (filterServiceURL == null){
            return SingularProperties.getOpt(getSingularContext().getServerPropertyKey(SSOFilter.SERVICE_URL_PARAM));
        }
        return Optional.ofNullable(filterServiceURL);
    }
}
