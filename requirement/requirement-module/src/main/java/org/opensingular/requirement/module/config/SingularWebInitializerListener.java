package org.opensingular.requirement.module.config;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;

/**
 * TODO
 *
 * @see SingularInitializer
 * @see AbstractSingularInitializer
 * @see SingularWebApplicationInitializer
 */
@FunctionalInterface
public interface SingularWebInitializerListener {

    /**
     * TODO
     */
    void initialize(ServletContext servletContext, AnnotationConfigWebApplicationContext applicationContext);
}