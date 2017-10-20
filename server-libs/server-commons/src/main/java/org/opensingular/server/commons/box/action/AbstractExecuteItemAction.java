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

package org.opensingular.server.commons.box.action;

import org.opensingular.lib.commons.ui.Icon;
import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.flow.controllers.IController;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.service.dto.ItemActionConfirmation;
import org.opensingular.server.commons.service.dto.ItemActionType;

import static org.opensingular.server.commons.RESTPaths.EXECUTE;
import static org.opensingular.server.commons.RESTPaths.PATH_BOX_ACTION;

public abstract class AbstractExecuteItemAction extends BoxItemAction {


    public AbstractExecuteItemAction(String name, String label, Icon icon, Class<? extends IController> controller, ItemActionConfirmation confirmation, BoxItemData line) {
        super(name, label, icon, ItemActionType.EXECUTE, getEndpointExecute(line), controller, confirmation);
    }

    public AbstractExecuteItemAction(String name, String label, Icon icon, Class<? extends IController> controller, BoxItemData line) {
        super(name, label, icon, ItemActionType.EXECUTE, getEndpointExecute(line), controller, null);
    }

    protected static String getEndpointExecute(BoxItemData line) {
        return PATH_BOX_ACTION + EXECUTE + "?id=" + line.getPetitionId();

    }


}
