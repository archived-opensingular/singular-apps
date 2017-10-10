package org.opensingular.server.studio.user;

import org.apache.wicket.request.cycle.RequestCycle;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.config.SingularServerConfiguration;
import org.opensingular.server.commons.spring.security.DefaultUserDetails;
import org.opensingular.server.commons.spring.security.SingularPermission;

import java.util.List;

/**
 * Implementation of UserDetails that keep the login through the contexts,
 * also return the ServerContext from request every time that the metod is called
 */
public class SharedUserDetails extends DefaultUserDetails {

    public SharedUserDetails(String username, List<SingularPermission> roles, String displayName) {
        super(username, roles, displayName, null);
    }

    @Override
    public IServerContext getServerContext() {
        RequestCycle requestCycle = RequestCycle.get();
        if (requestCycle != null) {
            return IServerContext.getContextFromRequest(requestCycle.getRequest(), singularServerConfiguration().getContexts());
        }
        return null;
    }

    @Override
    public boolean keepLoginThroughContexts() {
        return true;
    }

    protected SingularServerConfiguration singularServerConfiguration() {
        return ApplicationContextProvider.get().getBean(SingularServerConfiguration.class);
    }
}