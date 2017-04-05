package org.opensingular.server.commons.config;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opensingular.server.commons.test.SingularMockServletContext;
import org.opensingular.server.commons.test.SingularServerTestBase;
import org.opensingular.server.commons.test.TestInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.inject.Inject;
import javax.servlet.ServletException;

import static org.opensingular.server.commons.config.SingularInitializer.SERVLET_ATTRIBUTE_FORM_CONFIGURATION_CONFIGURATION;
import static org.opensingular.server.commons.config.SingularInitializer.SERVLET_ATTRIBUTE_SPRING_HIBERNATE_CONFIGURATION;
import static org.opensingular.server.commons.config.SingularInitializer.SERVLET_ATTRIBUTE_WEB_CONFIGURATION;

public class ConfigTest extends SingularServerTestBase {

    @Inject
    public SingularServerConfiguration singularServerConfiguration;

    @Test
    public void checkServletParams() throws ServletException {

        Assert.assertEquals(singularServerConfiguration.getContexts().length, 3);
        Assert.assertNotNull(singularServerConfiguration.getDefaultPublicUrls());
        Assert.assertArrayEquals(singularServerConfiguration.getDefinitionsPackages(), TestInitializer.DEFINITIONS_PACKS_ARRAY);
        Assert.assertNotNull(singularServerConfiguration.getFormTypes());
        Assert.assertEquals(singularServerConfiguration.getProcessGroupCod(), TestInitializer.TESTE);
        Assert.assertEquals(singularServerConfiguration.getSpringMVCServletMapping(), TestInitializer.SPRING_MVC_SERVLET_MAPPING);
        singularServerConfiguration.setAttribute("teste", "teste");
        Assert.assertEquals(singularServerConfiguration.getAttribute("teste"), "teste");

    }
}
