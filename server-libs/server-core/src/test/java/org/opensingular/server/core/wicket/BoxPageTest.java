package org.opensingular.server.core.wicket;

import javax.inject.Inject;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.TextField;
import org.junit.Test;
import org.opensingular.form.wicket.component.SingularButton;
import org.opensingular.form.wicket.helpers.AssertionsWComponent;
import org.opensingular.form.wicket.helpers.SingularFormTester;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.server.commons.STypeFOO;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.test.ContextUtil;
import org.opensingular.server.commons.test.SingularServletContextTestExecutionListener;
import org.opensingular.server.commons.wicket.view.form.FormPage;
import org.opensingular.server.commons.wicket.view.util.ActionContext;
import org.opensingular.server.core.test.ServerApplicationMock;
import org.opensingular.server.core.test.SingularServerBaseTest;
import org.opensingular.server.core.wicket.box.BoxPage;
import org.opensingular.server.core.wicket.history.HistoryPage;
import org.opensingular.server.p.commons.config.PServerContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class BoxPageTest extends SingularServerBaseTest {

    public static final String SINGULAR = "/singular";

    static {
        ContextUtil.setContextPath(SINGULAR);
        ContextUtil.setPathInfo(SINGULAR + PServerContext.PETITION.getUrlPath());
    }

    private SingularWicketTester tester;

    @Inject
    private ServerApplicationMock singularApplication;

    @WithUserDetails("vinicius.nunes")
    @Test
    public void renderTestPageWithMenu() {
        tester = new SingularWicketTester(singularApplication);
        BoxPage boxPage = new BoxPage();
        tester.startPage(boxPage);
        tester.assertRenderedPage(BoxPage.class);
        tester.assertNoErrorMessage();
    }

    @WithUserDetails("vinicius.nunes")
    @Test
    public void deleteItem() {
        tester = new SingularWicketTester(singularApplication);
        BoxPage boxPage = new BoxPage();
        tester.startPage(boxPage);
        tester.assertRenderedPage(BoxPage.class);
        tester.assertNoErrorMessage();

        Component deleteButton = tester.getAssertionsPage()
                .getSubCompomentWithId("actions")
                .getSubCompomentWithId("3")
                .getSubCompomentWithId("link")
                .getTarget();
        tester.executeAjaxEvent(deleteButton, "click");
        tester.assertNoErrorMessage();

        Component confirmButton = tester.getAssertionsPage()
                .getSubCompomentWithId("delete-btn")
                .getTarget();
        tester.executeAjaxEvent(confirmButton, "click");
        tester.assertNoErrorMessage();

        tester.assertRenderedPage(BoxPage.class);
        tester.assertNoErrorMessage();
    }

    @WithUserDetails("vinicius.nunes")
    @Test
    public void cancelDeleteItem() {
        tester = new SingularWicketTester(singularApplication);
        BoxPage boxPage = new BoxPage();
        tester.startPage(boxPage);
        tester.assertRenderedPage(BoxPage.class);

        Component deleteButton = tester.getAssertionsPage()
                .getSubCompomentWithId("actions")
                .getSubCompomentWithId("3")
                .getSubCompomentWithId("link")
                .getTarget();
        tester.executeAjaxEvent(deleteButton, "click");
        tester.assertNoErrorMessage();

        Component confirmButton = tester.getAssertionsPage()
                .getSubCompomentWithId("cancel-delete-btn")
                .getTarget();
        tester.executeAjaxEvent(confirmButton, "click");

        tester.assertRenderedPage(BoxPage.class);
        tester.assertNoErrorMessage();
    }

    @WithUserDetails("vinicius.nunes")
    @Test
    public void relocateItem() {
        tester = new SingularWicketTester(singularApplication);
        BoxPage boxPage = new BoxPage();
        tester.startPage(boxPage);
        tester.assertRenderedPage(BoxPage.class);

        Component deleteButton = tester.getAssertionsPage()
                .getSubCompomentWithId("actions")
                .getSubCompomentWithId("4")
                .getSubCompomentWithId("link")
                .getTarget();
        tester.executeAjaxEvent(deleteButton, "click");
        tester.assertNoErrorMessage();

        Component confirmationForm = tester.getAssertionsPage()
                .getSubCompomentWithId("confirmationForm")
                .getTarget();
        SingularFormTester formTester = tester.newSingularFormTester(confirmationForm.getPageRelativePath());
        formTester.select("selecao", 0);

        Component confirmButton = tester.getAssertionsPage()
                .getSubCompomentWithId("delete-btn")
                .getTarget();
        tester.executeAjaxEvent(confirmButton, "click");

        tester.assertRenderedPage(BoxPage.class);
        tester.assertNoErrorMessage();
    }

    @WithUserDetails("vinicius.nunes")
    @Test
    public void historyForm() {
        tester = new SingularWicketTester(singularApplication);
        sendDraft();

        BoxPage boxPage = new BoxPage();
        tester.startPage(boxPage);
        Component historyLink = tester.getAssertionsPage()
                .getSubCompomentWithId("actions")
                .getSubCompomentWithId("5")
                .getSubCompomentWithId("link")
                .getTarget();
        tester.clickLink(historyLink);

        tester.assertRenderedPage(HistoryPage.class);
    }

    public FormPage sendDraft() {
        ActionContext context = new ActionContext();
        context.setFormName(STypeFOO.FULL_NAME);
        context.setFormAction(FormAction.FORM_FILL);
        FormPage p = new FormPage(context);
        tester.startPage(p);
        tester.assertRenderedPage(FormPage.class);

        TextField<String> t = (TextField<String>) new AssertionsWComponent(p).getSubComponents(TextField.class).first().getTarget();
        t.getModel().setObject("teste");
        tester.executeAjaxEvent(new AssertionsWComponent(p).getSubCompomentWithId("send-btn").getTarget(), "click");
        tester.executeAjaxEvent(new AssertionsWComponent(p).getSubCompomentWithId("confirm-btn").getTarget(), "click");
        return p;
    }
}
