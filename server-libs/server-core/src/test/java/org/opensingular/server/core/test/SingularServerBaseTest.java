package org.opensingular.server.core.test;

import org.opensingular.server.commons.test.ConfigurationMock;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.springframework.test.context.ContextConfiguration;


@ContextConfiguration(classes = ConfigurationMock.class, loader = SingularServerContextLoader.class)
public abstract class SingularServerBaseTest extends SingularCommonsBaseTest {


}
