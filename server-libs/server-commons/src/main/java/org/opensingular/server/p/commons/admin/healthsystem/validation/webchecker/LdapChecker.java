/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.server.p.commons.admin.healthsystem.validation.webchecker;

import org.opensingular.form.type.core.SIString;
import org.opensingular.form.validation.InstanceValidatable;
import org.opensingular.lib.commons.util.Loggable;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class LdapChecker implements IProtocolChecker, Loggable {

    @Override
    public void protocolCheck(InstanceValidatable<SIString> validatable) {
        Map<String, String> ldapInfo = new HashMap<>();
        ldapInfo.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        ldapInfo.put(Context.PROVIDER_URL, validatable.getInstance().getValue());
        ldapInfo.put("com.sun.jndi.ldap.read.timeout", "2000");

        DirContext dirContext;
        try {
            dirContext = new InitialDirContext(new Hashtable<>(ldapInfo));
            dirContext.close();
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            validatable.error(e.getMessage());
        }
    }
}
