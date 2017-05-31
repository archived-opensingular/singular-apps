package org.opensingular.server.commons.spring.security.config.cas.util;

import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.protocol.http.mock.MockHttpServletRequest;
import org.apache.wicket.protocol.http.mock.MockHttpServletResponse;
import org.apache.wicket.protocol.http.mock.MockHttpSession;
import org.apache.wicket.protocol.http.mock.MockServletContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.verification.Times;
import org.opensingular.server.commons.config.ServerContext;
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
            public String getServletPath() {
                return ServerContext.WORKLIST.getContextPath();
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
}
