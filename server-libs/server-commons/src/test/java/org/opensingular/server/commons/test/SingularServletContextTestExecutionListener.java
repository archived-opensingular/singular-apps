package org.opensingular.server.commons.test;

import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.config.ServerContext;
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


    public static final String CONTEXT_PATH = "/singular";

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
            getLogger().info("Configurando Request Context Path: " + CONTEXT_PATH);
            request.setContextPath(CONTEXT_PATH);
            String pathInfo = "/singular" + ServerContext.WORKLIST.getUrlPath();
            getLogger().info("Configurando Request Path Info: " + pathInfo);
            request.setPathInfo(pathInfo);

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