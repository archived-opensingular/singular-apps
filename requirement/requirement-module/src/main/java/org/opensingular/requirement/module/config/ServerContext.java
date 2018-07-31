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

package org.opensingular.requirement.module.config;

import org.opensingular.lib.commons.base.SingularUtil;
import org.opensingular.requirement.module.config.workspace.Workspace;
import org.opensingular.requirement.module.config.workspace.WorkspaceSettings;

/**
 * Utilitário para prover a configuração de contexto atual e os métodos utilitários
 * relacionados.
 */
public abstract class ServerContext implements IServerContext {
    private final String name;
    private WorkspaceSettings settings;
    private Workspace workspace;

    public ServerContext(String name) {
        this.name = name;
    }

    public abstract void configure(WorkspaceSettings settings);

    public abstract void configure(Workspace workspace);

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public WorkspaceSettings getSettings() {
        if (settings != null) {
            return settings;
        }

        String nameJavaIdentity = SingularUtil.convertToJavaIdentity(name, true).toLowerCase();

        settings = new WorkspaceSettings();
        settings
                .contextPath("/" + nameJavaIdentity + "/*")
                .propertiesBaseKey("singular." + nameJavaIdentity)
                .addPublicUrl("/wicket/resource/*")
                .addPublicUrl("/public/*");

        configure(settings);

        return settings;
    }

    @Override
    public Workspace getWorkspace() {
        if (workspace != null) {
            return workspace;
        }

        workspace = new Workspace();
        configure(workspace);

        return workspace;
    }
}