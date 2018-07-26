package org.opensingular.requirement.module.config;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.List;

/**
 * SingularInitializer responsavel por distribuir os elementos necessarios para a inicialização do singular
 *  *
 * @see SingularWebAppInitializer
 */
public interface SingularInitializer {
    /**
     * Cria o AnnotationConfigWebApplicationContext inicial do Singular
     */
    AnnotationConfigWebApplicationContext createApplicationContext();

    /**
     * Lista as implementações de SingularWebAppInitializerListener que serão execeutadas pelo
     * {@link SingularWebAppInitializer}
     */
    List<? extends SingularWebAppInitializerListener> getSingularWebInitializerListener();
}