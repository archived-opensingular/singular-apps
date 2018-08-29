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

import org.opensingular.requirement.module.RequirementRegistry;
import org.opensingular.requirement.module.SingularModule;
import org.opensingular.requirement.module.WorkspaceConfiguration;
import org.opensingular.requirement.module.SingularRequirement;
import org.opensingular.requirement.module.builder.SingularRequirementBuilder;
import org.opensingular.requirement.module.config.DefaultContexts;
import org.opensingular.requirement.module.workspace.DefaultDraftbox;
import org.opensingular.requirement.module.workspace.DefaultInbox;
import org.opensingular.requirement.module.workspace.DefaultOngoingbox;
import org.opensingular.requirement.module.workspace.WorkspaceRegistry;

public class FooSingularModule implements SingularModule {

    public static final String GRUPO_TESTE = "GRUPO_TESTE";

    @Override
    public String abbreviation() {
        return GRUPO_TESTE;
    }

    @Override
    public String name() {
        return "Grupo Processo Teste";
    }

    @Override
    public void requirements(RequirementRegistry requirementRegistry) {
        requirementRegistry
                .add(FooRequirement.class);
    }

    @Override
    public void workspace(WorkspaceRegistry workspaceRegistry) {
        workspaceRegistry
                .add(FooWorklistContext.class)
                .add(FooRequirementContext.class);
    }


    public static class FooWorklistContext extends DefaultContexts.WorklistContext {
        @Override
        public void setup(WorkspaceConfiguration workspaceConfiguration) {
            workspaceConfiguration
                    .addBox(DefaultInbox.class).newFor(FooRequirement.class)
                    .addBox(DefaultDraftbox.class);
        }
    }

    public static class FooRequirementContext extends DefaultContexts.RequirementContext {
        @Override
        public void setup(WorkspaceConfiguration workspaceConfiguration) {
            workspaceConfiguration
                    .addBox(DefaultDraftbox.class).newFor(FooRequirement.class)
                    .addBox(DefaultOngoingbox.class);
        }
    }

}