/*
 * Copyright (c) 2016, Singular and/or its affiliates. All rights reserved.
 * Singular PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.opensingular.server.commons.flow.controllers;

import org.opensingular.flow.core.MUser;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.spring.util.AutoScanDisabled;
import org.opensingular.server.commons.flow.actions.ActionRequest;
import org.opensingular.server.commons.flow.actions.ActionResponse;
import org.opensingular.server.commons.service.PetitionInstance;
import org.opensingular.server.commons.service.PetitionUtil;
import org.springframework.stereotype.Controller;

import javax.annotation.Nonnull;

import static org.opensingular.server.commons.flow.actions.DefaultActions.ACTION_ASSIGN;

@AutoScanDisabled
@Controller
public class DefaultAssignController extends IController implements Loggable {

    @Override
    public ActionResponse execute(@Nonnull PetitionInstance petition, ActionRequest action) {
        try {
            MUser user = PetitionUtil.findUserOrException(action.getIdUsuario());

            petition.getCurrentTaskOrException().relocateTask(user, user, false, "", action.getLastVersion());
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
