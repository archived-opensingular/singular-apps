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
 * TODO
 *
 * @see SingularInitializer
 * @see SingularServerInitializerProvider
 * @see SingularWebInitializerListener
 */
public class SingularWebApplicationInitializer implements WebApplicationInitializer {
    /**
     * TODO
     */
    private SingularInitializer singularInitializer;

    /**
     * TODO
     */
    public SingularWebApplicationInitializer() {
        this(SingularServerInitializerProvider.get().retrieve());
    }

    /**
     * TODO
     */
    public SingularWebApplicationInitializer(SingularInitializer singularInitializer) {
        this.singularInitializer = singularInitializer;
    }

    /**
     * TODO
     */
    @Override
    public void onStartup(ServletContext ctx) {
        SingularContextSetup.reset();
        AnnotationConfigWebApplicationContext applicationContext = singularInitializer.createApplicationContext();
        singularInitializer.getSingularWebInitializerListener()
                .forEach(singularInitializer -> singularInitializer.initialize(ctx, applicationContext));
    }
}