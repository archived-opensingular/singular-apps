/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.server.core.service;

import org.apache.wicket.model.IModel;
import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.box.BoxItemDataMap;
import org.opensingular.server.commons.box.action.ActionResponse;
import org.opensingular.server.commons.connector.ModuleConnector;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.dto.ItemActionConfirmation;
import org.opensingular.server.commons.service.dto.ItemBox;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
public class BoxService implements Loggable {

    @Inject
    private ModuleConnector moduleConnector;

    @SuppressWarnings("unchecked")
    public List<Actor> buscarUsuarios(ModuleEntity module, IModel<BoxItemDataMap> currentModel, ItemActionConfirmation confirmation) {
        return moduleConnector.buscarUsuarios(module, currentModel.getObject(), confirmation);
    }

    public <T extends ActionResponse> T callModule(String url, Object arg, Class<T> clazz) {
        return moduleConnector.callModule(url, arg, clazz);
    }

    public long countQuickSearch(ModuleEntity module, ItemBox itemBox, QuickFilter filter) {
        return moduleConnector.countQuickSearch(module, itemBox, filter);
    }

    public List<BoxItemDataMap> quickSearch(ModuleEntity module, ItemBox itemBox, QuickFilter filter) {
        return moduleConnector.quickSearch(module, itemBox, filter);
    }
}
