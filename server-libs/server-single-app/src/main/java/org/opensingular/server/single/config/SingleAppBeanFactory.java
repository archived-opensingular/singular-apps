package org.opensingular.server.single.config;

import org.opensingular.server.commons.connector.ModuleDriver;
import org.opensingular.server.commons.spring.SingularDefaultBeanFactory;
import org.springframework.context.annotation.Bean;

public class SingleAppBeanFactory extends SingularDefaultBeanFactory {

    @Bean
    @Override
    public ModuleDriver moduleDriver() {
        return new LocalModuleDriver();
    }
}
