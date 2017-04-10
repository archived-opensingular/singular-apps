package org.opensingular.server.module.test;

import org.opensingular.server.commons.service.SingularRequirementService;
import org.opensingular.server.module.service.SingularRequirementServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ModuleConfigurationMock {

    @Primary
    @Bean
    public SingularRequirementService requirementService(){
        return new SingularRequirementServiceImpl();
    }
}
