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

import org.apache.wicket.Application;
import org.apache.wicket.request.Request;
import org.opensingular.requirement.module.WorkspaceConfiguration;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.opensingular.requirement.module.spring.security.AbstractSingularSpringSecurityAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Utilitário para prover a configuração de contexto atual e os métodos utilitários
 * relacionados.
 */
public interface IServerContext extends Serializable {

    static IServerContext getContextFromRequest(Request request, Collection<IServerContext> contexts) {
        return getContextFromRequest((HttpServletRequest) request.getContainerRequest(), contexts);
    }

    static IServerContext getContextFromName(String name, Collection<IServerContext> contexts) {
        for (IServerContext ctx : contexts) {
            if (name.equals(ctx.getName())) {
                return ctx;
            }
        }
        throw SingularServerException.rethrow("Não foi possível determinar o contexto do servidor do singular");
    }

    static IServerContext getContextFromRequest(HttpServletRequest request, Collection<IServerContext> contexts) {
        String contextPath = request.getContextPath();
        String context = request.getPathInfo().replaceFirst(contextPath, "");
        for (IServerContext ctx : contexts) {
            if (context.startsWith(ctx.getUrlPath())) {
                return ctx;
            }
        }
        throw SingularServerException.rethrow("Não foi possível determinar o contexto do servidor do singular");
    }

    default String getServerPropertyKey(String basePropertyKey) {
        return getPropertiesBaseKey() + "." + basePropertyKey;
    }

    /**
     * O contexto no formato aceito por servlets e filtros
     */
    String getContextPath();

    /**
     * Conversao do formato aceito por servlets e filtros (contextPath) para java regex
     */
    String getPathRegex();

    /**
     * Conversao do formato aceito por servlets e filtros (contextPath) para um formato de url
     * sem a / ao final.
     */
    String getUrlPath();

    /**
     * Informa o nome teste contexto
     */
    String getName();

    /**
     * Informa a aplicação wicket deste contexto
     */
    Class<? extends Application> getWicketApplicationClass();

    /**
     * Informa a configuração do spring security para este contexto
     */
    Class<? extends AbstractSingularSpringSecurityAdapter> getSpringSecurityConfigClass();

    /**
     * Informa se os requerimentos que passarem por esse contexto deve ter seus owners checkados
     *
     * @deprecated API_REVIEW
     */
    @Deprecated
    default boolean checkOwner() {
        return false;
    }

    /**
     *
     */
    @Deprecated
    String getPropertiesBaseKey();

    /**
     * Contexts public urls
     *
     * @return
     */
    default List<String> getPublicUrls() {
        ArrayList<String> urls = new ArrayList<>();
        urls.add(getUrlPath() + "/wicket/resource/*");
        urls.add(getUrlPath() + "/public/*");
        return urls;
    }

    /**
     * Configura o workspace, adicionando caixas e outros itens
     */
    void setup(WorkspaceConfiguration workspaceConfiguration);
}
