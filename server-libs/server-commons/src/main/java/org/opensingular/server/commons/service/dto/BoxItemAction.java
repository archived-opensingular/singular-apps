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

package org.opensingular.server.commons.service.dto;

import org.opensingular.lib.wicket.util.resource.Icone;
import org.opensingular.server.commons.flow.controllers.DefaultAssignController;
import org.opensingular.server.commons.flow.controllers.IController;
import org.opensingular.server.commons.form.FormAction;

public class BoxItemAction extends ItemAction {

    private String     endpoint;
    private FormAction formAction;
    private String     requirementId;
    private boolean useExecute = false;


    public BoxItemAction() {
    }

    public BoxItemAction(String name) {
        super(name);
    }

    public BoxItemAction(String name, String label, Icone icon, ItemActionType type) {
        super(name, label, icon, type);
    }

    public BoxItemAction(String name, String label, Icone icon, ItemActionType type, ItemActionConfirmation confirmation) {
        super(name, label, icon, type, confirmation);
    }

    public BoxItemAction(String name, String label, Icone icon, ItemActionType type, Class<? extends IController> defaultAssignControllerClass) {
        super(name, label, icon, type, defaultAssignControllerClass, null);
    }

    @Deprecated
    public String getEndpoint() {
        return endpoint;
    }

    @Deprecated
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public boolean isUseExecute() {
        return useExecute;
    }

    public void setUseExecute(boolean useExecute) {
        this.useExecute = useExecute;
    }

    public FormAction getFormAction() {
        return formAction;
    }

    public void setFormAction(FormAction formAction) {
        this.formAction = formAction;
    }

    public String getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(String requirementId) {
        this.requirementId = requirementId;
    }
}