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

package org.opensingular.server.core.service;

import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.server.commons.WorkspaceConfigurationMetadata;
import org.opensingular.server.commons.box.BoxItemDataImpl;
import org.opensingular.server.commons.box.BoxItemDataMap;
import org.opensingular.server.commons.box.action.ActionRequest;
import org.opensingular.server.commons.box.action.ActionResponse;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.connector.ModuleDriver;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.service.dto.ItemActionConfirmation;
import org.opensingular.server.commons.service.dto.ItemBox;
import org.springframework.context.annotation.Primary;

import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

@Primary
@Named
public class ModuleDriverMock implements ModuleDriver {

    @Override
    public WorkspaceConfigurationMetadata retrieveModuleWorkspace(ModuleEntity module, IServerContext serverContext) {
        return null;
    }

    @Override
    public String countAll(ModuleEntity module, ItemBox box, List<String> flowNames, String loggedUser) {
        return null;
    }

    @Override
    public long countFiltered(ModuleEntity module, ItemBox box, QuickFilter filter) {
        return 2L;
    }

    @Override
    public List<BoxItemDataMap> searchFiltered(ModuleEntity module, ItemBox box, QuickFilter filter) {
        List<BoxItemDataMap> result = new ArrayList<>();
        result.add(createItem("1", "Descrição", "Situação", "Processo",
                "25/04/2017 08:49", "Form tipo", "Chave processo",
                "25/04/2017 08:49", "25/04/2017 08:49", "25/04/2017 08:49",
                "1", null, null));
        result.add(createItem("2", "Descrição", "Situação", "Processo",
                "25/04/2017 08:49", "Form tipo", "Chave processo",
                "25/04/2017 08:49", "25/04/2017 08:49", "25/04/2017 08:49",
                "1", null, null));

        return result;
    }

    @Override
    public List<Actor> findEligibleUsers(ModuleEntity module, BoxItemDataMap rowItemData, ItemActionConfirmation confirmAction) {
        Actor actor = new Actor(1, "USUARIO.TESTE", "Usuário de Teste", "usuarioteste@teste.com.br");
        return Collections.singletonList(actor);
    }

    @Override
    public String buildUrlToBeRedirected(BoxItemDataMap rowItemData, BoxItemAction rowAction, Map<String, String> params, String baseURI) {
        return null;
    }


    @Override
    public ActionResponse executeAction(ModuleEntity moduleEntity, BoxItemAction itemAction, Map<String, String> params, ActionRequest actionRequest) {
        ActionResponse response = new ActionResponse("Sucesso", true);
        return ActionResponse.class.cast(response);
    }

    private BoxItemDataMap createItem(String codPeticao, String description, String situation, String processName,
                                      String creationDate, String type, String processType, String situationBeginDate,
                                      String processBeginDate, String editionDate, String processInstanceId, String rootPetition,
                                      String parentPetition) {
        Map<String, Serializable> item = new HashMap<>();
        item.put("codPeticao", codPeticao);
        item.put("description", description);
        item.put("situation", situation);
        item.put("processName", processName);
        item.put("creationDate", creationDate);
        item.put("type", type);
        item.put("processType", processType);
        item.put("situationBeginDate", situationBeginDate);
        item.put("processBeginDate", processBeginDate);
        item.put("editionDate", editionDate);
        item.put("processInstanceId", processInstanceId);
        item.put("rootPetition", rootPetition);
        item.put("parentPetition", parentPetition);

        BoxItemDataImpl i = new BoxItemDataImpl();
        i.setRawMap(item);

        i.getBoxItemActions()
                .addEditAction(i)
                .addViewAction(i)
                .addDeleteAction(i)
                .addRelocateAction(i);

        return new BoxItemDataMap(i);
    }


}
