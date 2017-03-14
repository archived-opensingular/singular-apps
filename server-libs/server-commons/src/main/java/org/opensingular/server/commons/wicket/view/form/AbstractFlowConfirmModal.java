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

package org.opensingular.server.commons.wicket.view.form;


import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.opensingular.form.SInstance;
import org.opensingular.form.wicket.enums.ViewMode;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.service.PetitionInstance;

public abstract class AbstractFlowConfirmModal<PE extends PetitionEntity, PI extends PetitionInstance> implements FlowConfirmModal {

    private final AbstractFormPage<PE, PI> formPage;

    public AbstractFlowConfirmModal(AbstractFormPage<PE, PI> formPage) {
        this.formPage = formPage;
    }

    /**
     * @param tn -> transition name
     * @param im -> instance model
     * @param vm -> view mode
     * @param m  -> modal
     * @return the new AjaxButton
     */
    protected FlowConfirmButton<PE, PI> newFlowConfirmButton(String tn, IModel<? extends SInstance> im, ViewMode vm, BSModalBorder m) {
        return new FlowConfirmButton<PE, PI>(tn, "confirm-btn", im, ViewMode.EDIT == vm, formPage, m){
            @Override
            protected void onValidationSuccess(AjaxRequestTarget ajaxRequestTarget, Form form, IModel model) {
                super.onValidationSuccess(ajaxRequestTarget, form, model);
                onConfirm(tn, im);
            }
        };
    }

    protected void onConfirm(String tn, IModel<? extends SInstance> im) {

    }

    protected void addDefaultConfirmButton(String tn, IModel<? extends SInstance> im, ViewMode vm, BSModalBorder modal) {
        modal.addButton(BSModalBorder.ButtonStyle.CONFIRM, "label.button.confirm", newFlowConfirmButton(tn, im, vm, modal));
    }

    protected void addDefaultCancelButton(final BSModalBorder modal) {
        modal.addButton(
                BSModalBorder.ButtonStyle.CANCEL,
                "label.button.cancel",
                new AjaxButton("cancel-btn") {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        modal.hide(target);
                    }
                }
        );
    }

    protected final AbstractFormPage<PE, PI> getFormPage() {
        return formPage;
    }
}