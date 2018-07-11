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

package org.opensingular.requirement.module.wicket.view.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.opensingular.form.SInstance;
import org.opensingular.form.wicket.component.SingularSaveButton;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;

/**
 * This button will show modal for confirmation when success,
 * and a toast if has error in validation.
 * This button have already implemented the ValidationSuccess and ValidationError.
 */
public class ServerSendButton extends SingularSaveButton {

    private final BSModalBorder sendModal;

    public ServerSendButton(String id, IModel<? extends SInstance> currentInstance, BSModalBorder sendModal) {
        super(id, currentInstance);
        this.sendModal = sendModal;
    }

    @Override
    protected void onValidationSuccess(AjaxRequestTarget target, Form<?> form, IModel<? extends SInstance> instanceModel) {
        sendModal.show(target);
    }

    @Override
    protected void onValidationError(AjaxRequestTarget target, Form<?> form, IModel<? extends SInstance> instanceModel) {
        findParent(AbstractFormPage.class).addToastrErrorMessage("message.send.error");
    }

}
