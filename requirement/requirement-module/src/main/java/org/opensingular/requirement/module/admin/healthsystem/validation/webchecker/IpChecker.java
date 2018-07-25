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

package org.opensingular.requirement.module.admin.healthsystem.validation.webchecker;

import java.net.InetAddress;

import org.opensingular.form.type.core.SIString;
import org.opensingular.form.validation.InstanceValidatable;
import org.opensingular.lib.commons.util.Loggable;

public class IpChecker implements IProtocolChecker, Loggable {

    @Override
    public void protocolCheck(InstanceValidatable<SIString> validatable) {
        String   url       = validatable.getInstance().getValue().replace("ip://", "");
        String[] piecesUrl = url.split(":");

        try {
            if (!InetAddress.getByName(piecesUrl[0]).isReachable(2000)) {
                validatable.error("Address not reacheable!");
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            validatable.error(e.getMessage());
        }
    }
}
