package org.opensingular.requirement.module.config;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.List;

/**
 * TODO
 *
 * @see SingularWebApplicationInitializer
 */
public interface SingularInitializer {
    /**
     * TODO
     */
    AnnotationConfigWebApplicationContext createApplicationContext();

    /**
     * TODO
     */
    List<? extends SingularWebInitializerListener> getSingularWebInitializerListener();
}