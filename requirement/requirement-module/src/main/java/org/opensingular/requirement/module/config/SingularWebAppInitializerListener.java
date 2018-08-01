package org.opensingular.requirement.module.config;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;

/**
 * Interface que possibilita executar passos de configuração ao subir o contexto do servidor e do spring;
 * Utilizando o {@link ServletContext} é possivel registrar filtros, listeners e servlets.
 * Utilizando o {@link AnnotationConfigWebApplicationContext} é possivel registrar configurações e beans
 *
 * @see SingularInitializer
 * @see AbstractSingularInitializer
 * @see SingularWebAppInitializer
 */
@FunctionalInterface
public interface SingularWebAppInitializerListener {

    /**
     * Método acionado ao inicializer a aplicação
     * @see SingularWebAppInitializer#onStartup(ServletContext)
     */
    void onInitialize(ServletContext servletContext, AnnotationConfigWebApplicationContext applicationContext);
}