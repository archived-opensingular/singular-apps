package org.opensingular.server.commons.spring.security;

import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.util.url.UrlToolkitBuilder;
import org.opensingular.server.commons.wicket.SingularServerApplication;
import org.opensingular.server.commons.wicket.SingularSession;

import javax.servlet.ServletContext;
import java.io.Serializable;

public class SecurityAuthPathsFactory implements Serializable {

    public SecurityAuthPaths get() {

        SingularSession singularSession = SingularSession.get();
        SingularServerApplication singularServerApplication = SingularServerApplication.get();

        SingularUserDetails userDetails = singularSession.getUserDetails();
        IServerContext serverContext = userDetails.getServerContext();

        ServletContext servletContext = singularServerApplication.getServletContext();

        UrlToolkitBuilder urlToolkitBuilder = new UrlToolkitBuilder();

        return new SecurityAuthPaths(servletContext.getContextPath(), serverContext.getUrlPath(), urlToolkitBuilder);
    }

}
