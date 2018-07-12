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

package org.opensingular.requirement.module.builder;

import org.opensingular.form.SType;
import org.opensingular.requirement.module.wicket.view.form.AbstractFormPage;

public class SingularRequirementDefinitionForm {

    private SingularRequirementBuilderContext builderContext;

    SingularRequirementDefinitionForm(SingularRequirementBuilderContext builderContext) {
        this.builderContext = builderContext;
    }

    public SingularRequirementDefinitionForms mainForm(Class<? extends SType<?>> form) {
        return new SingularRequirementDefinitionForms(builderContext.setMainForm(form));
    }

    public SingularRequirementDefinitionForm defaultExecutionPage(Class<? extends AbstractFormPage<?, ?>> defaultExecutionPage) {
        builderContext.defaultExecutionPage(defaultExecutionPage);
        return this;
    }

}