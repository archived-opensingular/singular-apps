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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.opensingular.requirement.module.config.ServerStartExecutorBean;
import org.opensingular.requirement.module.connector.ModuleService;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.opensingular.requirement.module.SingularModuleConfigurationBean;
import org.opensingular.requirement.module.SingularRequirementRef;

/**
 * Classe para abrigar a lógica de carga
 * de definições de requisição no banco
 * do servidor.
 */
@Named
public class RequirementDefinitionUpdaterExecutor {

    @Inject
    private SingularModuleConfigurationBean singularModuleConfiguration;

    @Inject
    private ServerStartExecutorBean serverStartExecutorBean;

    @Inject
    private ModuleService moduleService;

    @PostConstruct
    public void init() {
        serverStartExecutorBean.register(this::saveAllRequirementDefinitions);
    }

    /**
     * Percorre todos requerimentos contidos na configuração do módulo
     * e os repassa para salvar/recuperar os dados do banco.
     */
    public void saveAllRequirementDefinitions() {
        for (SingularRequirementRef singularRequirementRef : singularModuleConfiguration.getRequirements()) {
            try {
                moduleService.save(singularRequirementRef);
            } catch (Exception e) {
                throw SingularServerException.rethrow(String.format("Erro ao salvar requerimento %s", singularRequirementRef.getRequirement().getName()), e);
            }
        }
    }
}
