package org.opensingular.requirement.module.config.melody;

import net.bull.javamelody.MonitoringFilter;
import net.bull.javamelody.SessionListener;
import org.opensingular.lib.commons.base.SingularProperties;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import java.util.EnumSet;

import static org.opensingular.lib.commons.base.SingularProperties.SINGULAR_JAVAMELODY_ENABLED;

public class MelodyFilter {

    private static MelodyFilter ourInstance = new MelodyFilter();

    public static MelodyFilter getInstance() {
        return ourInstance;
    }

    private MelodyFilter() {
    }

    public void addJavaMelodyFilter(ServletContext container) {
        if(SingularProperties.get().isTrue(SINGULAR_JAVAMELODY_ENABLED)) {
            FilterRegistration.Dynamic dynamic = container.addFilter("javamelodyFilter", MonitoringFilter.class);
            dynamic.addMappingForUrlPatterns(EnumSet.of(DispatcherType.ASYNC, DispatcherType.REQUEST),
                    false, "/*");
            dynamic.setAsyncSupported(true);
            container.addListener(new SessionListener());
        }
    }
}
