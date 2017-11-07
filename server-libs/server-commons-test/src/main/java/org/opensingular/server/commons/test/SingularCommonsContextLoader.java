/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
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

package org.opensingular.server.commons.test;

import org.opensingular.server.commons.config.SingularInitializer;
import org.springframework.test.context.web.WebMergedContextConfiguration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletException;

public class SingularCommonsContextLoader extends AbstractSingularContextLoader {

    /**
     * Customização para iniciar o singular
     * @param context
     * @param webMergedConfig
     */
    protected void customizeContext(AnnotationConfigWebApplicationContext context, WebMergedContextConfiguration webMergedConfig) {
        try {
            new SingularInitializer( new CommonsInitializerMock(context)).onStartup(context.getServletContext());
        } catch (ServletException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
