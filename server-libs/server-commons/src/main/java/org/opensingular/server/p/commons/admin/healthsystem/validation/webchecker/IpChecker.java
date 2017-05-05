package org.opensingular.server.p.commons.admin.healthsystem.validation.webchecker;

import org.opensingular.form.type.core.SIString;
import org.opensingular.form.validation.IInstanceValidatable;
import org.opensingular.lib.commons.util.Loggable;

import java.net.InetAddress;

public class IpChecker implements IProtocolChecker, Loggable {

    @Override
    public void protocolCheck(IInstanceValidatable<SIString> validatable) {
        String   url       = validatable.getInstance().getValue().replace("ip://", "");
        String[] piecesUrl = url.split(":");

        try {
            if (!InetAddress.getByName(piecesUrl[0]).isReachable(2000)) {
                throw new Exception("Address not reacheble!");
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            validatable.error(e.getMessage());
        }
    }
}
