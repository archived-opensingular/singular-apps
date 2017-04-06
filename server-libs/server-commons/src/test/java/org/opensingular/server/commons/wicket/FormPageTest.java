package org.opensingular.server.commons.wicket;

import org.junit.Test;
import org.opensingular.form.wicket.helpers.AssertionsWComponent;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.server.commons.STypeFOO;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.test.SingularApplicationMock;
import org.opensingular.server.commons.test.SingularServerBaseTest;
import org.opensingular.server.commons.test.SingularServletContextTestExecutionListener;
import org.opensingular.server.commons.wicket.error.Page500;
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

    private SingularWicketTester tester;

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void testFormPageRendering() {
        tester = new SingularWicketTester(singularApplication);
        ActionContext context = new ActionContext();
        context.setFormName(STypeFOO.FULL_NAME);
        context.setFormAction(FormAction.FORM_FILL);
        FormPage p = new FormPage(context);
        tester.startPage(p);
        tester.assertRenderedPage(FormPage.class);
    }


    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void testSaveForm() {
        tester = new SingularWicketTester(singularApplication);
        ActionContext context = new ActionContext();
        context.setFormName(STypeFOO.FULL_NAME);
        context.setFormAction(FormAction.FORM_FILL);
        FormPage p = new FormPage(context);
        tester.startPage(p);
        tester.executeAjaxEvent(new AssertionsWComponent(p).getSubCompomentWithId("save-btn").getTarget(), "click");
        tester.assertRenderedPage(FormPage.class);
    }



    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void testFormPageWithoutContext() {
        tester = new SingularWicketTester(singularApplication);
        ActionContext context = new ActionContext();
        context.setFormName(STypeFOO.FULL_NAME);
        FormPage      p       = new FormPage(context);
        tester.startPage(p);
        tester.assertRenderedPage(Page500.class);
    }


}
