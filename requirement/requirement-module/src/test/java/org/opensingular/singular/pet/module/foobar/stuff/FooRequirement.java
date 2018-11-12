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

package org.opensingular.singular.pet.module.foobar.stuff;


import org.opensingular.requirement.module.builder.RequirementConfigurationBuilder;
import org.opensingular.requirement.module.RequirementDefinition;
import org.opensingular.requirement.module.builder.RequirementDefinitionConfiguration;
import org.opensingular.requirement.module.service.RequirementInstance;

public class FooRequirement extends RequirementDefinition<RequirementInstance> {


    public FooRequirement() {
        super("FOO_REQ", RequirementInstance.class);
    }

    @Override
    public RequirementDefinitionConfiguration configure(RequirementConfigurationBuilder conf) {
        return conf
                .name("Foo requirement")
                .mainForm(STypeFoo.class)
                .flowDefintion(FOOFlowWithTransition.class)
                .build();
    }
}
