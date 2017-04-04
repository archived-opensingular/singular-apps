package org.opensingular.server.commons.test;

import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebMergedContextConfiguration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

import javax.servlet.ServletException;

public class SingularAnnotationConfigWebContextLoader extends AnnotationConfigWebContextLoader {

    @Override
    protected void customizeContext(GenericWebApplicationContext context, WebMergedContextConfiguration webMergedConfig) {
        AnnotationConfigWebApplicationContext config      = new AnnotationConfigWebApplicationContext();
        TestInitializer                       initializer = new TestInitializer(config);
        try {
            initializer.onStartup(new SingularMockServletContext());
        } catch (ServletException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        config.getBeanFactory().freezeConfiguration();
        for (String beanName : config.getBeanDefinitionNames()) {
            context.registerBeanDefinition(beanName, config.getBeanFactory().getBeanDefinition(beanName));
        }
    }
}
