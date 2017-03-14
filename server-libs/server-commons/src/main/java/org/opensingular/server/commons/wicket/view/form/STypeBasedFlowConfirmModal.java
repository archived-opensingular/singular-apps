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
import org.apache.wicket.model.StringResourceModel;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.document.RefType;
import org.opensingular.form.event.SInstanceEventType;
import org.opensingular.form.persistence.FormKey;
import org.opensingular.form.wicket.enums.ViewMode;
import org.opensingular.form.wicket.panel.SingularFormPanel;
import org.opensingular.lib.commons.lambda.IBiConsumer;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;
import org.opensingular.lib.wicket.util.model.IReadOnlyModel;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.service.PetitionInstance;
import org.opensingular.server.commons.wicket.builder.HTMLParameters;
import org.opensingular.server.commons.wicket.builder.MarkupCreator;

public class STypeBasedFlowConfirmModal<PE extends PetitionEntity, PI extends PetitionInstance> extends AbstractFlowConfirmModal<PE, PI> {

    private final RefType refType;
    private final FormKey                          formKey;
    private final IBiConsumer<SIComposite, String> onCreateInstance;
    private       String                           transitionName;
    private       boolean                          dirty;
    private       boolean                          validatePageForm;
    private SingularFormPanel singularFormPanel;
    private       IModel<SInstance>                rootInstance;

    public STypeBasedFlowConfirmModal(AbstractFormPage<PE, PI> formPage,
                                      RefType refType,
                                      FormKey formKey,
                                      IBiConsumer<SIComposite, String> onCreateInstance, boolean validatePageForm) {
        super(formPage);

        this.refType = refType;
        this.formKey = formKey;
        this.onCreateInstance = onCreateInstance;
        this.dirty = false;
        this.validatePageForm = validatePageForm;
    }

    @Override
    public String getMarkup(String idSuffix) {
        return MarkupCreator.div("flow-modal" + idSuffix, new HTMLParameters().styleClass("portlet-body form"), MarkupCreator.div("singular-form-panel"));
    }

    @Override
    public BSModalBorder init(String idSuffix, String tn, IModel<? extends SInstance> im, ViewMode vm) {
        this.transitionName = tn;
        final BSModalBorder modal = new BSModalBorder("flow-modal" + idSuffix, new StringResourceModel("label.button.confirm", getFormPage(), null));
        addCloseButton(modal);
        addDefaultConfirmButton(tn, im, vm, modal);
        modal.add(buildSingularFormPanel());
        return modal;
    }

    @Override
    protected void addDefaultConfirmButton(String tn, IModel<? extends SInstance> im, ViewMode vm, BSModalBorder modal) {
        modal.addButton(BSModalBorder.ButtonStyle.CONFIRM, "label.button.send", newFlowConfirmButton(tn, im, vm, modal));
    }

    @Override
    protected FlowConfirmButton<PE, PI> newFlowConfirmButton(String tn, IModel<? extends SInstance> im, ViewMode vm, BSModalBorder m) {
        return new FlowConfirmButton<>(tn, "confirm-btn", im, validatePageForm && ViewMode.EDIT == vm, getFormPage(), m);
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
        if (onCreateInstance != null) {
            onCreateInstance.accept((SIComposite) instance, transitionName);
        }
                /*
                deve ser adicionado apos o listener de criar a instancia
                 */
        appendDirtyListener(instance);
        return instance;
    }

    private void appendDirtyListener(SInstance instance) {
        instance.getDocument().getInstanceListeners().add(SInstanceEventType.VALUE_CHANGED, evt -> setDirty(true));
    }

    @SuppressWarnings("unchecked")
    public IModel<SInstance> getInstanceModel() {
        if (rootInstance == null) {
            rootInstance = (IReadOnlyModel<SInstance>) () -> singularFormPanel.getInstance();
        }
        return rootInstance;
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
        /*
            força o estado de "dirty" mesmo que não exista
          nenhuma alteração ao confirmar o envio
         */
        this.setDirty(true);
    }
}