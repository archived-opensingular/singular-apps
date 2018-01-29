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
package org.opensingular.app.commons.mail.service.email;

import org.opensingular.lib.commons.util.Loggable;

public interface IMailSenderREST extends Loggable {

    public static final String PATH = "/rest/mail";
    public static final String PATH_SEND_ALL = "/sendAll";
    
    /**
     * Aciona o job para envio dos emails psersistidos em banco
     */
    boolean sendAll();
}
