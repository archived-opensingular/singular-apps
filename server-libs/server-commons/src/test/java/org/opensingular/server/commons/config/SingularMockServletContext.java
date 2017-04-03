package org.opensingular.server.commons.config;

import org.mockito.Mockito;
import org.opensingular.lib.commons.util.Loggable;
import org.springframework.mock.web.MockServletContext;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;
import java.util.EventListener;

public class SingularMockServletContext extends MockServletContext implements Loggable {


    @Override
    public void addListener(Class<? extends EventListener> listenerClass) {
        addListener(listenerClass.getName());
    }

    @Override
    public void addListener(String className) {
        getLogger().info("addListener class : %s", className);
    }

    @Override
    public <T extends EventListener> void addListener(T t) {
        addListener(t.getClass());
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, String className) {
        getLogger().info("addFilter name: %s class: %s", filterName, className);
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
        getLogger().info("addServlet name: %s class: %s", servletName, className);
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
