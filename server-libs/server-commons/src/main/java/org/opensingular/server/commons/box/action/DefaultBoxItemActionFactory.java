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

package org.opensingular.server.commons.box.action;


import org.opensingular.lib.wicket.util.resource.Icone;
import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.flow.controllers.DefaultAssignController;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.service.dto.ItemActionConfirmation;
import org.opensingular.server.commons.service.dto.ItemActionType;
import org.opensingular.server.commons.wicket.view.util.DispatcherPageUtil;

import static org.opensingular.server.commons.RESTPaths.DELETE;
import static org.opensingular.server.commons.RESTPaths.EXECUTE;
import static org.opensingular.server.commons.RESTPaths.PATH_BOX_ACTION;
import static org.opensingular.server.commons.RESTPaths.USERS;
import static org.opensingular.server.commons.wicket.view.util.DispatcherPageParameters.FORM_NAME;
import static org.opensingular.server.commons.wicket.view.util.DispatcherPageParameters.REQUIREMENT_ID;

public class DefaultBoxItemActionFactory {


    private static final ItemActionConfirmation CONFIRMATION_DELETE   = new ItemActionConfirmation("Excluir o rascunho", "Confirma a exclusão?", "Cancelar", "Remover", null);
    private static final ItemActionConfirmation CONFIRMATION_RELOCATE = new ItemActionConfirmation("Realocar", "Usuário:", "Cancelar", "Realocar", USERS);

    protected static String getEndpointPopUp(BoxItemData line,
                                             FormAction formAction) {
        return DispatcherPageUtil
                .baseURL("")
                .formAction(formAction.getId())
                .petitionId(line.getPetitionId())
                .param(FORM_NAME, line.getType())
                .param(REQUIREMENT_ID, line.getRequirementDefinitionId())
                .build();

    }

    protected static String getEndpointExecute(BoxItemData line) {
        return PATH_BOX_ACTION + EXECUTE + "?id=" + line.getPetitionId();

    }

    public static BoxItemAction getEDIT(BoxItemData line) {
        BoxItemAction action = new BoxItemAction("edit", "Alterar", Icone.PENCIL, ItemActionType.POPUP);
        action.setFormAction(FormAction.FORM_FILL);
        action.setEndpoint(getEndpointPopUp(line, FormAction.FORM_FILL));
        return action;
    }

    public static BoxItemAction getDELETE(BoxItemData line) {
        BoxItemAction action      = new BoxItemAction("delete", "Excluir", Icone.MINUS, ItemActionType.ENDPOINT, CONFIRMATION_DELETE);
        String        endpointUrl = PATH_BOX_ACTION + DELETE + "?id=" + line.getPetitionId();
        action.setEndpoint(endpointUrl);
        return action;
    }

    public static BoxItemAction getVIEW(BoxItemData line) {
        BoxItemAction action = new BoxItemAction("view", "Visualizar", Icone.EYE, ItemActionType.POPUP);
        action.setFormAction(FormAction.FORM_VIEW);
        action.setEndpoint(getEndpointPopUp(line, FormAction.FORM_VIEW));
        return action;
    }

    public static BoxItemAction getANALYSE(BoxItemData line) {
        BoxItemAction action = new BoxItemAction("analyse", "Analisar", Icone.PENCIL, ItemActionType.POPUP);
        action.setFormAction(FormAction.FORM_ANALYSIS);
        action.setEndpoint(getEndpointPopUp(line, FormAction.FORM_ANALYSIS));
        action.setUseExecute(true);
        return action;
    }

    public static BoxItemAction getASSIGN(BoxItemData line) {
        BoxItemAction action      = new BoxItemAction("assign", "Atribuir", Icone.ARROW_DOWN, ItemActionType.ENDPOINT, DefaultAssignController.class);
        String        endpointUrl = getEndpointExecute(line);
        action.setEndpoint(endpointUrl);
        return action;
    }

    public static BoxItemAction getRELOCATE(BoxItemData line) {
        BoxItemAction action      = new BoxItemAction("relocate", "Realocar", Icone.SHARE_SQUARE, ItemActionType.ENDPOINT, CONFIRMATION_RELOCATE);
        String        endpointUrl = getEndpointExecute(line);
        action.setEndpoint(endpointUrl);
        return action;
    }
}
