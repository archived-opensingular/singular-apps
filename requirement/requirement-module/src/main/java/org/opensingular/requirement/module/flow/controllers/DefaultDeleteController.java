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

package org.opensingular.requirement.module.flow.controllers;

import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.box.action.ActionRequest;
import org.opensingular.requirement.module.box.action.ActionResponse;
import org.opensingular.requirement.module.service.RequirementInstance;
import org.opensingular.requirement.module.service.RequirementService;
import org.springframework.stereotype.Controller;

import javax.annotation.Nonnull;
import javax.inject.Inject;


@Controller
public class DefaultDeleteController extends IController implements Loggable {

    @Inject
    private RequirementService<?, ?> requirementService;

    @Override
    public ActionResponse execute(@Nonnull RequirementInstance requirement, ActionRequest action) {
        try {
            requirementService.deleteRequirement(requirement.getCod());
            return new ActionResponse("Registro excluído com sucesso", true);
        } catch (Exception e) {
            final String msg = "Erro ao excluir o item.";
            getLogger().error(msg, e);
            return new ActionResponse(msg, false);
        }
    }

}
