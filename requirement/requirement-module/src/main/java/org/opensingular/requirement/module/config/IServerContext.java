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

import org.apache.wicket.request.Request;
import org.opensingular.requirement.module.config.workspace.Workspace;
import org.opensingular.requirement.module.config.workspace.WorkspaceSettings;
import org.opensingular.requirement.module.exception.SingularServerException;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collection;

/**
 * Utilitário para prover a configuração de contexto atual e os métodos utilitários
 * relacionados.
 */
public interface IServerContext extends Serializable {

    /**
     * API_VIEW
     */
    @Deprecated
    static IServerContext getContextFromRequest(Request request, Collection<IServerContext> contexts) {
        return getContextFromRequest((HttpServletRequest) request.getContainerRequest(), contexts);
    }

    /**
     * API_VIEW
     */
    @Deprecated
    static IServerContext getContextFromName(String name, Collection<IServerContext> contexts) {
        for (IServerContext ctx : contexts) {
            if (name.equals(ctx.getName())) {
                return ctx;
            }
        }
        throw SingularServerException.rethrow("Não foi possível determinar o contexto do servidor do singular");
    }

    /**
     * API_VIEW
     */
    @Deprecated
    static IServerContext getContextFromRequest(HttpServletRequest request, Collection<IServerContext> contexts) {
        String contextPath = request.getContextPath();
        String context = request.getPathInfo().replaceFirst(contextPath, "");
        for (IServerContext ctx : contexts) {
            if (context.startsWith(ctx.getSettings().getUrlPath())) {
                return ctx;
            }
        }
        throw SingularServerException.rethrow("Não foi possível determinar o contexto do servidor do singular");
    }

    /**
     * API_VIEW
     */
    @Deprecated
    default String getServerPropertyKey(String basePropertyKey) {
        return getSettings().getPropertiesBaseKey() + "." + basePropertyKey;
    }

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
}