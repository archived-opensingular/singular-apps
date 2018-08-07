/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.requirement.module.box.action.defaults;

import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.requirement.module.box.BoxItemData;
import org.opensingular.requirement.module.box.action.AbstractExecuteItemAction;
import org.opensingular.requirement.module.flow.controllers.DefaultAssignController;
import org.opensingular.requirement.module.service.dto.ItemActionConfirmation;

import static org.opensingular.requirement.module.RESTPaths.USERS;

public class RelocateAction extends AbstractExecuteItemAction {


    private static final ItemActionConfirmation CONFIRMATION_RELOCATE = new ItemActionConfirmation("Realocar", "Usuário:", "Cancelar", "Realocar", USERS);


    public RelocateAction(BoxItemData line) {
        super("relocate", "Realocar", DefaultIcons.SHARE_SQUARE, DefaultAssignController.class, CONFIRMATION_RELOCATE, line);
    }
}
