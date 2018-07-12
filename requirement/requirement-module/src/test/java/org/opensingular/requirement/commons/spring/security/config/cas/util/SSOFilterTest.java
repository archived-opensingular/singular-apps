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

import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.protocol.http.mock.MockHttpServletRequest;
import org.apache.wicket.protocol.http.mock.MockHttpServletResponse;
import org.apache.wicket.protocol.http.mock.MockHttpSession;
import org.apache.wicket.protocol.http.mock.MockServletContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.verification.Times;
import org.opensingular.requirement.module.config.ServerContext;
import org.opensingular.requirement.module.spring.security.config.cas.util.SSOConfigurableFilter;
import org.opensingular.requirement.module.spring.security.config.cas.util.SSOFilter;
import org.springframework.mock.web.MockFilterConfig;

import javax.servlet.FilterChain;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class SSOFilterTest {

    public  MockFilterConfig        filterConfig;
    private MockHttpServletRequest  request;
    private MockHttpServletResponse response;

    @Before
    public void configureFilterConfig() {
        MockApplication application = new MockApplication();

        MockServletContext context = new MockServletContext(application, "");
        context.setAttribute("nada", ServerContext.WORKLIST);

        filterConfig = new MockFilterConfig(context);
        filterConfig.addInitParameter(SSOFilter.URL_EXCLUDE_PATTERN_PARAM, "/rest");
        filterConfig.addInitParameter(SSOFilter.CLIENT_LOGOUT_URL, "/logout");
        filterConfig.addInitParameter(SSOConfigurableFilter.SINGULAR_CONTEXT_ATTRIBUTE, "nada");

        request = new MockHttpServletRequest(application, new MockHttpSession(context), context){
            @Override
            public String getContextPath() {
                return ServerContext.WORKLIST.getUrlPath();
            }
        };

        response = new MockHttpServletResponse(request);

    }

    @Test
    public void testLogout() throws Exception {
        SSOFilter   filter      = spy(SSOFilter.class);
        FilterChain filterChain = mock(FilterChain.class);
        filter.init(filterConfig);

        request.setURL("/worklist/logout");
        filter.doFilter(request, response, filterChain);

        verify(filterChain, new Times(0)).doFilter(request, response);
    }


    @Test
    public void testExcluded() throws Exception {
        SSOFilter   filter      = spy(SSOFilter.class);
        FilterChain filterChain = mock(FilterChain.class);
        filter.init(filterConfig);

        request.setURL("/worklist/rest");
        filter.doFilter(request, response, filterChain);

        verify(filterChain, new Times(1)).doFilter(request, response);
    }

    @Test
    public void testAnother() throws Exception {
        SSOFilter   filter      = spy(SSOFilter.class);
        FilterChain filterChain = mock(FilterChain.class);
        filter.init(filterConfig);

        request.setURL("/worklist/whatever");
        filter.doFilter(request, response, filterChain);

        verify(filterChain, new Times(0)).doFilter(request, response);
    }
}
