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
import org.apache.wicket.model.IModel;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.lib.wicket.util.bootstrap.layout.BSContainer;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;
import org.opensingular.requirement.module.service.RequirementInstance;

import java.io.Serializable;

public interface TransitionController<T extends SType<?>> extends Serializable {
    Class<T> getType();

    boolean isValidatePageForm();

    default void onCreateInstance(SIComposite pageInstance, SInstance transitionInstance) {
    }

    default boolean onShow(SIComposite pageInstance, SInstance transitionInstance, BSModalBorder modal, AjaxRequestTarget ajaxRequestTarget) {
        return true;
    }

    default boolean isShowSaveButton() {
        return true;
    }

    default void appendExtraContent(BSContainer extraContainer) {
    }

    default void onConfirmTransition(RequirementInstance<?, ?> requirementInstance, String transitionName, IModel<? extends SInstance> instanceModel) {

    }
}
