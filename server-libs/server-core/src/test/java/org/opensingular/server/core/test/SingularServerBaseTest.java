package org.opensingular.server.core.test;

import org.apache.wicket.Page;
import org.opensingular.form.wicket.helpers.AssertionsWComponent;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.lib.commons.lambda.IConsumer;
import org.opensingular.server.commons.STypeFOO;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.opensingular.server.commons.wicket.view.form.FormPage;
import org.opensingular.server.commons.wicket.view.util.ActionContext;
import org.springframework.test.context.ContextConfiguration;


@ContextConfiguration(classes = ServerConfigurationMock.class, loader = SingularServerContextLoader.class)
public abstract class SingularServerBaseTest extends SingularCommonsBaseTest {


}
