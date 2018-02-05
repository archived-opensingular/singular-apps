/*
 * Copyright (c) 2016, Singular and/or its affiliates. All rights reserved.
 * Singular PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.opensingular.server.commons.flow.controllers;

import org.opensingular.flow.core.SUser;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.box.action.ActionAtribuirRequest;
import org.opensingular.server.commons.box.action.ActionRequest;
import org.opensingular.server.commons.box.action.ActionResponse;
import org.opensingular.server.commons.service.RequirementInstance;
import org.opensingular.server.commons.service.RequirementUtil;
import org.springframework.stereotype.Controller;

import javax.annotation.Nonnull;
import java.util.Optional;


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
