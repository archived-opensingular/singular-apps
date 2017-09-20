package org.opensingular.server.commons.cache;

import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import java.util.Optional;

@WebListener
public class WicketSessionCacheHttpSessionListener implements javax.servlet.http.HttpSessionListener, Loggable {

    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        if (ApplicationContextProvider.isConfigured()){
            try {
                ApplicationContextProvider.get().getBean(WicketSessionCacheManager.class).clearCache();
            } catch (org.springframework.beans.factory.NoSuchBeanDefinitionException e) {
                getLogger().warn(e.getMessage(), e);
            }
        }
    }
}
