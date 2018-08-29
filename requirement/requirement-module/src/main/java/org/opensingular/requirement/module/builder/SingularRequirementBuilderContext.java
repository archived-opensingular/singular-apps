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

import java.util.LinkedHashSet;
import java.util.Set;

import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.form.SType;
import org.opensingular.requirement.module.service.RequirementSender;
import org.opensingular.requirement.module.wicket.view.form.AbstractFormPage;

public class SingularRequirementBuilderContext {

    private String name;
    private Class<? extends SType<?>> mainForm;
    private Set<Class<? extends FlowDefinition>> flowClasses = new LinkedHashSet<>();
    private Class<? extends AbstractFormPage<?, ?>> defaultExecutionPage;
    private Class<? extends RequirementSender> requirementSenderBeanClass;

    public String getName() {
        return name;
    }

    public SingularRequirementBuilderContext setName(String name) {
        this.name = name;
        return this;
    }

    public SingularRequirementBuilderContext setMainForm(Class<? extends SType<?>> mainForm) {
        this.mainForm = mainForm;
        return this;
    }

    public Class<? extends SType<?>> getMainForm() {
        return mainForm;
    }

    public Set<Class<? extends FlowDefinition>> getFlowClasses() {
        return flowClasses;
    }

    public SingularRequirementBuilderContext addFlowClass(Class<? extends FlowDefinition> flowClass) {
        this.flowClasses.add(flowClass);
        return this;
    }

    public Class<? extends AbstractFormPage<?, ?>> getDefaultExecutionPage() {
        return defaultExecutionPage;
    }

    public SingularRequirementBuilderContext defaultExecutionPage(Class<? extends AbstractFormPage<?, ?>> defaultExecutionPage) {
        this.defaultExecutionPage = defaultExecutionPage;
        return this;
    }

    public Class<? extends RequirementSender> getRequirementSenderBeanClass() {
        return requirementSenderBeanClass;
    }

    public SingularRequirementBuilderContext setRequirementSenderBeanClass(Class<? extends RequirementSender> requirementSender) {
        this.requirementSenderBeanClass = requirementSender;
        return this;
    }
}