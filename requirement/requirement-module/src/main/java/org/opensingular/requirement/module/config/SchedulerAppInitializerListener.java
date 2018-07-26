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

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;

@Deprecated
public abstract class SchedulerAppInitializerListener implements SingularWebAppInitializerListener {
    @Override
    public void onInitialize(ServletContext servletContext, AnnotationConfigWebApplicationContext applicationContext) {
        applicationContext.register(mailConfiguration());
        applicationContext.register(attachmentGCConfiguration());
    }

    public abstract Class<?> mailConfiguration();

    /**
     * @return
     * @deprecated Deveria ter implementação padrão.
     * A implementação padrão deve vir para esse módulo ou o método deve ser removido
     * //TODO danilo.mesquita
     */
    @Deprecated
    public abstract Class<?> attachmentGCConfiguration();
}