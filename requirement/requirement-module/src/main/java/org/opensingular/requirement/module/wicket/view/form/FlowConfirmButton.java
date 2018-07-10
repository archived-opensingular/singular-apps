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
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.opensingular.form.SInstance;
import org.opensingular.form.wicket.component.SingularSaveButton;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;
import org.opensingular.requirement.module.exception.RequirementConcurrentModificationException;
import org.opensingular.requirement.module.exception.SingularServerFormValidationError;
import org.opensingular.requirement.module.persistence.entity.form.RequirementEntity;
import org.opensingular.requirement.module.service.RequirementInstance;
import org.springframework.orm.hibernate4.HibernateOptimisticLockingFailureException;

public class FlowConfirmButton<RE extends RequirementEntity, RI extends RequirementInstance> extends SingularSaveButton implements Loggable {

    private final AbstractFormPage<RE, RI> formPage;
    private final String              transitionName;
    private final BSModalBorder       modal;

    public FlowConfirmButton(final String transitionName,
                             final String id,
                             final IModel<? extends SInstance> model,
                             final boolean validate,
                             final AbstractFormPage<RE, RI> formPage,
                             final BSModalBorder modal) {
        super(id, model, validate);
        this.formPage = formPage;
        this.transitionName = transitionName;
        this.modal = modal;
    }

    @Override
    protected void onValidationSuccess(AjaxRequestTarget ajaxRequestTarget, Form<?> form, IModel<? extends SInstance> model) {
        try {
            formPage.executeTransition(ajaxRequestTarget, form, transitionName, model);
        } catch (HibernateOptimisticLockingFailureException | RequirementConcurrentModificationException e) {
            getLogger().debug(e.getMessage(), e);
            configureBackDropAndShowError(ajaxRequestTarget, "message.save.concurrent_error");
        } catch (SingularServerFormValidationError ex){
            getLogger().debug(ex.getMessage(), ex);
            configureBackDropAndShowError(ajaxRequestTarget, "message.send.error");
        }
    }

    private void configureBackDropAndShowError(AjaxRequestTarget ajaxRequestTarget, String messageKey) {
        modal.hide(ajaxRequestTarget);
        formPage.addToastrErrorMessage(messageKey);
        modal.show(ajaxRequestTarget);
    }

    @Override
    protected void onValidationError(final AjaxRequestTarget ajaxRequestTarget,
                                     final Form<?> form,
                                     final IModel<? extends SInstance> instanceModel) {
        modal.hide(ajaxRequestTarget);
        formPage.addToastrErrorMessage("Não é possivel " + transitionName.toLowerCase() + " enquanto houver correções a serem feitas.");
        ajaxRequestTarget.add(form);
    }

}