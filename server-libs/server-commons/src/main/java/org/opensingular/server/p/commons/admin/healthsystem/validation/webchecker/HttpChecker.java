package org.opensingular.server.p.commons.admin.healthsystem.validation.webchecker;

import org.opensingular.form.type.core.SIString;
import org.opensingular.form.validation.IInstanceValidatable;
import org.opensingular.lib.commons.util.Loggable;

import java.net.URL;
import java.net.URLConnection;

public class HttpChecker implements IProtocolChecker, Loggable {

    @Override
    public void protocolCheck(IInstanceValidatable<SIString> validatable) {
        URLConnection openConnection;
        try {
            openConnection = new URL(validatable.getInstance().getValue()).openConnection();
            openConnection.setConnectTimeout(2000);
            openConnection.connect();
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            validatable.error(e.getMessage());
        }
    }

}
