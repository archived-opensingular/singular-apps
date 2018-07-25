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

import java.util.EventListener;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;

import org.mockito.Mockito;
import org.opensingular.lib.commons.util.Loggable;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockServletContext;

public class SingularServletContextMock extends MockServletContext implements Loggable {


    public SingularServletContextMock(String resourceBasePath, ResourceLoader resourceLoader) {
        super(resourceBasePath, resourceLoader);
    }

    public SingularServletContextMock() {
    }

    @Override
    public void addListener(Class<? extends EventListener> listenerClass) {
        addListener(listenerClass.getName());
    }

    @Override
    public String getVirtualServerName() {
        return null;
    }

    @Override
    public void addListener(String className) {
        getLogger().info("addListener class : {}", className);
    }

    @Override
    public <T extends EventListener> void addListener(T t) {
        addListener(t.getClass());
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, String className) {
        getLogger().info("addFilter name: {} class: {}", filterName, className);
        return Mockito.mock(FilterRegistration.Dynamic.class);
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
        return addFilter(filterName, filter.getClass());
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
        return addFilter(filterName, filterClass.getName());
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, String className) {
        getLogger().info("addServlet name: {} class: {}", servletName, className);
        return Mockito.mock(ServletRegistration.Dynamic.class);
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
        return  addServlet(servletName, servletClass.getName());
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
        return addServlet(servletName, servlet.getClass());
    }


}
