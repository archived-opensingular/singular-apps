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

import org.opensingular.flow.core.DefinitionInfo;
import org.opensingular.flow.core.FlowMap;
import org.opensingular.flow.core.ITaskDefinition;
import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.flow.core.builder.FlowBuilder;
import org.opensingular.flow.core.builder.FlowBuilderImpl;
import org.opensingular.flow.core.defaults.PermissiveTaskAccessStrategy;
import org.opensingular.server.commons.flow.SingularRequirementTaskPageStrategy;
import org.opensingular.server.commons.wicket.view.form.FormPage;

@DefinitionInfo("fooooooooFlow")
public class FooFlow extends FlowDefinition<FlowInstance> {


    public FooFlow() {
        super(FlowInstance.class);
    }

    @Override
    protected FlowMap createFlowMap() {
        FlowBuilder flow = new FlowBuilderImpl(this);

        ITaskDefinition dobarDef  = () -> "Do bar";
        ITaskDefinition endbarDef = () -> "No more bar";

        flow.addEndTask(endbarDef);
        flow.addHumanTask(dobarDef)
                .withExecutionPage(SingularRequirementTaskPageStrategy.of(FormPage.class))
                .uiAccess(new PermissiveTaskAccessStrategy())
                .go(endbarDef);

        flow.setStartTask(dobarDef);


        return flow.build();

    }

}
