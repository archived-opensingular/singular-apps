package org.opensingular.server.commons.config;

import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;

public class SingularAnnotationConfigWebContextLoader extends AnnotationConfigWebContextLoader {

    @Override
    public void processContextConfiguration(ContextConfigurationAttributes configAttributes) {
        super.processContextConfiguration(configAttributes);
    }
}
