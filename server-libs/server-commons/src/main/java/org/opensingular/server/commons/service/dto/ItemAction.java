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
import org.opensingular.lib.wicket.util.resource.Icone;
import org.opensingular.lib.wicket.util.resource.SingularIcon;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.flow.controllers.IController;

import java.io.Serializable;

public class ItemAction implements Serializable {

    private String  name;
    private boolean defaultAction;

    private String         label;
    private SingularIcon   icon;
    private ItemActionType type;
    private String         controllerClassName;

    private ItemActionConfirmation confirmation;

    public ItemAction() {
    }

    public ItemAction(String name) {
        this.name = name;
        defaultAction = true;
    }

    public ItemAction(String name, String label, Icone icon, ItemActionType type) {
        this.name = name;
        this.label = label;
        this.icon = icon;
        this.type = type;
        defaultAction = false;
    }

    public ItemAction(String name, String label, Icone icon, ItemActionType type, ItemActionConfirmation confirmation) {
        this.name = name;
        this.label = label;
        this.icon = icon;
        this.type = type;
        this.confirmation = confirmation;
        defaultAction = false;
    }

    public ItemAction(String name, String label, Icone icon, ItemActionType type, Class<? extends IController> controller, ItemActionConfirmation confirmation) {
        this.name = name;
        this.defaultAction = defaultAction;
        this.label = label;
        this.icon = icon;
        this.type = type;
        this.controllerClassName = controller.getName();
        this.confirmation = confirmation;
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

    public SingularIcon getIcon() {
        return icon;
    }

    public void setIcon(Icone icon) {
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
