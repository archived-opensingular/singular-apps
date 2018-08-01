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

import org.opensingular.requirement.module.executor.BoxUpdaterExecutor;
import org.opensingular.requirement.module.workspace.BoxDefinition;

import java.io.Serializable;
import java.util.Set;

/**
 * API_VIEW
 */
public interface BoxInfo extends Serializable {
    /**
     * @return O ID unico desta box
     */
    String getBoxId();

    /**
     * @param boxId é o ID unico desta box, é configurado pelo {@link BoxUpdaterExecutor#saveAllBoxDefinitions()}
     */
    void setBoxId(String boxId);

    /**
     * Classe de Definição da caixa
     */
    Class<? extends BoxDefinition> getBoxDefinitionClass();

    /**
     * TODO
     */
    BoxInfo newFor(Class<? extends SingularRequirement> requirement);

    /**
     * TODO
     */
    @Deprecated
    Set<Class<? extends SingularRequirement>> getRequirements();
}