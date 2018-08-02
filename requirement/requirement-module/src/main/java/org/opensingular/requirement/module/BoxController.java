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

import org.opensingular.requirement.module.box.BoxItemDataImpl;
import org.opensingular.requirement.module.box.BoxItemDataList;
import org.opensingular.requirement.module.persistence.filter.BoxFilter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class BoxController {
    /**
     *
     */
    private final BoxItemDataProvider boxItemDataProvider;

    public BoxController(BoxItemDataProvider boxItemDataProvider) {
        this.boxItemDataProvider = boxItemDataProvider;
    }

    public Long countItens(BoxFilter filter) {
        return boxItemDataProvider.count(filter);
    }

    public BoxItemDataList searchItens(BoxFilter filter) {
        List<Map<String, Serializable>> itens = boxItemDataProvider.search(filter);
        BoxItemDataList result = new BoxItemDataList();
        ActionProvider actionProvider = addBuiltInDecorators(boxItemDataProvider.getActionProvider());

        for (Map<String, Serializable> item : itens) {
            BoxItemDataImpl line = new BoxItemDataImpl();
            line.setRawMap(item);
            line.setBoxItemActions(actionProvider.getLineActions(line, filter));
            result.getBoxItemDataList().add(line);
        }
        return result;
    }

    protected ActionProvider addBuiltInDecorators(ActionProvider actionProvider) {
        return new AuthorizationAwareActionProviderDecorator(actionProvider);
    }
}