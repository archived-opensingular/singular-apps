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

package org.opensingular.server.module.executor;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.server.commons.config.ServerStartExecutorBean;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.service.dto.BoxDefinitionData;
import org.opensingular.server.module.SingularModuleConfiguration;
import org.opensingular.server.module.service.BoxService;
import org.opensingular.server.module.service.ModuleService;
import org.opensingular.server.p.commons.config.PServerContext;

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
        for (PServerContext context : PServerContext.values()) {
            List<BoxDefinitionData> boxDefinitionData = singularModuleConfiguration.buildItemBoxes(context);

            for (BoxDefinitionData boxData : boxDefinitionData) {
                try {
                    boxService.saveBoxDefinition(module, boxData);
                } catch (Exception e) {
                    throw SingularServerException.rethrow(String.format("Erro ao salvar a caixa %s", boxData.getItemBox().getName()), e);
                }
            }
        }
    }

}
