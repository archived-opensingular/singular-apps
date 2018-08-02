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

import org.opensingular.lib.commons.context.SingularContextSetup;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;

/**
 * Implementação de WebApplicationInitializer que faz o bootstrap do Singular Requerimentos
 *
 * @see SingularInitializer
 * @see AbstractSingularInitializer
 * @see SingularInitializerProvider
 * @see SingularWebAppInitializerListener
 */
public class SingularWebAppInitializer implements WebApplicationInitializer {
    /**
     *
     */
    private SingularInitializer singularInitializer;

    /**
     * Faz a inicialização do {@link AnnotationConfigWebApplicationContext} e aciona todos os
     * {@link SingularWebAppInitializerListener} declarados no {@link SingularInitializer}
     */
    @Override
    public void onStartup(ServletContext ctx) {
        if (singularInitializer == null) {
            singularInitializer = resolveSingularInitializer();
        }
        SingularContextSetup.reset();
        AnnotationConfigWebApplicationContext applicationContext = singularInitializer.createApplicationContext();
        singularInitializer.getSingularWebInitializerListener().forEach(listener -> listener.onInitialize(ctx, applicationContext));
    }

    /**
     * Faz a busca pela implementação de {@link SingularInitializer}, um projeto utilizando Singular Requerimentos,
     * deve conter apenas uma implementação desta interface em seu classpath.
     * Utiliza o {@link SingularInitializerProvider} para fazer a busca.
     */
    @SuppressWarnings("WeakerAccess")
    protected SingularInitializer resolveSingularInitializer() {
        return SingularInitializerProvider.retrieve();
    }

    /**
     * Permite a sobreescrita do {@link SingularInitializer}, util em testes
     */
    public SingularWebAppInitializer setSingularInitializer(SingularInitializer singularInitializer) {
        this.singularInitializer = singularInitializer;
        return this;
    }
}