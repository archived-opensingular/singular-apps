package org.opensingular.server.commons.config;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opensingular.server.commons.test.SingularMockServletContext;
import org.opensingular.server.commons.test.TestInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletException;

import static org.opensingular.server.commons.config.SingularInitializer.SERVLET_ATTRIBUTE_FORM_CONFIGURATION_CONFIGURATION;
import static org.opensingular.server.commons.config.SingularInitializer.SERVLET_ATTRIBUTE_SPRING_HIBERNATE_CONFIGURATION;
import static org.opensingular.server.commons.config.SingularInitializer.SERVLET_ATTRIBUTE_WEB_CONFIGURATION;

public class ConfigTest {

    AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
    SingularMockServletContext            mockServletContext = new SingularMockServletContext();
    TestInitializer                       initializer        = new TestInitializer(applicationContext);

    @Before
    public void testConfigSpike() throws ServletException {
        initializer.onStartup(mockServletContext);
    }

    @Test
    public void checkServletParams() {
        Assert.assertNotNull(mockServletContext.getAttribute(SERVLET_ATTRIBUTE_WEB_CONFIGURATION));
        Assert.assertNotNull(mockServletContext.getAttribute(SERVLET_ATTRIBUTE_SPRING_HIBERNATE_CONFIGURATION));
        Assert.assertNotNull(mockServletContext.getAttribute(SERVLET_ATTRIBUTE_FORM_CONFIGURATION_CONFIGURATION));
    }
}
