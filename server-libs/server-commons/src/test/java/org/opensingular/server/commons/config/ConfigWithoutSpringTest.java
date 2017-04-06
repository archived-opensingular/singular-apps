package org.opensingular.server.commons.config;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.server.commons.test.SingularServletContextMock;
import org.opensingular.server.commons.test.SingularInitializerMock;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletException;

import static org.opensingular.server.commons.config.SingularInitializer.SERVLET_ATTRIBUTE_FORM_CONFIGURATION_CONFIGURATION;
import static org.opensingular.server.commons.config.SingularInitializer.SERVLET_ATTRIBUTE_SPRING_HIBERNATE_CONFIGURATION;
import static org.opensingular.server.commons.config.SingularInitializer.SERVLET_ATTRIBUTE_WEB_CONFIGURATION;

public class ConfigWithoutSpringTest {

    AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
    SingularServletContextMock mockServletContext = new SingularServletContextMock();
    SingularInitializerMock    initializer        = new SingularInitializerMock(applicationContext);


    @Test
    public void checkServletParams() throws ServletException {
        initializer.onStartup(mockServletContext);
        Assert.assertNotNull(mockServletContext.getAttribute(SERVLET_ATTRIBUTE_WEB_CONFIGURATION));
        Assert.assertNotNull(mockServletContext.getAttribute(SERVLET_ATTRIBUTE_SPRING_HIBERNATE_CONFIGURATION));
        Assert.assertNotNull(mockServletContext.getAttribute(SERVLET_ATTRIBUTE_FORM_CONFIGURATION_CONFIGURATION));
    }
}
