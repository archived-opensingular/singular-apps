package org.opensingular.server.commons.test;

import org.mockito.Mockito;
import org.opensingular.lib.commons.util.Loggable;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockServletContext;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;
import java.util.EventListener;

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
