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

package org.opensingular.requirement.module.flow.builder;

import org.opensingular.flow.core.FlowMap;
import org.opensingular.flow.core.ITaskDefinition;
import org.opensingular.flow.core.STask;

/**
 * Representa uma {@link FlowMap} especilizada em requerimentos. Apresenta comportamentos e configurações
 * adicionais específicos de requerimentos.
 *
 * @author Daniel C. Bordin on 23/03/2017.
 */
public class FlowMapRequirement extends FlowMap {

    public FlowMapRequirement(RequirementFlowDefinition<?> flowDefinition) {
        super(flowDefinition);
    }

    @Override
    protected STransitionRequirement newTransition(STask<?> origin, String name, STask<?> destination) {
        return new STransitionRequirement(origin, name, destination);
    }

    @Override
    public STaskHumanRequirement addHumanTask(ITaskDefinition definition) {
        return addTask(new STaskHumanRequirement(this, definition.getName(), definition.getKey()));
    }
}
