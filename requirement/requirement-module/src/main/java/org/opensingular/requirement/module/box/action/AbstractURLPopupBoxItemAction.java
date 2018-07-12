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

package org.opensingular.requirement.module.box.action;

import org.opensingular.lib.commons.ui.Icon;
import org.opensingular.requirement.module.box.BoxItemData;
import org.opensingular.requirement.module.form.FormAction;
import org.opensingular.requirement.module.service.dto.BoxItemAction;
import org.opensingular.requirement.module.service.dto.ItemActionType;
import org.opensingular.requirement.module.wicket.view.util.DispatcherPageUtil;

import static org.opensingular.requirement.module.wicket.view.util.ActionContext.FORM_NAME;
import static org.opensingular.requirement.module.wicket.view.util.ActionContext.REQUIREMENT_DEFINITION_ID;

public abstract class AbstractURLPopupBoxItemAction extends BoxItemAction {


    public AbstractURLPopupBoxItemAction(String name, String label, Icon icon, FormAction action, String endpoint) {
        super(name, label, icon, ItemActionType.URL_POPUP, action, endpoint);
    }

    public AbstractURLPopupBoxItemAction(String name, String label, Icon icon, FormAction formAction, BoxItemData line) {
        this(name, label, icon, formAction, getEndpointPopUp(line, formAction));
    }

    public AbstractURLPopupBoxItemAction(String name, String label, Icon icon, String endpoint) {
        this(name, label, icon, null, endpoint);
    }


    protected static String getEndpointPopUp(BoxItemData line,
                                             FormAction formAction) {
        return DispatcherPageUtil
                .baseURL("")
                .formAction(formAction.getId())
                .requirementId(line.getRequirementId())
                .param(FORM_NAME, line.getType())
                .param(REQUIREMENT_DEFINITION_ID, line.getRequirementDefinitionId())
                .build();

    }

}
