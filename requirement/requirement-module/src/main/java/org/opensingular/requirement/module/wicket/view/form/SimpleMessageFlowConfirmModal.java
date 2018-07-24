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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.opensingular.form.SInstance;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;
import org.opensingular.requirement.module.persistence.entity.form.RequirementEntity;
import org.opensingular.requirement.module.service.RequirementInstance;


public class SimpleMessageFlowConfirmModal<RI extends RequirementInstance> extends AbstractFlowConfirmModal<RI> {

    public SimpleMessageFlowConfirmModal(String id, String transitionName, AbstractFormPage<RI> formPage) {
        super(id, transitionName, formPage);
    }

    @Override
    void addComponentsToModalBorder(BSModalBorder modalBorder) {
        addDefaultCancelButton(modalBorder);
        addDefaultConfirmButton(modalBorder);
        modalBorder.add(new Label("flow-msg", String.format("Tem certeza que deseja %s ?", getTransition())));
    }

    @Override
    protected void onConfirm(String transitionName, IModel<? extends SInstance> instanceModel) {
        super.onConfirm(transitionName, instanceModel);
        getFormPage().onConfirmTransition(transitionName, instanceModel);
    }
}