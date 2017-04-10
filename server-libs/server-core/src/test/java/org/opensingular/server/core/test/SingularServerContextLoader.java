package org.opensingular.server.core.test;


import org.opensingular.server.commons.test.SingularCommonsContextLoader;
import org.springframework.test.context.web.WebMergedContextConfiguration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletException;

public class SingularServerContextLoader extends SingularCommonsContextLoader {


    @Override
    protected void customizeContext(AnnotationConfigWebApplicationContext context, WebMergedContextConfiguration webMergedConfig) {
        try {
            new ServerInitializerMock(context).onStartup(context.getServletContext());
        } catch (ServletException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
