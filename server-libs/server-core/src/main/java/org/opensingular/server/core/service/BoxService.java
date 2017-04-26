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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Named;

import org.apache.wicket.model.IModel;
import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.flow.persistence.entity.ProcessGroupEntity;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.box.BoxItemDataList;
import org.opensingular.server.commons.box.action.ActionResponse;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.dto.ItemActionConfirmation;
import org.opensingular.server.commons.service.dto.ItemBox;
import org.opensingular.server.core.wicket.model.BoxItemDataMap;
import org.springframework.web.client.RestTemplate;

import static org.opensingular.server.commons.RESTPaths.PATH_BOX_SEARCH;

@Named
public class BoxService implements Loggable {

    @SuppressWarnings("unchecked")
    public List<Actor> buscarUsuarios(ProcessGroupEntity processGroup, IModel<BoxItemDataMap> currentModel, ItemActionConfirmation confirmation) {
        final String connectionURL = processGroup.getConnectionURL();
        final String url           = connectionURL + PATH_BOX_SEARCH + confirmation.getSelectEndpoint();

        try {
            return Arrays.asList(new RestTemplate().postForObject(url, currentModel.getObject(), Actor[].class));
        } catch (Exception e) {
            getLogger().error("Erro ao acessar serviço: " + url, e);
            return Collections.emptyList();
        }
    }

    public <T extends ActionResponse> T callModule(String url, Object arg, Class<T> clazz) {
        return new RestTemplate().postForObject(url, arg, clazz);
    }

    public long countQuickSearch(ProcessGroupEntity processGroup, ItemBox itemBox, QuickFilter filter) {
        final String connectionURL = processGroup.getConnectionURL();
        final String url           = connectionURL + itemBox.getCountEndpoint();
        try {
            return new RestTemplate().postForObject(url, filter, Long.class);
        } catch (Exception e) {
            getLogger().error("Erro ao acessar serviço: " + url, e);
            return 0;
        }
    }

    public List<BoxItemDataMap> quickSearch(ProcessGroupEntity processGroup, ItemBox itemBox, QuickFilter filter) {
        final String connectionURL = processGroup.getConnectionURL();
        final String url           = connectionURL + itemBox.getSearchEndpoint();
        try {
            return new RestTemplate().postForObject(url, filter, BoxItemDataList.class)
                    .getBoxItemDataList()
                    .stream()
                    .map(BoxItemDataMap::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            getLogger().error("Erro ao acessar serviço: " + url, e);
            return Collections.emptyList();
        }
    }
}
