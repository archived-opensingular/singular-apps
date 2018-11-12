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

import org.opensingular.requirement.module.RequirementSendInterceptor;
import org.opensingular.requirement.module.wicket.view.form.AbstractFormPage;

import javax.annotation.Nonnull;

public class RequirementAdvancedConfigurationBuilder {

    private final RequirementDefinitionConfiguration requirementDefinitionConfiguration;

    public RequirementAdvancedConfigurationBuilder(@Nonnull RequirementDefinitionConfiguration requirementDefinitionConfiguration) {
        this.requirementDefinitionConfiguration = requirementDefinitionConfiguration;
    }

    /***
     * Allows customization of the default execution page. I.E the page that is opened on every action over instances
     * of this requirement definition
     * @param formPageClass
     * @return
     */
    public RequirementAdvancedConfigurationBuilder executionPage(Class<? extends AbstractFormPage<?>> formPageClass) {
        requirementDefinitionConfiguration.setExecutionPage(formPageClass);
        return this;
    }


    /**
     * Configures a single instance of a {@link RequirementSendInterceptor}
     * @param requirementSendInterceptor
     * @return
     */
    public RequirementAdvancedConfigurationBuilder setRequirementSendInterceptor(RequirementSendInterceptor<?, ?> requirementSendInterceptor) {
        requirementDefinitionConfiguration.setRequirementSendInterceptor(requirementSendInterceptor);
        return this;
    }


    public RequirementDefinitionConfiguration build() {
        return requirementDefinitionConfiguration;
    }


}
