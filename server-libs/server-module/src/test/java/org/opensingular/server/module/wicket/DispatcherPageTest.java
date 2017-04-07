package org.opensingular.server.module.wicket;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Test;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.server.commons.test.CommonsApplicationMock;
import org.opensingular.server.commons.test.CommonsConfigurationMock;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.opensingular.server.commons.test.SingularCommonsContextLoader;
import org.opensingular.server.commons.test.SingularServletContextTestExecutionListener;
import org.opensingular.server.commons.wicket.error.AccessDeniedPage;
import org.opensingular.server.module.test.ModuleConfigurationMock;
import org.opensingular.server.module.wicket.view.util.dispatcher.DispatcherPage;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;

import javax.inject.Inject;
import javax.transaction.Transactional;


@ContextConfiguration(classes = {CommonsConfigurationMock.class, ModuleConfigurationMock.class}, loader = SingularCommonsContextLoader.class)
@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class DispatcherPageTest extends SingularCommonsBaseTest {


    @Inject
    CommonsApplicationMock singularApplication;


    private SingularWicketTester tester;

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void dispatchAndRender() {
        tester = new SingularWicketTester(singularApplication);
        PageParameters pageParameters = new PageParameters();
        pageParameters.add("a","1");
        pageParameters.add("f","foooooo.StypeFoo");
        tester.startPage(DispatcherPage.class, pageParameters);
        tester.assertRenderedPage(AccessDeniedPage.class);
    }


}
