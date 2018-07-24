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

package org.opensingular.requirement.module;

import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.form.SType;
import org.opensingular.requirement.module.wicket.view.form.AbstractFormPage;


//TODO reqdef fazer esse builder fluente
public class RequirementConfigurationBuilder {


    private RequirementDefinitionConfiguration requirementDefinitionConfiguration = new RequirementDefinitionConfiguration();


    public RequirementDefinitionConfiguration build() {
        return requirementDefinitionConfiguration;
    }

    public RequirementConfigurationBuilder mainForm(Class<? extends SType> mainForm) {
        requirementDefinitionConfiguration.setMainForm(mainForm);
        return this;
    }

    public RequirementConfigurationBuilder flowDefintion(Class<? extends FlowDefinition> flowDefinitionClass) {
        requirementDefinitionConfiguration.setFlowDefition(flowDefinitionClass);
        return this;
    }

    public RequirementConfigurationBuilder name(String name) {
        requirementDefinitionConfiguration.setName(name);
        return this;
    }

    public RequirementConfigurationBuilder executionPage(Class<? extends AbstractFormPage<?>> formPageClass) {
        requirementDefinitionConfiguration.setExecutionPage(formPageClass);
        return this;
    }

    public RequirementConfigurationBuilder setRequirementSendListener(RequirementSendInterceptor<?, ?> requirementSendInterceptor) {
        requirementDefinitionConfiguration.setRequirementSendInterceptor(requirementSendInterceptor);
        return this;
    }
}
