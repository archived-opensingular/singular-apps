package org.opensingular.requirement.module;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Configura o Timeout da Sessão
 */
public class SessionTimeoutHttpSessionListener implements HttpSessionListener {
    private final int sessionTimeoutMinutes;

    public SessionTimeoutHttpSessionListener(int sessionTimeoutMinutes) {
        this.sessionTimeoutMinutes = sessionTimeoutMinutes;
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setMaxInactiveInterval(60 * sessionTimeoutMinutes);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

    }
}