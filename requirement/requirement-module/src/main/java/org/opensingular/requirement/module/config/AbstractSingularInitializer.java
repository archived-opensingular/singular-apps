package org.opensingular.requirement.module.config;

import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.opensingular.app.commons.spring.persistence.SingularPersistenceDefaultBeanFactory;
import org.opensingular.form.wicket.mapper.attachment.upload.servlet.FileUploadServlet;
import org.opensingular.form.wicket.mapper.attachment.upload.servlet.strategy.SimplePostFilesStrategy;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.commons.lambda.IConsumer;
import org.opensingular.lib.support.spring.security.DefaultRestSecurity;
import org.opensingular.lib.support.spring.util.SingularAnnotationConfigWebApplicationContext;
import org.opensingular.lib.wicket.util.application.SkinnableApplication;
import org.opensingular.lib.wicket.util.template.SkinOptions;
import org.opensingular.requirement.module.WorkspaceInitializer;
import org.opensingular.requirement.module.spring.SingularDefaultBeanFactory;
import org.opensingular.requirement.module.spring.SingularServerSpringAppConfig;
import org.springframework.orm.hibernate4.support.OpenSessionInViewFilter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * TODO
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractSingularInitializer implements SingularInitializer {
    protected abstract String[] springPackagesToScan();

    @Deprecated
    public SchedulerInitializer schedulerConfiguration() {
        return new SchedulerInitializer() {
            @Override
            public Class<?> mailConfiguration() {
                return MailSenderSchedulerInitializer.class;
            }

            @Override
            public Class<?> attachmentGCConfiguration() {
                return AttachmentGCSchedulerInitializer.class;
            }
        };
    }

    /**
     * TODO
     */
    public WorkspaceInitializer workspaceConfiguration() {
        return new WorkspaceInitializer();
    }

    /**
     * TODO
     */
    @Override
    public AnnotationConfigWebApplicationContext createApplicationContext() {
        SingularAnnotationConfigWebApplicationContext applicationContext;
        applicationContext = new SingularAnnotationConfigWebApplicationContext();
        applicationContext.scan(springPackagesToScan());
        return applicationContext;
    }

    /**
     * TODO
     */
    protected List<Class<?>> getSpringAnnotatedClasses() {
        List<Class<?>> annotatedClasses = new ArrayList<>();
        annotatedClasses.add(springConfigurationClass());
        annotatedClasses.add(beanFactory());
        annotatedClasses.add(persistenceConfiguration());
        annotatedClasses.add(getSingularSpringWebMVCConfig());
        annotatedClasses.add(getRestSecurityConfiguration());
        return annotatedClasses;
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
    protected Class<? extends SingularServerSpringAppConfig> springConfigurationClass() {
        return SingularServerSpringAppConfig.class;
    }

    /**
     * TODO
     */
    protected Class<? extends SingularDefaultBeanFactory> beanFactory() {
        return SingularDefaultBeanFactory.class;
    }

    /**
     * TODO
     */
    protected Class<? extends SingularPersistenceDefaultBeanFactory> persistenceConfiguration() {
        return SingularPersistenceDefaultBeanFactory.class;
    }

    /**
     * TODO
     */
    public Class<? extends SingularSpringWebMVCConfig> getSingularSpringWebMVCConfig() {
        return SingularSpringWebMVCConfig.class;
    }

    /**
     * TODO
     */
    public Class<? extends WebSecurityConfigurerAdapter> getRestSecurityConfiguration() {
        return DefaultRestSecurity.class;
    }

    /**
     * TODO
     */
    protected void setupServletContext(ServletContext servletContext,
                                       AnnotationConfigWebApplicationContext applicationContext) {

        ServletRegistration.Dynamic dispatcherServlet = servletContext
                .addServlet("Spring MVC Dispatcher Servlet", new DispatcherServlet(applicationContext));
        dispatcherServlet.setLoadOnStartup(1);
        dispatcherServlet.addMapping(getDefaultFilterMapping());

        servletContext
                .addFilter("springSecurityFilterChain", new DelegatingFilterProxy(
                        "springSecurityFilterChain", applicationContext))
                .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, getDefaultFilterMapping());

        servletContext
                .addFilter("opensessioninview", OpenSessionInViewFilter.class)
                .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, getDefaultFilterMapping());

        servletContext
                .addServlet(SimplePostFilesStrategy.class.getName(), FileUploadServlet.class)
                .addMapping(SimplePostFilesStrategy.URL_PATTERN);

        servletContext
                .addListener(new HttpSessionListener() {
                    @Override
                    public void sessionCreated(HttpSessionEvent se) {
                        se.getSession().setMaxInactiveInterval(60 * getSessionTimeoutMinutes());
                    }

                    @Override
                    public void sessionDestroyed(HttpSessionEvent se) {
                    }
                });

        servletContext
                .addListener(new ContextLoaderListener(applicationContext));

        servletContext
                .addListener(RequestContextListener.class);

        if (SingularProperties.get().isTrue(SingularProperties.DEFAULT_CAS_ENABLED)) {
            servletContext.addListener(SingleSignOutHttpSessionListener.class);
        }

        String contextPath = servletContext.getContextPath();//NOSONAR
        servletContext
                .setAttribute(SkinnableApplication.INITSKIN_CONSUMER_PARAM,
                        (IConsumer<SkinOptions>) skinOptions -> initSkins(contextPath, skinOptions));

    }

    /**
     * TODO
     */
    private String getDefaultFilterMapping() {
        return "/*";
    }

    /**
     * TODO
     */
    protected void registerAnnotatedClasses(ServletContext servletContext,
                                            AnnotationConfigWebApplicationContext applicationContext) {
        getSpringAnnotatedClasses().forEach(applicationContext::register);
    }

    /**
     * TODO
     */
    protected void initSkins(String contextPath, SkinOptions skinOptions) {
    }

    /**
     * Configura o timeout da sessão web em minutos
     * <p>
     * x@return
     */
    protected int getSessionTimeoutMinutes() {
        return 15;//15 minutos
    }

    /**
     * TODO
     */
    @Override
    public List<? extends SingularWebInitializerListener> getSingularWebInitializerListener() {
        List<SingularWebInitializerListener> initializerListeners = new ArrayList<>();
        initializerListeners.add(this::registerAnnotatedClasses);
        initializerListeners.add(this::setupServletContext);
        initializerListeners.add(schedulerConfiguration());
        initializerListeners.add(workspaceConfiguration());
        return initializerListeners;
    }
}