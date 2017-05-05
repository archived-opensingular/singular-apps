package org.opensingular.server.commons.util.url;

import org.apache.wicket.request.Url;

public class UrlToolkit {

    private Url url;

    UrlToolkit(Url url) {
        this.url = url;
    }

    public String concatServerAdressWithContext(String context) {
        String contextPrefix = "";
        String fullContext = "";
        if (!context.startsWith("/")) {
            fullContext = "/" + context;
        }
        return url.getProtocol() + "://" + url.getHost() + ":" + url.getPort() + contextPrefix + fullContext;
    }

}
