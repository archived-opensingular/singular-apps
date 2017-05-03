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

import org.opensingular.form.wicket.enums.AnnotationMode;
import org.opensingular.form.wicket.enums.ViewMode;
import org.opensingular.server.commons.flow.FlowResolver;
import org.opensingular.server.commons.service.PetitionSender;
import org.opensingular.server.commons.wicket.view.util.ActionContext;

import java.io.Serializable;
import java.util.Optional;

public class FormPageExecutionContext implements Serializable {

    private ActionContext actionContext;
    private String formType;
    private FlowResolver resolver;
    private boolean mainForm = true;
    private Class<? extends PetitionSender> petitionSender;

    public FormPageExecutionContext(ActionContext context, String formName, FlowResolver resolver, Class<? extends PetitionSender> petitionSender) {
        this(context);
        this.resolver = resolver;
        if (formName != null) {
            this.mainForm = false;
            this.formType = formName;
        }
        this.petitionSender = petitionSender;
    }

    public FormPageExecutionContext(ActionContext context) {
        this.actionContext = context;
        actionContext.getFormName().ifPresent(f -> formType = f);
        this.mainForm = true;
    }


    public ViewMode getViewMode() {
        return actionContext.getFormAction().get().getViewMode();
    }

    public AnnotationMode getAnnotationMode() {
        return actionContext.getFormAction().get().getAnnotationMode();
    }

    public Optional<Long> getPetitionId() {
        return actionContext.getPetitionId();
    }


    public String getFormName() {
        return formType;
    }

    public Optional<Long> getParentPetitionId() {
        return actionContext.getParentPetitionId();
    }


    public boolean isMainForm() {
        return mainForm;
    }

    public FlowResolver getFlowResolver() {
        return resolver;
    }

    public ActionContext copyOfInnerActionContext() {
        return actionContext.clone();
    }

    public Class<? extends PetitionSender> getPetitionSender() {
        return petitionSender;
    }
}