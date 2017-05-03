package org.opensingular.server.commons.config;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.opensingular.server.commons.test.CommonsInitializerMock;

import javax.inject.Inject;
import javax.servlet.ServletException;

public class ConfigTest extends SingularCommonsBaseTest {

    @Inject
    public SingularServerConfiguration singularServerConfiguration;

    @Test
    public void checkServletParams() throws ServletException {

        Assert.assertEquals(singularServerConfiguration.getContexts().length, 3);
        Assert.assertNotNull(singularServerConfiguration.getDefaultPublicUrls());
        Assert.assertNotNull(singularServerConfiguration.getFormTypes());
        Assert.assertEquals(singularServerConfiguration.getProcessGroupCod(), CommonsInitializerMock.TESTE);
        Assert.assertEquals(singularServerConfiguration.getSpringMVCServletMapping(), CommonsInitializerMock.SPRING_MVC_SERVLET_MAPPING);
        singularServerConfiguration.setAttribute("teste", "teste");
        Assert.assertEquals(singularServerConfiguration.getAttribute("teste"), "teste");

    }
}
