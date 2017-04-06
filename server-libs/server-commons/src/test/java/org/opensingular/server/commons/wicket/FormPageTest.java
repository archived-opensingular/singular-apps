package org.opensingular.server.commons.wicket;

import org.apache.wicket.markup.html.form.TextField;
import org.junit.Assert;
import org.junit.Test;
import org.opensingular.form.SInstance;
import org.opensingular.form.document.RefType;
import org.opensingular.form.persistence.FormKey;
import org.opensingular.form.service.FormService;
import org.opensingular.form.spring.SpringSDocumentFactory;
import org.opensingular.form.util.transformer.Value;
import org.opensingular.form.wicket.helpers.AssertionsWComponent;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.server.commons.STypeFOO;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.service.PetitionService;
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

    public static final String SUPER_TESTE_STRING = "SuperTeste";
    @Inject
    SingularApplicationMock singularApplication;

    @Inject
    FormService formService;

    @Inject
    PetitionService<?,?> petitionService;

    @Inject
    SpringSDocumentFactory documentFactory;

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
    @Test
    public void testSaveForm() {
        tester = new SingularWicketTester(singularApplication);
        ActionContext context = new ActionContext();
        context.setFormName(STypeFOO.FULL_NAME);
        context.setFormAction(FormAction.FORM_FILL);
        FormPage p = new FormPage(context);
        tester.startPage(p);
        tester.assertRenderedPage(FormPage.class);

        TextField<String> t = (TextField<String>) new AssertionsWComponent(p).getSubComponents(TextField.class).first().getTarget();
        t.getModel().setObject(SUPER_TESTE_STRING);
        tester.executeAjaxEvent(new AssertionsWComponent(p).getSubCompomentWithId("save-btn").getTarget(), "click");

        SInstance fooInstance = tester.getAssertionsInstance().getTarget();
        FormKey formKey = FormKey.from(fooInstance);

        Assert.assertNotNull(petitionService.getFormProcessInstanceEntity(fooInstance));
        Assert.assertNotNull(formService.loadFormEntity(formKey));

        SInstance si = formService.loadSInstance(formKey, RefType.of(STypeFOO.class), documentFactory);
        Assert.assertEquals(SUPER_TESTE_STRING, Value.of(si, STypeFOO.FIELD_NOME));


    }


    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void testFormPageWithoutContext() {
        tester = new SingularWicketTester(singularApplication);
        ActionContext context = new ActionContext();
        context.setFormName(STypeFOO.FULL_NAME);
        FormPage p = new FormPage(context);
        tester.startPage(p);
        tester.assertRenderedPage(Page500.class);
    }


}
