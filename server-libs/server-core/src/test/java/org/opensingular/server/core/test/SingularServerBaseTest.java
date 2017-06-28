package org.opensingular.server.core.test;

import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;


@ContextConfiguration(classes = ServerConfigurationMock.class, loader = SingularServerContextLoader.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public abstract class SingularServerBaseTest extends SingularCommonsBaseTest {


}
