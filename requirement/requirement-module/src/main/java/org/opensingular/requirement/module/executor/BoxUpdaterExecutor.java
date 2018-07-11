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

package org.opensingular.requirement.module.executor;

import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.config.ServerStartExecutorBean;
import org.opensingular.requirement.module.config.SingularServerConfiguration;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.opensingular.requirement.module.persistence.entity.form.BoxEntity;
import org.opensingular.requirement.module.service.dto.BoxDefinitionData;
import org.opensingular.requirement.module.BoxController;
import org.opensingular.requirement.module.SingularModuleConfiguration;
import org.opensingular.requirement.module.service.BoxService;
import org.opensingular.requirement.module.service.ModuleService;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Classe para abrigar a lógica de carga inicial
 * de caixas no banco do servidor.
 */
@Named
public class BoxUpdaterExecutor {

    @Inject
    private SingularModuleConfiguration singularModuleConfiguration;

    @Inject
    private BoxService boxService;

    @Inject
    private ModuleService moduleService;

    @Inject
    private ServerStartExecutorBean serverStartExecutorBean;

    @Inject
    private SingularServerConfiguration singularServerConfiguration;

    @PostConstruct
    public void init() {
        serverStartExecutorBean.register(this::saveAllBoxDefinitions);
    }

    /**
     * Percorre todos requerimentos contidos na configuração do módulo
     * e os repassa para salvar/recuperar os dados do banco.
     */
    public void saveAllBoxDefinitions() {
        ModuleEntity module = moduleService.getModule();
        for (IServerContext context : singularServerConfiguration.getContexts()) {
            List<BoxController> boxControllers = singularModuleConfiguration.getBoxControllerByContext(context);
            for (BoxController boxController : boxControllers) {
                BoxDefinitionData boxData = singularModuleConfiguration.buildBoxDefinitionData(boxController, context);
                try {
                    BoxEntity boxEntity = boxService.saveBoxDefinition(module, boxData);
                    boxController.setId(boxEntity.getCod().toString());
                } catch (Exception e) {
                    throw SingularServerException.rethrow(String.format("Erro ao salvar a caixa %s", boxData.getItemBox().getName()), e);
                }
            }
        }
    }

}
