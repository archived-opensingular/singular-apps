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

package org.opensingular.requirement.module.wicket.view.form;

import de.alpharogroup.wicket.js.addon.toastr.ToastrType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.opensingular.flow.core.ITaskDefinition;
import org.opensingular.flow.core.TaskInstance;
import org.opensingular.form.SFormUtil;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.wicket.component.SingularSaveButton;
import org.opensingular.form.wicket.enums.ViewMode;
import org.opensingular.form.wicket.panel.SingularFormPanel;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.opensingular.requirement.module.service.RequirementInstance;
import org.opensingular.requirement.module.wicket.view.SingularToastrHelper;

import javax.annotation.Nonnull;

import static org.opensingular.lib.wicket.util.util.Shortcuts.$m;

public class STypeBasedFlowConfirmModal<RI extends RequirementInstance> extends AbstractFlowConfirmModal<RI> {

    private final IModel<RI>              requirementInstanceModel;
    private final TransitionController<?> transitionController;
    private       SingularFormPanel       singularFormPanel;

    public STypeBasedFlowConfirmModal(String id,
                                      String transitionName,
                                      AbstractFormPage<RI> formPage,
                                      IModel<RI> requirementInstanceModel,
                                      TransitionController<?> transitionController) {
        super(id, transitionName, formPage);
        this.requirementInstanceModel = requirementInstanceModel;
        this.transitionController = transitionController;
    }

    @Override
    protected FlowConfirmButton<RI> newFlowConfirmButton(String tn, IModel<? extends SInstance> im, ViewMode vm, BSModalBorder m, boolean validation) {
        return new FlowConfirmButton<RI>(tn, "confirm-btn", im, isValidateConfirmButton(vm, validation), getFormPage(), m) {
            @Override
            protected void onValidationSuccess(AjaxRequestTarget ajaxRequestTarget, Form<?> form, IModel<? extends SInstance> model) {
                getRequirement().saveForm(singularFormPanel.getInstance());
                onConfirm(tn, singularFormPanel.getInstanceModel());
                super.onValidationSuccess(ajaxRequestTarget, form, model);
            }
        };
    }

    private boolean isValidateConfirmButton(ViewMode vm, boolean validation) {
        return validation && transitionController.isValidatePageForm() && ViewMode.EDIT == vm;
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
                }.setDefaultFormProcessing(false)
        );
    }

    private SingularFormPanel buildSingularFormPanel() {
        singularFormPanel = new SingularFormPanel("singular-form-panel", true);
        singularFormPanel.setInstanceCreator(this::createInstance);
        singularFormPanel.setModalContainer(externalContainer);
        return singularFormPanel;
    }

    /**
     * Retorna a petição atual ou dispara exception se ainda não estiver configurada.
     */
    @Nonnull
    protected final RI getRequirement() {
        if (requirementInstanceModel != null && requirementInstanceModel.getObject() != null) {
            return requirementInstanceModel.getObject();
        }
        throw new SingularServerException("O requerimento (" + RequirementInstance.class.getName() + ") ainda está null");
    }

    private SInstance createInstance() {
        SInstance    instance;
        TaskInstance taskInstance = getRequirement().getFlowInstance().getCurrentTaskOrException();
        instance = getRequirement().resolveForm(SFormUtil.getTypeName(transitionController.getType()), ITaskDefinition.of(taskInstance.getName(), taskInstance.getAbbreviation()));
        transitionController.onCreateInstance((SIComposite) getFormPage().getInstanceModel().getObject(), instance);
        return instance;
    }

    public IModel<? extends SInstance> getInstanceModel() {
        return singularFormPanel.getInstanceModel();
    }

    @Override
    protected void onConfirm(String tn, IModel<? extends SInstance> im) {
        super.onConfirm(tn, im);
        transitionController.onConfirmTransition(requirementInstanceModel.getObject(), tn, im);
    }

    @Override
    void addComponentsToModalBorder(BSModalBorder modalBorder) {
        addCloseButton(modalBorder);
        addDefaultConfirmButton(modalBorder);
        addSaveButton(modalBorder);
        modalBorder.add(buildSingularFormPanel());
    }

    private void addSaveButton(BSModalBorder modalBorder) {
        if (transitionController.isShowSaveButton()) {
            modalBorder.addButton(BSModalBorder.ButtonStyle.DEFAULT, "label.button.save", "Salvar", new SingularSaveButton("id", $m.get(() -> singularFormPanel.getInstanceModel().getObject()), false) {
                @Override
                protected void onValidationSuccess(AjaxRequestTarget target, Form<?> form, IModel<? extends SInstance> instanceModel) {
                    getRequirement().saveForm(instanceModel.getObject());
                    new SingularToastrHelper(this.getPage()).
                            addToastrMessage(ToastrType.SUCCESS, "message.data.success", null);
                }
            });
        }
    }

    /**
     * This method is responsible for update the container of the modal, and show.
     * The update container will call the build of all Singular components for initializing with correct behavior.
     *
     * @param ajaxRequestTarget The target to show modal.
     */
    @Override
    public void onShowUpdate(AjaxRequestTarget ajaxRequestTarget) {
        singularFormPanel.updateContainer();
        getModalBorder().show(ajaxRequestTarget);
    }
}