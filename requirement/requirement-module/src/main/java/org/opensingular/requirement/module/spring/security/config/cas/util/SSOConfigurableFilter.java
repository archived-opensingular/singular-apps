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

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.opensingular.requirement.module.config.IServerContext;

/**
 * The type Sso configurable filter.
 */
public abstract class SSOConfigurableFilter implements Filter {

    public static final String SINGULAR_CONTEXT_ATTRIBUTE      = "SSOFilterSingularContextAttribute";
    private IServerContext serverContext;

    protected IServerContext getSingularContext(){
        return serverContext;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.serverContext = (IServerContext) filterConfig.getServletContext().getAttribute(filterConfig.getInitParameter(SINGULAR_CONTEXT_ATTRIBUTE));
    }

    /**
     * Instantiates a new Sso configurable filter.
     */
    public SSOConfigurableFilter() {
    }
}
