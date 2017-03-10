package org.opensingular.server.commons.spring.security.config.cas.util;

import org.apache.wicket.request.Url;

public class UrlToolkit {

    private Url url;

    public UrlToolkit(Url url) {
        this.url = url;
    }

    public String concatServerAdressWithContext(String context) {
        String contextPrefix = "";
        if (!context.startsWith("/")) {
            context = "/";
        }
        return url.getProtocol() + "://" + url.getHost() + ":" + url.getPort() + contextPrefix + context;
    }

}
