package org.opensingular.requirement.module.config;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * Initializer que registra as configurações do spring utilizando {@link AnnotationConfigWebApplicationContext#register(Class[])}
 */
public class SpringConfigRegisterSingularWebAppInitializerListener implements SingularWebAppInitializerListener {
    private final List<Class<?>> annotatedClasses;

    public SpringConfigRegisterSingularWebAppInitializerListener(List<Class<?>> annotatedClasses) {
        this.annotatedClasses = annotatedClasses;
    }

    @Override
    public void onInitialize(ServletContext servletContext, AnnotationConfigWebApplicationContext applicationContext) {
        annotatedClasses.forEach(applicationContext::register);
    }
}
