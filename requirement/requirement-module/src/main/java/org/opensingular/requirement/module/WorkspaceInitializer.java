package org.opensingular.requirement.module;

import org.apache.wicket.protocol.http.WicketFilter;
import org.opensingular.form.SType;
import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.config.SingularWebInitializerListener;
import org.opensingular.requirement.module.flow.builder.RequirementFlowDefinition;
import org.opensingular.requirement.module.spring.security.config.SingularLogoutFilter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * TODO
 */
public class WorkspaceInitializer implements SingularWebInitializerListener, Loggable {

    /**
     * TODO
     */
    @Override
    public void initialize(ServletContext servletContext, AnnotationConfigWebApplicationContext applicationContext) {
        SingularModuleConfiguration singularModuleConfiguration = new SingularModuleConfiguration();
        try {
            singularModuleConfiguration.init(applicationContext);
        } catch (IllegalAccessException | InstantiationException ex) {
            getLogger().error(ex.getMessage(), ex);
        }

        for (IServerContext context : singularModuleConfiguration.getContexts()) {
            addWicketFilter(servletContext, context);
            Class<?> config = getSpringSecurityConfigClassByContext(context);
            if (config != null) {
                applicationContext.register(config);
                addLogoutFilter(servletContext, context);
            }
        }

        singularModuleConfiguration.setFormTypes(lookupFormTypes());
        for (String url : publicUrls()) {
            singularModuleConfiguration.addPublicUrl(url);
        }
        singularModuleConfiguration.initDefinitionsPackages(lookupRequirementFlowDefinition());

        servletContext.setAttribute(SingularModuleConfiguration.SERVLET_ATTRIBUTE_SGL_MODULE_CONFIG, singularModuleConfiguration);
    }

    /**
     * TODO
     */
    protected void addWicketFilter(ServletContext ctx, IServerContext context) {
        FilterRegistration.Dynamic wicketFilter = ctx.addFilter(context.getName() + System.identityHashCode(context), WicketFilter.class);
        wicketFilter.setInitParameter("applicationClassName", context.getWicketApplicationClass().getName());
        wicketFilter.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, context.getContextPath());
        wicketFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, context.getContextPath());
    }

    /**
     * TODO
     */
    protected void addLogoutFilter(ServletContext ctx, IServerContext context) {
        ctx
                .addFilter("singularLogoutFilter" + System.identityHashCode(context), SingularLogoutFilter.class)
                .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, context.getUrlPath() + "/logout");
    }

    /**
     * TODO
     */
    protected Class<? extends WebSecurityConfigurerAdapter> getSpringSecurityConfigClassByContext(IServerContext context) {
        return context.getSpringSecurityConfigClass();
    }

    /**
     * TODO
     */
    protected List<Class<? extends SType<?>>> lookupFormTypes() {
        return SingularClassPathScanner.get()
                .findSubclassesOf(SType.class)
                .stream()
                .filter(f -> !Modifier.isAbstract(f.getModifiers()))
                .map(i -> (Class<? extends SType<?>>) i)
                .collect(Collectors.toList());
    }

    /**
     * TODO
     */
    protected String[] publicUrls() {
        List<String> urls = new ArrayList<>();
        urls.add("/rest/*");
        urls.add("/resources/*");
        urls.add("/public/*");
        urls.add("/index.html");
        return urls.toArray(new String[0]);
    }

    /**
     * TODO
     */
    protected Set<Class<? extends RequirementFlowDefinition>> lookupRequirementFlowDefinition() {
        return SingularClassPathScanner.get()
                .findSubclassesOf(RequirementFlowDefinition.class);
    }

}