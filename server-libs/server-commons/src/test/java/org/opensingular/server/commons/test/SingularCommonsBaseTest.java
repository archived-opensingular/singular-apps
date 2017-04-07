package org.opensingular.server.commons.test;

import org.junit.runner.RunWith;
import org.opensingular.lib.commons.util.Loggable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = CommonsConfigurationMock.class, loader = SingularCommonsContextLoader.class)
public abstract class SingularCommonsBaseTest implements Loggable {




}
