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

import org.opensingular.form.wicket.enums.AnnotationMode;
import org.opensingular.form.wicket.enums.ViewMode;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.opensingular.requirement.module.wicket.view.util.ActionContext;

import java.io.Serializable;
import java.util.Optional;

public class FormPageExecutionContext implements Serializable {

    private ActionContext actionContext;
    private String formType;
    private boolean mainForm = true;

    public FormPageExecutionContext(ActionContext context, String formName) {
        this(context);
        if (formName != null) {
            this.mainForm = false;
            this.formType = formName;
        }
    }

    public FormPageExecutionContext(ActionContext context) {
        this.actionContext = context;
        actionContext.getFormName().ifPresent(f -> formType = f);
        this.mainForm = true;
    }


    public ViewMode getViewMode() {
        return actionContext.getFormAction().orElseThrow(()-> new SingularServerException("FormAction não encontrado !")).getViewMode();
    }

    public AnnotationMode getAnnotationMode() {
        return actionContext.getFormAction().orElseThrow(()-> new SingularServerException("FormAction não encontrado !")).getAnnotationMode();
    }

    public Optional<Long> getRequirementId() {
        return actionContext.getRequirementId();
    }


    public String getFormName() {
        return formType;
    }

    public Optional<Long> getParentRequirementId() {
        return actionContext.getParentRequirementId();
    }


    public boolean isMainForm() {
        return mainForm;
    }

    public ActionContext copyOfInnerActionContext() {
        return new ActionContext(actionContext);
    }

    public Optional<String> getRequirementDefinitionKey() {
        return actionContext.getRequirementDefinitionKey();
    }
}