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


import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.opensingular.form.SInstance;
import org.opensingular.form.wicket.enums.ViewMode;
import org.opensingular.lib.commons.base.SingularUtil;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;
import org.opensingular.requirement.module.persistence.entity.form.RequirementEntity;
import org.opensingular.requirement.module.service.RequirementInstance;

public abstract class AbstractFlowConfirmModal<RI extends RequirementInstance> extends FlowConfirmPanel {

    private final AbstractFormPage<RI> formPage;

    public AbstractFlowConfirmModal(String id, String transition, AbstractFormPage<RI> formPage) {
        super(id, transition);
        this.formPage = formPage;
    }

    /**
     * @param tn -> transition name
     * @param im -> instance model
     * @param vm -> view mode
     * @param m  -> modal
     * @return the new AjaxButton
     */
    protected FlowConfirmButton<RI> newFlowConfirmButton(String tn, IModel<? extends SInstance> im, ViewMode vm, BSModalBorder m, boolean validation) {
        return new FlowConfirmButton<RI>(tn, "confirm-btn", im, validation && ViewMode.EDIT == vm, formPage, m) {
            @Override
            protected void onValidationSuccess(AjaxRequestTarget ajaxRequestTarget, Form form, IModel model) {
                onConfirm(tn, im);
                super.onValidationSuccess(ajaxRequestTarget, form, model);
            }
        };
    }

    protected void onConfirm(String tn, IModel<? extends SInstance> im) {

    }

    protected void addDefaultConfirmButton(BSModalBorder modal) {
        addDefaultConfirmButton(modal, true);
    }

    /**
     * Add the confirm button to confirmation modal. The button label can be customized by creating the
     * wicket resource bundle using  label.button.confirm. plus the java identity of the transition name, for example
     * the transition name Analisar Outorga has java identity equals to analisarOutorga, creating the resource key
     * label.button.confirm.analisarOutorga=Confirmar Analise, will override the button label.
     *
     * @see SingularUtil#convertToJavaIdentity(String, boolean)
     * @see org.apache.wicket.resource.loader.IStringResourceLoader
     * @see <a href='https://ci.apache.org/projects/wicket/guide/6.x/guide/i18n.html'>Internationalization with Wicket</a>
     *
     * @param modal the modal to add the button
     */
    protected void addDefaultConfirmButton(BSModalBorder modal, boolean validation) {
        String transition = getTransition();
        IModel<? extends SInstance> formInstance = getFormPage().getFormInstance();
        ViewMode viewMode = getFormPage().getViewMode(getFormPage().getConfig());

        String transitionButtonLabel= "label.button.confirm."+ SingularUtil.convertToJavaIdentity(transition, true);
        String defaultButtonLabelWhenNull = getString("label.button.confirm", null, "Confirmar");

        FlowConfirmButton<RI> button = newFlowConfirmButton(transition, formInstance, viewMode, modal, validation);
        modal.addButton(BSModalBorder.ButtonStyle.CONFIRM, transitionButtonLabel, defaultButtonLabelWhenNull, button );
    }

    //FIXME esse método talvez deva estar no SimpleMessageFlowConfirmModal
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

    protected final AbstractFormPage<RI> getFormPage() {
        return formPage;
    }
}