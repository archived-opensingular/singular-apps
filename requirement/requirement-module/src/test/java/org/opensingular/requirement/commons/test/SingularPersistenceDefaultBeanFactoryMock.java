package org.opensingular.requirement.commons.test;

import org.opensingular.app.commons.spring.persistence.SingularPersistenceDefaultBeanFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement(
        proxyTargetClass = true
)
public class SingularPersistenceDefaultBeanFactoryMock extends SingularPersistenceDefaultBeanFactory {

}
