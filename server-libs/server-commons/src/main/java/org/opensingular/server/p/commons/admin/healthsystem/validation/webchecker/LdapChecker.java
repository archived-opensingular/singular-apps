package org.opensingular.server.p.commons.admin.healthsystem.validation.webchecker;

import org.opensingular.form.type.core.SIString;
import org.opensingular.form.validation.IInstanceValidatable;
import org.opensingular.lib.commons.util.Loggable;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class LdapChecker implements IProtocolChecker, Loggable {

    @Override
    public void protocolCheck(IInstanceValidatable<SIString> validatable) {
        Map<String, String> ldapInfo = new HashMap<>();
        ldapInfo.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        ldapInfo.put(Context.PROVIDER_URL, validatable.getInstance().getValue());
        ldapInfo.put("com.sun.jndi.ldap.read.timeout", "2000");

        DirContext dirContext;
        try {
            dirContext = new InitialDirContext(new Hashtable<>(ldapInfo));
            dirContext.close();tes
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            validatable.error(e.getMessage());
        }
    }
}
