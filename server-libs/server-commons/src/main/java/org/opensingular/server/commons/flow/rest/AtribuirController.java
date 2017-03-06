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

package org.opensingular.server.commons.flow.rest;

import org.opensingular.flow.core.MUser;
import org.opensingular.flow.core.ProcessInstance;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.service.PetitionUtil;
import org.springframework.stereotype.Controller;

import javax.annotation.Nonnull;

import static org.opensingular.server.commons.flow.action.DefaultActions.ACTION_ASSIGN;

@Controller
public class AtribuirController extends IController implements Loggable {

    @Override
    public ActionResponse execute(@Nonnull PetitionEntity petition, ActionRequest actionRequest) {
        try {
            ProcessInstance processInstance = PetitionUtil.getProcessInstance(petition);
            MUser user = PetitionUtil.findUserOrException(actionRequest.getIdUsuario());

            processInstance.getCurrentTaskOrException().relocateTask(user, user, false, "", actionRequest.getLastVersion());
            return new ActionResponse("Tarefa atribuída com sucesso.", true);
        } catch (Exception e) {
            String resultMessage = "Erro ao atribuir tarefa.";
            getLogger().error(resultMessage, e);
            return new ActionResponse(resultMessage, false);
        }
    }

    @Override
    public String getActionName() {
        return ACTION_ASSIGN.getName();
    }


}
