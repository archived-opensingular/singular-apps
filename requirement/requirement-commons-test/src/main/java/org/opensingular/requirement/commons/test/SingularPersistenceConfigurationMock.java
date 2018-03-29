package org.opensingular.requirement.commons.test;

import org.opensingular.requirement.commons.spring.SingularDefaultPersistenceConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement(
        proxyTargetClass = true
)
public class SingularPersistenceConfigurationMock extends SingularDefaultPersistenceConfiguration {

    @Override
    protected boolean getAdicionarAtorDefault() {
        return true;
    }
}
