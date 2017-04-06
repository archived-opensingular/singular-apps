package org.opensingular.server.core.wicket;


import org.junit.Test;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.server.commons.test.ContextUtil;
import org.opensingular.server.commons.test.SingularServletContextTestExecutionListener;
import org.opensingular.server.core.test.ServerApplicationMock;
import org.opensingular.server.core.test.SingularServerBaseTest;
import org.opensingular.server.core.wicket.box.BoxPage;
import org.opensingular.server.p.commons.config.PServerContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;

import javax.inject.Inject;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class BoxPageTest extends SingularServerBaseTest {

    public static final String SINGULAR = "/singular";

    static {
        ContextUtil.setContextPath(SINGULAR);
        ContextUtil.setPathInfo(SINGULAR + PServerContext.PETITION.getUrlPath());
    }

    @Inject
    private ServerApplicationMock singularApplication;

    private SingularWicketTester tester;


    @WithUserDetails("vinicius.nunes")
    @Test
    public void test() {
        tester = new SingularWicketTester(singularApplication);
        tester.startPage(BoxPage.class);
        tester.assertRenderedPage(BoxPage.class);
    }
}
