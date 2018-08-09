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

package org.opensingular.requirement.module.config;

import org.opensingular.requirement.module.config.workspace.Workspace;
import org.opensingular.requirement.module.config.workspace.WorkspaceSettings;

import java.io.Serializable;

/**
 * Utilitário para prover a configuração de contexto atual e os métodos utilitários
 * relacionados.
 */
public interface IServerContext extends Serializable {
    /**
     * @return the name of the context
     */
    String getName();

    /**
     * @return the workspace settings of the context
     */
    WorkspaceSettings getSettings();

    /**
     * @return the workspace of the context
     */
    Workspace getWorkspace();

    /**
     * @return the base key concatenated with the property
     */
    default String getServerPropertyKey(String basePropertyKey) {
        return getSettings().getPropertiesBaseKey() + "." + basePropertyKey;
    }
}