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

package org.opensingular.requirement.commons.test;

import org.opensingular.lib.commons.util.Loggable;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.ServletContext;

public class SingularServletContextTestExecutionListener extends AbstractTestExecutionListener implements Loggable {

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        setUpRequestContext(testContext);

    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        setUpRequestContext(testContext);
    }


    private void setUpRequestContext(TestContext testContext) {

        ApplicationContext context = testContext.getApplicationContext();

        if (context instanceof WebApplicationContext) {
            WebApplicationContext wac = (WebApplicationContext) context;
            ServletContext servletContext = wac.getServletContext();


            MockServletContext mockServletContext = (MockServletContext) servletContext;
            MockHttpServletRequest request = new MockHttpServletRequest(mockServletContext);
            ContextUtil.prepareRequest(request);

            MockHttpServletResponse response = new MockHttpServletResponse();
            ServletWebRequest servletWebRequest = new ServletWebRequest(request, response);

            RequestContextHolder.setRequestAttributes(servletWebRequest);

            if (wac instanceof ConfigurableApplicationContext) {
                @SuppressWarnings("resource")
                ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) wac;
                ConfigurableListableBeanFactory bf = configurableApplicationContext.getBeanFactory();
                bf.registerResolvableDependency(MockHttpServletResponse.class, response);
                bf.registerResolvableDependency(ServletWebRequest.class, servletWebRequest);
            }
        }
    }
}