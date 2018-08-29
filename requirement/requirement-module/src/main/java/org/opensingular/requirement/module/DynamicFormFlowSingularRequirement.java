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

package org.opensingular.requirement.module;

import org.opensingular.form.SType;
import org.opensingular.requirement.module.service.RequirementSender;
import org.opensingular.requirement.module.wicket.view.form.AbstractFormPage;

/**
 * Singular requirement implementation capable of dynamically resolve
 * the necessary flow.
 */
public class DynamicFormFlowSingularRequirement extends SingularRequirementAdapter {

    private Class<? extends SType<?>> form;
    private Class<? extends AbstractFormPage<?, ?>> initPage;
    private Class<? extends RequirementSender> requirementSenderBeanClass;

    public DynamicFormFlowSingularRequirement(String name,
                                              Class<? extends SType<?>> form,
                                              BoundedFlowResolver flowResolver,
                                              Class<? extends AbstractFormPage<?, ?>> initPage,
                                              Class<? extends RequirementSender> requirementSenderBeanClass) {
        super(name, flowResolver);
        this.form = form;
        this.initPage = initPage;
        this.requirementSenderBeanClass = requirementSenderBeanClass;
    }

    @Override
    public final Class<? extends SType> getMainForm() {
        return form;
    }

    @Override
    public Class<? extends AbstractFormPage<?, ?>> getDefaultExecutionPage() {
        if (initPage != null) {
            return initPage;
        } else {
            return super.getDefaultExecutionPage();
        }
    }

    public Class<? extends RequirementSender> getRequirementSenderBeanClass() {
        if(requirementSenderBeanClass != null) {
            return requirementSenderBeanClass;
        } else {
            return super.getRequirementSenderBeanClass();
        }
    }

}