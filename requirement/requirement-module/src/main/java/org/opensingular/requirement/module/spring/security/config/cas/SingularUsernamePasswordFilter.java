package org.opensingular.requirement.module.spring.security.config.cas;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SingularUsernamePasswordFilter extends UsernamePasswordAuthenticationFilter {

    public static final String SINGULAR_USERNAME_KEY = "SINGULAR_USERNAME_KEY";
    public static final String SINGULAR_AUTHENTICATION_MESSAGE_EXCEPTION = "SINGULAR_AUTHENTICATION_MESSAGE_EXCEPTION";

    public SingularUsernamePasswordFilter(String loginPage) {
        setAuthenticationFailureHandler(new SingularAuthenticationFailureHandler(loginPage));
        setRequiresAuthenticationRequestMatcher(new RegexRequestMatcher(loginPage + ".*", "POST"));

    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        String username = super.obtainUsername(request);
        HttpSession session = request.getSession(true);
        session.setAttribute(SINGULAR_USERNAME_KEY, username);
        return username;
    }

    private static class SingularAuthenticationFailureHandler implements AuthenticationFailureHandler {

        private String loginPage;

        SingularAuthenticationFailureHandler(String loginPage) {
            this.loginPage = loginPage;
        }

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
            request.getSession().setAttribute(SINGULAR_AUTHENTICATION_MESSAGE_EXCEPTION, exception.getMessage());
            response.sendRedirect(loginPage);
        }
    }

}
