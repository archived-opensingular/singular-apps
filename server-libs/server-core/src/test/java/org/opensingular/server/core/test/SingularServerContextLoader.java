package org.opensingular.server.core.test;


import javax.servlet.ServletException;

import org.opensingular.server.commons.config.SingularInitializer;
import org.opensingular.server.commons.test.AbstractSingularContextLoader;
import org.opensingular.server.commons.test.CommonsInitializerMock;
import org.springframework.test.context.web.WebMergedContextConfiguration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class SingularServerContextLoader extends AbstractSingularContextLoader {


    @Override
    protected void customizeContext(AnnotationConfigWebApplicationContext context, WebMergedContextConfiguration webMergedConfig) {
        try {
            new SingularInitializer(new ServerInitializerMock(context)).onStartup(context.getServletContext());
        } catch (ServletException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
