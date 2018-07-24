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

import org.opensingular.requirement.module.RequirementConfiguration;
import org.opensingular.requirement.module.SingularModule;
import org.opensingular.requirement.module.WorkspaceConfiguration;
import org.opensingular.requirement.module.RequirementDefinition;
import org.opensingular.requirement.module.builder.SingularRequirementBuilder;
import org.opensingular.requirement.module.config.DefaultContexts;
import org.opensingular.requirement.module.workspace.*;

public class FooSingularModule implements SingularModule {

    public static final String GRUPO_TESTE = "GRUPO_TESTE";
    private FooRequirement fooRequirement = new FooRequirement();

    @Override
    public String abbreviation() {
        return GRUPO_TESTE;
    }

    @Override
    public String name() {
        return "Grupo Processo Teste";
    }

    @Override
    public void requirements(RequirementConfiguration config) {
        config
                .addRequirement(fooRequirement)
                .addRequirement(this::barRequirement);
    }

    @Override
    public void workspace(WorkspaceRegistry workspaceRegistry) {
        workspaceRegistry
                .add(DefaultContexts.WorklistContext.class)
                .addBox(new DefaultInbox()).newFor(this::barRequirement)
                .addBox(new DefaultDonebox()).newFor(fooRequirement);

        workspaceRegistry
                .add(DefaultContexts.RequirementContext.class)
                .addBox(new DefaultDraftbox()).newFor(this::barRequirement)
                .addBox(new DefaultOngoingbox()).newFor(fooRequirement);
    }


    public RequirementDefinition barRequirement(SingularRequirementBuilder builder) {
        return builder
                .name("Bar Requirement")
                .mainForm(STypeFoo.class)
                .allowedFlow(FooFlow.class)
                .build();
    }
}
