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
import org.opensingular.server.commons.service.PetitionInstance;
import org.opensingular.server.commons.service.PetitionUtil;
import org.springframework.stereotype.Controller;

import javax.annotation.Nonnull;
import java.util.Optional;


@Controller
public class DefaultAssignController extends IController implements Loggable {

    @Override
    public ActionResponse execute(@Nonnull PetitionInstance petition, ActionRequest action) {
        try {
            SUser user = PetitionUtil.findUserOrException(action.getIdUsuario());
            if (action instanceof ActionAtribuirRequest) {
                Optional<String> idDestino = Optional.ofNullable(((ActionAtribuirRequest) action).getIdUsuarioDestino());
                if (idDestino.isPresent()) {
                    return relocate(petition, action, user, PetitionUtil.findUserOrException(idDestino.get()));
                } else {
                    return unassign(petition, action, user);
                }
            }
            return assign(petition, action, user);
        } catch (Exception e) {
            String resultMessage = "Erro ao atribuir tarefa.";
            getLogger().error(resultMessage, e);
            return new ActionResponse(resultMessage, false);
        }
    }


    private ActionResponse relocate(PetitionInstance petition, ActionRequest action, SUser author, SUser target) {
        petition.getCurrentTaskOrException().relocateTask(author, target, false, "", action.getLastVersion());
        return new ActionResponse("Tarefa atribuída com sucesso.", true);
    }

    private ActionResponse assign(PetitionInstance petition, ActionRequest action, SUser author) {
        petition.getCurrentTaskOrException().relocateTask(author, author, false, "", action.getLastVersion());
        return new ActionResponse("Tarefa atribuída com sucesso.", true);
    }

    private ActionResponse unassign(PetitionInstance petition, ActionRequest action, SUser author) {
        petition.getCurrentTaskOrException().relocateTask(author, null, false, "", action.getLastVersion());
        return new ActionResponse("Tarefa desalocada com sucesso.", true);
    }

}
