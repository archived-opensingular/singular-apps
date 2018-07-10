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
package org.opensingular.requirement.module.wicket.box;


import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.opensingular.form.wicket.mapper.components.AbstractConfirmationModal;
import org.opensingular.requirement.module.service.dto.BoxItemAction;

import java.io.Serializable;

public abstract class BoxContentConfirmModal<T extends Serializable> extends AbstractConfirmationModal {
    protected final BoxItemAction itemAction;
    protected final IModel<T>     dataModel;

    public BoxContentConfirmModal(BoxItemAction itemAction, IModel<T> dataModel) {
        super("confirmationModal");
        this.itemAction = itemAction;
        this.dataModel = dataModel;
    }

    @Override
    protected String getCancelButtonLabel() {
        return itemAction == null ? "" : itemAction.getConfirmation().getCancelButtonLabel();
    }

    @Override
    protected String getConfirmButtonLabel() {
        return itemAction == null ? "" : itemAction.getConfirmation().getConfirmationButtonLabel();
    }

    @Override
    protected String getConfirmationMessage() {
        return itemAction == null ? "" : itemAction.getConfirmation().getConfirmationMessage();
    }

    @Override
    protected String getTitleText() {
        return itemAction == null ? "" : itemAction.getConfirmation().getTitle();
    }

    public void show(AjaxRequestTarget target) {
        border.show(target);
    }

}