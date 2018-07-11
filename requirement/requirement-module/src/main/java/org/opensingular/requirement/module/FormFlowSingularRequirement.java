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

import java.util.Optional;

import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.form.SType;
import org.opensingular.requirement.module.service.RequirementSender;
import org.opensingular.requirement.module.wicket.view.form.AbstractFormPage;

/**
 * Singular requirement with  Single form and Single flow.
 */
public class FormFlowSingularRequirement extends DynamicFormFlowSingularRequirement {

    public FormFlowSingularRequirement(String name, Class<? extends SType<?>> form, Class<? extends FlowDefinition> flow) {
        this(name, form, flow, null, null);
    }

    public FormFlowSingularRequirement(String name, Class<? extends SType<?>> form, Class<? extends FlowDefinition> flow,
                                              Class<? extends AbstractFormPage<?, ?>> initPage) {
        this(name, form, flow, initPage, null);
    }

    public FormFlowSingularRequirement(String name, Class<? extends SType<?>> form, Class<? extends FlowDefinition> flow,
                                              Class<? extends AbstractFormPage<?, ?>> initPage,
                                       Class<? extends RequirementSender> requirementSenderBeanClass) {
        super(name, form, new BoundedFlowResolver((c, i) -> Optional.of(flow), flow), initPage, requirementSenderBeanClass);
    }

}
