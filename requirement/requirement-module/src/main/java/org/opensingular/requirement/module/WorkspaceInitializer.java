package org.opensingular.requirement.module;

import org.apache.wicket.protocol.http.WicketFilter;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.config.SingularWebApplicationInitializer;
import org.opensingular.requirement.module.config.SpringSecurityInitializer;
import org.opensingular.requirement.module.spring.security.config.SingularLogoutFilter;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import java.util.EnumSet;


public class WorkspaceInitializer implements Loggable {
    private static final String SINGULAR_SECURITY = "[SINGULAR][WEB] {} {}";

    public void init(ServletContext servletContext, AnnotationConfigWebApplicationContext applicationContext) {
        SingularModuleConfiguration singularModuleConfiguration = new SingularModuleConfiguration();
        try {
            singularModuleConfiguration.init();
        } catch (IllegalAccessException | InstantiationException ex) {
            getLogger().error(ex.getMessage(), ex);
        }

        SpringSecurityInitializer springSecurityInitializer = (SpringSecurityInitializer) servletContext
                .getAttribute(SingularWebApplicationInitializer.SERVLET_ATTRIBUTE_SECURITY_CONFIGURATION_CONFIGURATION);

        for (IServerContext context : singularModuleConfiguration.getContexts()) {
            getLogger().info(SINGULAR_SECURITY, "Setting up web context:", context.getContextPath());
            addWicketFilter(servletContext, context);
            getLogger().info(SINGULAR_SECURITY, "Securing (Spring Security) context:", context.getContextPath());
            Class<?> config = springSecurityInitializer.getSpringSecurityConfigClass(context);
            if (config != null) {
                applicationContext.register(config);
                addLogoutFilter(servletContext, context);
            }
        }

        servletContext.setAttribute(SingularModuleConfiguration.SERVLET_ATTRIBUTE_SGL_MODULE_CONFIG, singularModuleConfiguration);
    }

    protected void addWicketFilter(ServletContext ctx, IServerContext context) {
        FilterRegistration.Dynamic wicketFilter = ctx.addFilter(context.getName() + System.identityHashCode(context), WicketFilter.class);
        wicketFilter.setInitParameter("applicationClassName", context.getWicketApplicationClass().getName());
        wicketFilter.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, context.getContextPath());
        wicketFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, context.getContextPath());
    }

    protected void addLogoutFilter(ServletContext ctx, IServerContext context) {
        ctx
                .addFilter("singularLogoutFilter" + System.identityHashCode(context), SingularLogoutFilter.class)
                .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, context.getUrlPath() + "/logout");
    }

}