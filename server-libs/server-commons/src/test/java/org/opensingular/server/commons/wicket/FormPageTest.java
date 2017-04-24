package org.opensingular.server.commons.wicket;

import org.apache.wicket.markup.html.form.TextField;
import org.jetbrains.annotations.NotNull;
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
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.service.PetitionInstance;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.test.CommonsApplicationMock;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.opensingular.server.commons.test.SingularServletContextTestExecutionListener;
import org.opensingular.server.commons.wicket.error.Page500;
import org.opensingular.server.commons.wicket.view.form.FormPage;
import org.opensingular.server.commons.wicket.view.util.ActionContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;

import javax.inject.Inject;
import javax.transaction.Transactional;

import net.vidageek.mirror.dsl.Mirror;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class FormPageTest extends SingularCommonsBaseTest {

    public static final String SUPER_TESTE_STRING = "SuperTeste";

    @Inject
    CommonsApplicationMock singularApplication;

    @Inject
    FormService formService;

    @Inject
    PetitionService<?, ?> petitionService;

    @Inject
    SpringSDocumentFactory documentFactory;

    private SingularWicketTester tester;

    @WithUserDetails("vinicius.nunes")
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
        saveDraft();

        SInstance fooInstance = tester.getAssertionsInstance().getTarget();
        FormKey   formKey     = FormKey.from(fooInstance);

        Assert.assertNotNull(petitionService.getFormProcessInstanceEntity(fooInstance));
        Assert.assertNotNull(formService.loadFormEntity(formKey));

        SInstance si = formService.loadSInstance(formKey, RefType.of(STypeFOO.class), documentFactory);
        Assert.assertEquals(SUPER_TESTE_STRING, Value.of(si, STypeFOO.FIELD_NOME));
    }

    private FormPage saveDraft() {
        ActionContext context = new ActionContext();
        context.setFormName(STypeFOO.FULL_NAME);
        context.setFormAction(FormAction.FORM_FILL);
        FormPage p = new FormPage(context);
        tester.startPage(p);
        tester.assertRenderedPage(FormPage.class);

        TextField<String> t = (TextField<String>) new AssertionsWComponent(p).getSubComponents(TextField.class).first().getTarget();
        t.getModel().setObject(SUPER_TESTE_STRING);
        tester.executeAjaxEvent(new AssertionsWComponent(p).getSubCompomentWithId("save-btn").getTarget(), "click");

        return p;
    }

    @WithUserDetails("vinicius.nunes")
    @Test
    public void testSendForm() {
        tester = new SingularWicketTester(singularApplication);
        FormPage p = sendDraft();

        PetitionInstance petition = getPetitionFrom(p);
        Assert.assertNotNull(petition.getProcessInstance());
    }

    @NotNull
    public FormPage sendDraft() {
        ActionContext context = new ActionContext();
        context.setFormName(STypeFOO.FULL_NAME);
        context.setFormAction(FormAction.FORM_FILL);
        FormPage p = new FormPage(context);
        tester.startPage(p);
        tester.assertRenderedPage(FormPage.class);

        TextField<String> t = (TextField<String>) new AssertionsWComponent(p).getSubComponents(TextField.class).first().getTarget();
        t.getModel().setObject(SUPER_TESTE_STRING);
        tester.executeAjaxEvent(new AssertionsWComponent(p).getSubCompomentWithId("send-btn").getTarget(), "click");
        tester.executeAjaxEvent(new AssertionsWComponent(p).getSubCompomentWithId("confirm-btn").getTarget(), "click");
        return p;
    }

    @WithUserDetails("vinicius.nunes")
    @Test
    public void testLoadDraft() {
        tester = new SingularWicketTester(singularApplication);

        FormPage p = saveDraft();

        PetitionInstance petition = getPetitionFrom(p);

        ActionContext context = new ActionContext();
        context.setFormName(STypeFOO.FULL_NAME);
        context.setFormAction(FormAction.FORM_FILL);
        context.setPetitionId(petition.getCod());

        FormPage p2 = new FormPage(context);
        tester.startPage(p2);
        tester.assertRenderedPage(FormPage.class);

        TextField<String> t2 = (TextField<String>) new AssertionsWComponent(p2).getSubComponents(TextField.class).first().getTarget();
        Assert.assertEquals(SUPER_TESTE_STRING, t2.getDefaultModelObject());
    }

    public PetitionInstance getPetitionFrom(FormPage p) {
        return (PetitionInstance) new Mirror().on(p).invoke().method("getPetition").withoutArgs();
    }

    @WithUserDetails("vinicius.nunes")
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
