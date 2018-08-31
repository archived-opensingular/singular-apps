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

package org.opensingular.requirement.module.builder;

import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.form.SType;
import org.opensingular.requirement.module.EmptyRequirementSendInterceptor;
import org.opensingular.requirement.module.RequirementSendInterceptor;
import org.opensingular.requirement.module.service.RequirementInstance;
import org.opensingular.requirement.module.service.dto.RequirementSubmissionResponse;
import org.opensingular.requirement.module.wicket.view.form.AbstractFormPage;
import org.opensingular.requirement.module.wicket.view.form.FormPage;

public class RequirementDefinitionConfiguration {


    private Class<? extends AbstractFormPage<?>> executionPage              = FormPage.class;
    private Class<? extends SType<?>>            mainForm;
    private Class<? extends FlowDefinition>      flowDefition;
    private String                               name;
    private RequirementSendInterceptor           requirementSendInterceptor = new EmptyRequirementSendInterceptor();


    public Class<? extends SType<?>> getMainForm() {
        return mainForm;
    }

    public Class<? extends FlowDefinition> getFlowDefinition() {
        return flowDefition;
    }

    public String getName() {
        return name;
    }

    public Class<? extends AbstractFormPage<?>> getExecutionPage() {
        return executionPage;
    }


    public void setExecutionPage(Class<? extends AbstractFormPage<?>> executionPage) {
        this.executionPage = executionPage;
    }

    void setMainForm(Class<? extends SType<?>> mainForm) {
        this.mainForm = mainForm;
    }

    void setFlowDefition(Class<? extends FlowDefinition> flowDefition) {
        this.flowDefition = flowDefition;
    }

    void setName(String name) {
        this.name = name;
    }

    public <RI extends RequirementInstance, RSR extends RequirementSubmissionResponse> RequirementSendInterceptor<RI, RSR> getRequirementSendInterceptor() {
        return requirementSendInterceptor;
    }

    public void setRequirementSendInterceptor(RequirementSendInterceptor<?, ?> requirementSendInterceptor) {
        this.requirementSendInterceptor = requirementSendInterceptor;
    }
}
