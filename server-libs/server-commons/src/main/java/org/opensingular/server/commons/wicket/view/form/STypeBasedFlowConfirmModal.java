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
import org.apache.wicket.model.Model;
import org.opensingular.form.SInstance;
import org.opensingular.form.document.RefType;
import org.opensingular.form.event.SInstanceEventType;
import org.opensingular.form.persistence.FormKey;
import org.opensingular.form.wicket.enums.ViewMode;
import org.opensingular.form.wicket.panel.SingularFormPanel;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.service.PetitionInstance;

public class STypeBasedFlowConfirmModal<PE extends PetitionEntity, PI extends PetitionInstance> extends AbstractFlowConfirmModal<PE, PI> {

    private final RefType                 refType;
    private final FormKey                 formKey;
    private final TransitionController<?> transitionController;
    private       boolean                 dirty;
    private       SingularFormPanel       singularFormPanel;

    public STypeBasedFlowConfirmModal(String id,
                                      String transitionName,
                                      AbstractFormPage<PE, PI> formPage,
                                      RefType refType,
                                      FormKey formKey,
                                      TransitionController<?> transitionController) {
        super(id, transitionName, formPage);
        this.refType = refType;
        this.formKey = formKey;
        this.transitionController = transitionController;
        this.dirty = false;
    }


    @Override
    protected void addDefaultConfirmButton(BSModalBorder modal) {
        modal.addButton(BSModalBorder.ButtonStyle.CONFIRM,
                "label.button.confirm",
                newFlowConfirmButton(getTransition(),
                        getFormPage().getInstanceModel(),
                        getFormPage().getConfig().getViewMode(),
                        modal));
    }

    @Override
    protected FlowConfirmButton<PE, PI> newFlowConfirmButton(String tn, IModel<? extends SInstance> im, ViewMode vm, BSModalBorder m) {
        return new FlowConfirmButton<>(tn, "confirm-btn", im, transitionController.isValidatePageForm() && ViewMode.EDIT == vm, getFormPage(), m);
    }

    private void addCloseButton(BSModalBorder modal) {
        modal.addButton(
                BSModalBorder.ButtonStyle.CANCEL,
                Model.of("Fechar"),
                new AjaxButton("cancel-btn") {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        modal.hide(target);
                    }
                }
        );
    }

    private SingularFormPanel buildSingularFormPanel() {
        singularFormPanel = new SingularFormPanel("singular-form-panel", true);
        singularFormPanel.setInstanceCreator(this::createInstance);
        return singularFormPanel;
    }

    private SInstance createInstance() {
        SInstance instance;
        if (formKey != null) {
            instance = getFormPage().getFormPetitionService().getSInstance(formKey, refType);
        } else {
            instance = getFormPage().getFormPetitionService().createInstance(refType);
        }
        if (transitionController != null) {
            transitionController.onCreateInstance(getFormPage().getInstance(), instance);
        }
        //deve ser adicionado apos o listener de criar a instancia
        appendDirtyListener(instance);
        return instance;
    }

    private void appendDirtyListener(SInstance instance) {
        instance.getDocument().getInstanceListeners().add(SInstanceEventType.VALUE_CHANGED, evt -> setDirty(true));
    }

    @SuppressWarnings("unchecked")
    public IModel<? extends SInstance> getInstanceModel() {
        return singularFormPanel.getInstanceModel();
    }

    public boolean isDirty() {
        return dirty;
    }

    public STypeBasedFlowConfirmModal setDirty(boolean dirty) {
        this.dirty = dirty;
        return this;
    }

    @Override
    protected void onConfirm(String tn, IModel<? extends SInstance> im) {
        super.onConfirm(tn, im);
        // força o estado de "dirty" mesmo que não exista nenhuma alteração ao confirmar o envio
        this.setDirty(true);
    }

    @Override
    void addComponentsToModalBorder(BSModalBorder modalBorder) {
        addCloseButton(modalBorder);
        addDefaultConfirmButton(modalBorder);
        modalBorder.add(buildSingularFormPanel());
    }

}