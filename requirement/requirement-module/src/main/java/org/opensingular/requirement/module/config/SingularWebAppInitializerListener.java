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

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;

/**
 * Interface que possibilita executar passos de configuração ao subir o contexto do servidor e do spring;
 * Utilizando o {@link ServletContext} é possivel registrar filtros, listeners e servlets.
 * Utilizando o {@link AnnotationConfigWebApplicationContext} é possivel registrar configurações e beans
 *
 * @see SingularInitializer
 * @see AbstractSingularInitializer
 * @see SingularWebAppInitializer
 */
@FunctionalInterface
public interface SingularWebAppInitializerListener {

    /**
     * Método acionado ao inicializer a aplicação
     * @see SingularWebAppInitializer#onStartup(ServletContext)
     */
    void onInitialize(ServletContext servletContext, AnnotationConfigWebApplicationContext applicationContext);
}