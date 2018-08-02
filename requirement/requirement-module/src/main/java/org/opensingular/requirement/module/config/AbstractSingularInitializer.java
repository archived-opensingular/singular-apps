package org.opensingular.requirement.module.config;

import org.opensingular.app.commons.spring.persistence.SingularPersistenceDefaultBeanFactory;
import org.opensingular.lib.support.spring.security.DefaultRestSecurity;
import org.opensingular.lib.support.spring.util.SingularAnnotationConfigWebApplicationContext;
import org.opensingular.requirement.module.WorkspaceAppInitializerListener;
import org.opensingular.requirement.module.spring.SingularDefaultBeanFactory;
import org.opensingular.requirement.module.spring.SingularServerSpringAppConfig;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementação do SingularInitializer que os valores defaults para iniciar o SingularRequerimentos
 *
 * @see SingularInitializer
 * @see SingularInitializerProvider
 * @see SingularWebAppInitializerListener
 * @see SingularWebAppInitializer
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractSingularInitializer implements SingularInitializer {
    /**
     * Cria o {@link AnnotationConfigWebApplicationContext} raiz
     */
    @Override
    public AnnotationConfigWebApplicationContext createApplicationContext() {
        SingularAnnotationConfigWebApplicationContext applicationContext;
        applicationContext = new SingularAnnotationConfigWebApplicationContext();
        applicationContext.scan(getSpringPackagesToScan());
        return applicationContext;
    }

    /**
     * Pacotes a serem scaniados automaticamente pelo Spring
     */
    protected abstract String[] getSpringPackagesToScan();

    /**
     * Lista as classes de configuração que serão registradas no {@link AnnotationConfigWebApplicationContext#register(Class[])}
     */
    protected List<Class<?>> getSpringAnnotatedConfigurationClasses() {
        List<Class<?>> annotatedClasses = new ArrayList<>();
        annotatedClasses.add(getSingularSpringAppConfigClass());
        annotatedClasses.add(getSingularBeanFactoryClass());
        annotatedClasses.add(getSingularPersistenceConfigurationBeanFactoryClass());
        annotatedClasses.add(getSingularSpringWebMVCConfigClass());
        annotatedClasses.add(getSingularRestSecurityConfigClass());
        return annotatedClasses;
    }

    /**
     * Recupera o timeout da sessão web em minutos
     */
    protected int getSessionTimeoutMinutes() {
        return 15;
    }

    /**
     * Cria o {@link WorkspaceAppInitializerListener}
     */
    public WorkspaceAppInitializerListener newWorkspaceInitializerListener() {
        return new WorkspaceAppInitializerListener();
    }

    /**
     * Cria o {@link SpringConfigRegisterSingularWebAppInitializerListener}
     */
    private SpringConfigRegisterSingularWebAppInitializerListener newSpringConfigRegisterSingularWebInitializerListener() {
        return new SpringConfigRegisterSingularWebAppInitializerListener(getSpringAnnotatedConfigurationClasses());
    }

    /**
     * Cria o {@link ServletContextSetupSingularWebAppInitializerListener}
     */
    private ServletContextSetupSingularWebAppInitializerListener newServletContextSetupSingularWebInitializerListener() {
        return new ServletContextSetupSingularWebAppInitializerListener(getSessionTimeoutMinutes());
    }

    /**
     * Fornece a classe que será utilizada como configuração java do Spring.
     * A classe fornecida deve herdar de {@link SingularServerSpringAppConfig} e deve
     * ser anotada com {@link org.springframework.context.annotation.Configuration}.
     * As principais configurações do pet server são feitas pela superclasse bastando declarar
     * na classe informada apenas as configurações e beans do spring específicos da aplicação
     *
     * @return Uma classe concreta que herda de {@link SingularServerSpringAppConfig}
     * e anotada com {@link org.springframework.context.annotation.Configuration}
     */
    protected Class<? extends SingularServerSpringAppConfig> getSingularSpringAppConfigClass() {
        return SingularServerSpringAppConfig.class;
    }

    /**
     * Recupera a configuração de persistencia
     *
     * @see SingularPersistenceDefaultBeanFactory
     */
    protected Class<? extends SingularPersistenceDefaultBeanFactory> getSingularPersistenceConfigurationBeanFactoryClass() {
        return SingularPersistenceDefaultBeanFactory.class;
    }

    /**
     * Recupera a configuração dos beans
     *
     * @see SingularDefaultBeanFactory
     */
    protected Class<? extends SingularDefaultBeanFactory> getSingularBeanFactoryClass() {
        return SingularDefaultBeanFactory.class;
    }

    /**
     * Recupera a configuração do SpringMVC
     *
     * @see SingularSpringWebMVCConfig
     */
    public Class<? extends SingularSpringWebMVCConfig> getSingularSpringWebMVCConfigClass() {
        return SingularSpringWebMVCConfig.class;
    }

    /**
     * Recupera a configuração de segurança dos serviços REST
     *
     * @see WebSecurityConfigurerAdapter
     */
    public Class<? extends WebSecurityConfigurerAdapter> getSingularRestSecurityConfigClass() {
        return DefaultRestSecurity.class;
    }

    /**
     * Lista os initializer que serão executados pelo {@link SingularWebAppInitializer}
     */
    @Override
    public List<? extends SingularWebAppInitializerListener> getSingularWebInitializerListener() {
        List<SingularWebAppInitializerListener> initializerListeners = new ArrayList<>();
        initializerListeners.add(newSpringConfigRegisterSingularWebInitializerListener());
        initializerListeners.add(newServletContextSetupSingularWebInitializerListener());
        initializerListeners.add(newWorkspaceInitializerListener());
        return initializerListeners;
    }
}