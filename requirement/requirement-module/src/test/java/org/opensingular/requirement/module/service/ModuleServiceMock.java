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

package org.opensingular.requirement.module.service;

import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.requirement.module.box.BoxItemDataMap;
import org.opensingular.requirement.module.box.action.ActionRequest;
import org.opensingular.requirement.module.box.action.ActionResponse;
import org.opensingular.requirement.module.connector.DefaultModuleService;
import org.opensingular.requirement.module.service.dto.BoxItemAction;
import org.opensingular.requirement.module.service.dto.ItemActionConfirmation;
import org.springframework.context.annotation.Primary;

import javax.inject.Named;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Primary
@Named
public class ModuleServiceMock extends DefaultModuleService {

    @Override
    public List<Actor> findEligibleUsers(BoxItemDataMap rowItemData, ItemActionConfirmation confirmAction) {
        Actor actor = new Actor(1, "USUARIO.TESTE", "Usuário de Teste", "usuarioteste@teste.com.br");
        return Collections.singletonList(actor);
    }


    @Override
    public ActionResponse executeAction(BoxItemAction itemAction, Map<String, String> params, ActionRequest actionRequest) {
        ActionResponse response = new ActionResponse("Sucesso", true);
        return ActionResponse.class.cast(response);
    }
}