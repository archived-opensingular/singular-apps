package org.opensingular.server.commons.wicket;

import org.junit.Test;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.server.commons.STypeFOO;
import org.opensingular.server.commons.spring.security.DefaultUserDetailService;
import org.opensingular.server.commons.test.SingularApplicationMock;
import org.opensingular.server.commons.test.SingularServerBaseTest;
import org.opensingular.server.commons.test.SingularServletContextTestExecutionListener;
import org.opensingular.server.commons.wicket.view.form.FormPage;
import org.opensingular.server.commons.wicket.view.util.ActionContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;

import javax.inject.Inject;
import javax.transaction.Transactional;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class FormPageTest extends SingularServerBaseTest {

    @Inject
    SingularApplicationMock singularApplication;

    @Inject
    DefaultUserDetailService defaultUserDetailService;

    private SingularWicketTester tester;


    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void testFormPage() {
        ActionContext context = new ActionContext();
        context.setFormName(STypeFOO.class.getName());
        FormPage p = new FormPage(context);
        tester.startPage(p);
        tester.assertRenderedPage(FormPage.class);
    }


}
