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

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.requirement.commons.CommonsInitializerMock;
import org.opensingular.requirement.commons.test.SingularServletContextMock;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletException;

import static org.opensingular.requirement.module.config.SingularInitializer.*;

public class ConfigWithoutSpringTest {

    AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
    SingularServletContextMock            mockServletContext = new SingularServletContextMock();
    CommonsInitializerMock                initializer        = new CommonsInitializerMock(applicationContext);


    @Test
    public void checkServletParams() throws ServletException {
        new SingularInitializer(initializer).onStartup(mockServletContext);
        Assert.assertNotNull(mockServletContext.getAttribute(SERVLET_ATTRIBUTE_WEB_CONFIGURATION));
        Assert.assertNotNull(mockServletContext.getAttribute(SERVLET_ATTRIBUTE_SPRING_HIBERNATE_CONFIGURATION));
        Assert.assertNotNull(mockServletContext.getAttribute(SERVLET_ATTRIBUTE_FORM_CONFIGURATION_CONFIGURATION));
    }
}
