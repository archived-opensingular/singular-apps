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
package org.opensingular.server.core.wicket.box;


import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;
import org.opensingular.server.commons.service.dto.BoxItemAction;

import java.io.Serializable;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

public abstract class BoxContentConfirmModal<T extends Serializable> extends Panel {
    protected final BoxItemAction itemAction;
    protected final IModel<T> dataModel;
    protected final Form<?> confirmationForm = new Form<>("confirmationForm");
    protected final IModel<String> title = new Model<>();
    protected final IModel<String> bodyText = new Model<>();
    protected final BSModalBorder border = new BSModalBorder("confirmationModal", title);

    protected AjaxButton confirmButton;
    protected AjaxButton cancelButton;

    public BoxContentConfirmModal(BoxItemAction itemAction, IModel<T> dataModel) {
        super("confirmationModal");
        this.itemAction = itemAction;
        this.dataModel = dataModel;
    }

    protected abstract void onConfirm(AjaxRequestTarget target);

    @Override
    protected void onInitialize() {
        super.onInitialize();
        title.setObject(getTitleText());
        bodyText.setObject(getConfirmationMessage());
        add(confirmationForm);
        confirmationForm.add(border);
        border.add(new Label("message", bodyText));
        addCancelButton();
        addConfirmButton();
        setOutputMarkupId(true);
    }

    protected void addCancelButton() {
        border.addButton(BSModalBorder.ButtonStyle.CANCEL, $m.get(this::getCancelButtonLabel),
                cancelButton = (AjaxButton) new AjaxButton("cancel-btn", confirmationForm) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        dataModel.setObject(null);
                        border.hide(target);
                    }
                }.setDefaultFormProcessing(false));
    }

    protected void addConfirmButton() {
        border.addButton(BSModalBorder.ButtonStyle.CONFIRM, $m.get(this::getConfirmButtonLabel),
                confirmButton = new AjaxButton("confirm-btn", confirmationForm) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        onConfirm(target);
                        border.hide(target);
                    }
                });
    }

    protected String getCancelButtonLabel() {
        return itemAction == null ? "" : itemAction.getConfirmation().getCancelButtonLabel();
    }

    protected String getConfirmButtonLabel() {
        return itemAction == null ? "" : itemAction.getConfirmation().getConfirmationButtonLabel();
    }


    protected String getConfirmationMessage() {
        return itemAction == null ? "" : itemAction.getConfirmation().getConfirmationMessage();
    }

    protected String getTitleText() {
        return itemAction == null ? "" : itemAction.getConfirmation().getTitle();
    }

    public void show(AjaxRequestTarget target) {
        border.show(target);
    }
}