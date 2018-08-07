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

package org.opensingular.requirement.module.flow.controllers;

import java.util.Optional;
import javax.annotation.Nonnull;

import org.opensingular.flow.core.SUser;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.box.action.ActionAtribuirRequest;
import org.opensingular.requirement.module.box.action.ActionRequest;
import org.opensingular.requirement.module.box.action.ActionResponse;
import org.opensingular.requirement.module.service.RequirementInstance;
import org.opensingular.requirement.module.service.RequirementUtil;
import org.springframework.stereotype.Controller;


@Controller
public class DefaultAssignController extends IController implements Loggable {

    @Override
    public ActionResponse execute(@Nonnull RequirementInstance requirement, ActionRequest action) {
        try {
            SUser user = RequirementUtil.findUserOrException(action.getIdUsuario());
            if (action instanceof ActionAtribuirRequest) {
                Optional<String> idDestiny = Optional.ofNullable(((ActionAtribuirRequest) action).getIdUsuarioDestino());
                if (idDestiny.isPresent()) {
                    return relocate(requirement, action, user, RequirementUtil.findUserOrException(idDestiny.get()));
                } else {
                    return unassign(requirement, action, user);
                }
            }
            return assign(requirement, action, user);
        } catch (Exception e) {
            String resultMessage = "Erro ao atribuir tarefa.";
            getLogger().error(resultMessage, e);
            return new ActionResponse(resultMessage, false);
        }
    }


    private ActionResponse relocate(RequirementInstance requirement, ActionRequest action, SUser author, SUser target) {
        requirement.getCurrentTaskOrException().relocateTask(author, target, false, "", action.getLastVersion());
        return new ActionResponse("Tarefa atribuída com sucesso.", true);
    }

    private ActionResponse assign(RequirementInstance requirement, ActionRequest action, SUser author) {
        requirement.getCurrentTaskOrException().relocateTask(author, author, false, "", action.getLastVersion());
        return new ActionResponse("Tarefa atribuída com sucesso.", true);
    }

    private ActionResponse unassign(RequirementInstance requirement, ActionRequest action, SUser author) {
        requirement.getCurrentTaskOrException().relocateTask(author, null, false, "", action.getLastVersion());
        return new ActionResponse("Tarefa desalocada com sucesso.", true);
    }

}
