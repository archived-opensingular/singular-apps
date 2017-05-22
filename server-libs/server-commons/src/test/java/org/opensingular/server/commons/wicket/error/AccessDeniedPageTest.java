package org.opensingular.server.commons.wicket.error;

import org.apache.wicket.Page;
import org.junit.Test;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.server.commons.test.CommonsApplicationMock;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.opensingular.server.commons.test.SingularServletContextTestExecutionListener;
import org.opensingular.server.commons.wicket.error.AccessDeniedPage;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;

import javax.inject.Inject;
import javax.transaction.Transactional;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class AccessDeniedPageTest extends SingularCommonsBaseTest {
    @Inject
    CommonsApplicationMock singularApplication;

    private SingularWicketTester tester;

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void testAccessDeniedPageRendering() {
        tester = new SingularWicketTester(singularApplication);
        Page p = new AccessDeniedPage();
        tester.startPage(p);
        tester.assertRenderedPage(AccessDeniedPage.class);
    }
}
