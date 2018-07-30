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

import org.opensingular.requirement.module.BoxItemDataProvider;
import org.opensingular.requirement.module.service.dto.DatatableField;
import org.opensingular.requirement.module.service.dto.ItemBox;

import java.util.List;

/**
 * Factory responsible for build one item box with its listings, custom actions and controllers
 */
public interface BoxDefinition {
    /**
     * Builds an {@link ItemBox}. This method do not need to check if the {@param context} is supported.
     *
     * @return An proper configured ItemBox
     */
    ItemBox build();

    /**
     * @return the data provider responsible for populate the box data
     */
    BoxItemDataProvider getDataProvider();

    /**
     * @return the fields that are expected to show in the box
     */
    List<DatatableField> getDatatableFields();
}