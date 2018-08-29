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

package org.opensingular.requirement.module.spring.security.config.cas.util;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.opensingular.lib.commons.base.SingularProperties;

/**
 * The type Single sing out filter wrapper.
 */
public class SingleSingOutFilterWrapper extends SSOConfigurableFilter {

    /**
     * Constante DELEGATE_CLASS_NAME.
     */
    private static final String DELEGATE_CLASS_NAME = "org.jasig.cas.client.session.SingleSignOutFilter";

    /**
     * Constante CAS_SERVER_URL_PREFIX_PARAM.
     */
    private static final String CAS_SERVER_URL_PREFIX_PARAM = "casServerUrlPrefix";

    /**
     * Campo delegate.
     */
    private Filter delegate = null;
    /**
     * Instantiates a new Single sing out filter wrapper.
     */
    public SingleSingOutFilterWrapper() {
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        final Map<String, String> params = new HashMap<String, String>();
        params.put(CAS_SERVER_URL_PREFIX_PARAM, SingularProperties.getOpt(getSingularContext().getServerPropertyKey(SSOFilter.SSO_URL_PREFIX)).orElse(null));
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
        delegate.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        delegate.doFilter(request, response, chain);
    }

    @Override
    public void destroy() {
        delegate.destroy();
    }
}
