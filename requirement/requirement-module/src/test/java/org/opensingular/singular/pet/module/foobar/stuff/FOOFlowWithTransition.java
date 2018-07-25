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

import javax.annotation.Nonnull;

import org.opensingular.flow.core.DefinitionInfo;
import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.flow.core.FlowMap;
import org.opensingular.flow.core.ITaskDefinition;
import org.opensingular.flow.core.builder.FlowBuilder;
import org.opensingular.flow.core.builder.FlowBuilderImpl;
import org.opensingular.flow.core.defaults.PermissiveTaskAccessStrategy;
import org.opensingular.requirement.module.flow.SingularRequirementTaskPageStrategy;
import org.opensingular.requirement.module.wicket.view.form.FormPage;

@DefinitionInfo("fooFlowWithTransitionCommons")
public class FOOFlowWithTransition extends FlowDefinition<FlowInstance> {

    public FOOFlowWithTransition() {
        super(FlowInstance.class);
    }

    @Nonnull
    @Override
    protected FlowMap createFlowMap() {
        FlowBuilder flow = new FlowBuilderImpl(this);

        ITaskDefinition startbarDef  = () -> "Start bar";
        ITaskDefinition dobarDef  = () -> "Do bar";
        ITaskDefinition middlebarDef = () -> "Transition bar";
        ITaskDefinition nobarDef = () -> "No more bar";
        ITaskDefinition endbarDef = () -> "End bar";

        flow.addHumanTask(dobarDef)
                .withExecutionPage(SingularRequirementTaskPageStrategy.of(FormPage.class))
                .uiAccess(new PermissiveTaskAccessStrategy());

        flow.addHumanTask(startbarDef)
                .withExecutionPage(SingularRequirementTaskPageStrategy.of(FormPage.class))
                .uiAccess(new PermissiveTaskAccessStrategy());

        flow.addHumanTask(middlebarDef)
                .withExecutionPage(SingularRequirementTaskPageStrategy.of(FormPage.class))
                .uiAccess(new PermissiveTaskAccessStrategy());

        flow.setStartTask(startbarDef);

        flow.addEndTask(endbarDef);
        flow.addEndTask(nobarDef);

        flow.from(startbarDef).go(middlebarDef);
        flow.from(startbarDef).go(dobarDef);
        flow.from(dobarDef).go(nobarDef);
        flow.from(startbarDef).go(nobarDef);
        flow.from(middlebarDef).go(endbarDef);


        return flow.build();//NOSONAR
    }
}
