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

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.opensingular.lib.wicket.util.resource.Icon;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.flow.controllers.IController;
import org.opensingular.server.commons.form.FormAction;

import java.io.Serializable;

public class BoxItemAction implements Serializable {

    private String                 endpoint;
    private FormAction             formAction;
    private String                 requirementId;
    private String                 name;
    private boolean                defaultAction;
    private ItemActionConfirmation confirmation;
    private String                 label;
    private Icon                   icon;
    private ItemActionType         type;
    private String                 controllerClassName;


    public BoxItemAction() {
    }

    public BoxItemAction(String name, String label, Icon icon, ItemActionType type, String endpoint, Class<? extends IController> controller, ItemActionConfirmation confirmation) {
        this.name = name;
        this.endpoint = endpoint;
        this.label = label;
        this.icon = icon;
        this.type = type;
        this.controllerClassName = controller != null ? controller.getName() : null;
        this.confirmation = confirmation;
    }

    public BoxItemAction(String name, String label, Icon icon, ItemActionType type, FormAction fomAction, String endpoint) {
        this.name = name;
        this.label = label;
        this.icon = icon;
        this.type = type;
        this.formAction = fomAction;
        this.endpoint = endpoint;
        defaultAction = false;
    }


    @Deprecated
    public String getEndpoint() {
        return endpoint;
    }

    @Deprecated
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @JsonIgnore
    public boolean isUseExecute() {
        return ItemActionType.EXECUTE == type;
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDefaultAction() {
        return defaultAction;
    }

    public void setDefaultAction(boolean defaultAction) {
        this.defaultAction = defaultAction;
    }

    public ItemActionType getType() {
        return type;
    }

    public void setType(ItemActionType type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public ItemActionConfirmation getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(ItemActionConfirmation confirmation) {
        this.confirmation = confirmation;
    }


    @SuppressWarnings("unchecked")
    @JsonIgnore
    public Class<IController> getController() {
        try {
            return (Class<IController>) Class.forName(controllerClassName);
        } catch (ClassNotFoundException e) {
            throw SingularServerException.rethrow(e.getMessage(), e);
        }
    }

    public String getControllerClassName() {
        return controllerClassName;
    }

    public void setControllerClassName(String controllerClassName) {
        this.controllerClassName = controllerClassName;
    }
}
