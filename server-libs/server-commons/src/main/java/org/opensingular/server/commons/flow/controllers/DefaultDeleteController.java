/*
 * Copyright (c) 2016, Singular and/or its affiliates. All rights reserved.
 * Singular PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.opensingular.server.commons.flow.controllers;

import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.box.action.ActionRequest;
import org.opensingular.server.commons.box.action.ActionResponse;
import org.opensingular.server.commons.service.PetitionInstance;
import org.opensingular.server.commons.service.PetitionService;
import org.springframework.stereotype.Controller;

import javax.annotation.Nonnull;
import javax.inject.Inject;


@Controller
public class DefaultDeleteController extends IController implements Loggable {

    @Inject
    private PetitionService<?, ?> petitionService;

    @Override
    public ActionResponse execute(@Nonnull PetitionInstance petition, ActionRequest action) {
        try {
            petitionService.deleteRequirement(petition.getCod());
            return new ActionResponse("Registro excluído com sucesso", true);
        } catch (Exception e) {
            final String msg = "Erro ao excluir o item.";
            getLogger().error(msg, e);
            return new ActionResponse(msg, false);
        }
    }

}
