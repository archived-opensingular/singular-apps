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

package org.opensingular.server.commons.rest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.lib.support.spring.util.AutoScanDisabled;
import org.opensingular.server.commons.flow.actions.ActionConfig;
import org.opensingular.server.commons.flow.actions.ActionRequest;
import org.opensingular.server.commons.flow.actions.ActionResponse;
import org.opensingular.server.commons.flow.controllers.IController;
import org.opensingular.server.commons.persistence.dto.TaskInstanceDTO;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.PetitionInstance;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.spring.security.AuthorizationService;
import org.opensingular.server.commons.spring.security.PermissionResolverService;
import org.opensingular.server.commons.spring.security.SingularPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static org.opensingular.server.commons.flow.actions.DefaultActions.ACTION_DELETE;
import static org.opensingular.server.commons.service.IServerMetadataREST.PATH_BOX_SEARCH;

/**
 * Essa interface deve ser protegida de forma que apenas o próprio servidor possa
 * acessar seus métodos.
 */
@AutoScanDisabled
@RequestMapping("/rest/flow")
@RestController
public class DefaultServerREST {

    public static final String PATH_BOX_ACTION = "/box/action";
    public static final String DELETE = "/delete";
    public static final String EXECUTE = "/executar";
    public static final String USERS = "/listarUsuarios";

    static final Logger LOGGER = LoggerFactory.getLogger(DefaultServerREST.class);

    @Inject
    protected PetitionService<PetitionEntity, PetitionInstance> petitionService;

    @Inject
    protected PermissionResolverService permissionResolverService;

    @Inject
    protected AuthorizationService authorizationService;

    @RequestMapping(value = PATH_BOX_ACTION + DELETE, method = RequestMethod.POST)
    public ActionResponse excluir(@RequestParam Long id, @RequestBody ActionRequest actionRequest) {
        try {
            boolean hasPermission = authorizationService.hasPermission(id, null, actionRequest.getIdUsuario(), ACTION_DELETE.getName());
            if (hasPermission) {
                petitionService.deletePetition(id);
                return new ActionResponse("Registro excluído com sucesso", true);
            } else {
                return new ActionResponse("Você não tem permissão para executar esta ação.", false);
            }
        } catch (Exception e) {
            final String msg = "Erro ao excluir o item.";
            LOGGER.error(msg, e);
            return new ActionResponse(msg, false);
        }
    }

    @RequestMapping(value = PATH_BOX_ACTION + EXECUTE, method = RequestMethod.POST)
    public ActionResponse executar(@RequestParam Long id, @RequestBody ActionRequest actionRequest) {
        try {
            PetitionInstance petition = petitionService.getPetition(id);
            ProcessDefinition<?> processDefinition = petition.getProcessDefinition();

            IController controller = getActionController(processDefinition, actionRequest);
            return controller.run(petition, actionRequest);
        } catch (Exception e) {
            final String msg = String.format("Erro ao executar a ação %s para o id %d. ", StringEscapeUtils.escapeJava(actionRequest.getName()), id);
            LOGGER.error(msg, e);
            return new ActionResponse(msg, false);
        }

    }

    private IController getActionController(ProcessDefinition<?> processDefinition, ActionRequest actionRequest) {
        final ActionConfig actionConfig = processDefinition.getMetaDataValue(ActionConfig.KEY);
        Class<? extends IController> controllerClass = actionConfig.getAction(actionRequest.getName());
        if (ApplicationContextProvider.get().containsBean(controllerClass.getName())) {
            return ApplicationContextProvider.get().getBean(controllerClass);
        } else {
            return ApplicationContextProvider.get().getAutowireCapableBeanFactory().createBean(controllerClass);
        }
    }

}