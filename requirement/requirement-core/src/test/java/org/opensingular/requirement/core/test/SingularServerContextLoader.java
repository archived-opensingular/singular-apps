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

package org.opensingular.requirement.core.test;


import javax.servlet.ServletException;

import org.opensingular.requirement.commons.config.SingularInitializer;

import org.opensingular.server.commons.test.AbstractSingularContextLoader;
import org.springframework.test.context.web.WebMergedContextConfiguration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class SingularServerContextLoader extends AbstractSingularContextLoader {


    @Override
    protected void customizeContext(AnnotationConfigWebApplicationContext context, WebMergedContextConfiguration webMergedConfig) {
        try {
            new SingularInitializer(new ServerInitializerMock(context)).onStartup(context.getServletContext());
        } catch (ServletException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
