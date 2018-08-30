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

package org.opensingular.requirement.module.workspace;

import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.exception.SingularServerException;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The WorkspaceRegistry, where the contexts should be added
 *
 * @see org.opensingular.requirement.module.SingularModule
 * @see org.opensingular.requirement.module.WorkspaceAppInitializerListener
 */
public class WorkspaceRegistry implements Loggable {
    private final Set<IServerContext> contexts = new LinkedHashSet<>();

    /**
     * Add a contexts to the registry
     * @param serverContextClass the context class
     * @return the current registry
     */
    public WorkspaceRegistry add(Class<? extends IServerContext> serverContextClass) {
        try {
            contexts.add(serverContextClass.newInstance());
        } catch (InstantiationException | IllegalAccessException ex) {
            getLogger().error(ex.getMessage(), ex);
            throw SingularServerException.rethrow("Não foi possivel criar uma instancia de " + serverContextClass, ex);
        }
        return this;
    }

    /**
     * @return all contexts added to this registry
     */
    public Set<IServerContext> getContexts() {
        return contexts;
    }
}