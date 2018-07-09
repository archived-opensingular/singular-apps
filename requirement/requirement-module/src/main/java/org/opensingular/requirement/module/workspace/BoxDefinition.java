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

package org.opensingular.requirement.module.workspace;

import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.service.dto.DatatableField;
import org.opensingular.requirement.module.service.dto.ItemBox;
import org.opensingular.requirement.module.BoxItemDataProvider;

import java.util.List;

/**
 * Factory responsible for build one item box with its listings, custom actions and controllers
 */
public interface BoxDefinition {

    /**
     * Checks is this {@link BoxDefinition} can be used under the given {@link IServerContext}
     * this method can be called multiple times.
     * @param context
     *  the current {@link IServerContext}
     * @return
     *  true if this factory can be used under the give {@param context}, false otherwise
     */
    boolean appliesTo(IServerContext context);

    /**
     * Builds an {@link ItemBox}. This method do not need to check if the {@param context} is supported, the current
     * {@link IServerContext} can be used to decide about minor changes in the {@param ItemBox} for each different
     * context
     * @param context
     *   the current {@link IServerContext}
     * @return
     *  An proper configured ItemBox
     */
    ItemBox build(IServerContext context);


    BoxItemDataProvider getDataProvider();

    List<DatatableField> getDatatableFields();

}
