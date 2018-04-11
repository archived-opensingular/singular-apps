/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.requirement.commons.wicket;

import net.vidageek.mirror.dsl.Mirror;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.junit.Test;
import org.opensingular.flow.core.TaskInstance;
import org.opensingular.form.SInstance;
import org.opensingular.form.document.RefType;
import org.opensingular.form.persistence.FormKey;
import org.opensingular.form.service.FormService;
import org.opensingular.form.spring.SpringSDocumentFactory;
import org.opensingular.form.util.transformer.Value;
import org.opensingular.form.wicket.component.SingularButton;
import org.opensingular.form.wicket.helpers.AssertionsWComponent;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.lib.wicket.util.ajax.ActionAjaxLink;
import org.opensingular.lib.wicket.util.bootstrap.layout.TemplatePanel;
import org.opensingular.requirement.commons.SPackageFOO;
import org.opensingular.requirement.commons.form.FormAction;
import org.opensingular.requirement.commons.service.RequirementInstance;
import org.opensingular.requirement.commons.service.RequirementService;
import org.opensingular.requirement.commons.test.CommonsApplicationMock;
import org.opensingular.requirement.commons.test.SingularCommonsBaseTest;
import org.opensingular.requirement.commons.test.SingularServletContextTestExecutionListener;
import org.opensingular.requirement.commons.wicket.error.Page500;
import org.opensingular.requirement.commons.wicket.view.form.FormPage;
import org.opensingular.requirement.commons.wicket.view.util.ActionContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;

import javax.inject.Inject;

import static org.junit.Assert.*;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class FormPageTest extends SingularCommonsBaseTest {

    public static final String SUPER_TESTE_STRING = "SuperTeste";

    @Inject
    CommonsApplicationMock singularApplication;

    @Inject
    FormService formService;

    @Inject
    RequirementService<?, ?> requirementService;

    @Inject
    SpringSDocumentFactory documentFactory;

    private SingularWicketTester tester;

    @WithUserDetails("vinicius.nunes")
    @Test
    public void testFormPageRendering() {
        tester = new SingularWicketTester(singularApplication);
        ActionContext context = new ActionContext();
        context.setFormName(SPackageFOO.STypeFOO.FULL_NAME);
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

        assertNotNull(requirementService.getFormFlowInstanceEntity(fooInstance));
        assertNotNull(formService.loadFormEntity(formKey));

        SInstance si = formService.loadSInstance(formKey, RefType.of(SPackageFOO.STypeFOO.class), documentFactory);
        assertEquals(SUPER_TESTE_STRING, Value.of(si, SPackageFOO.STypeFOO.FIELD_NOME));
    }

    private FormPage saveDraft() {
        ActionContext context = new ActionContext();
        context.setFormName(SPackageFOO.STypeFOO.FULL_NAME);
        context.setFormAction(FormAction.FORM_FILL);
        context.setRequirementDefinitionId(requirementDefinitionEntity.getCod());
        FormPage p = new FormPage(context);
        tester.startPage(p);
        tester.assertRenderedPage(FormPage.class);

        TextField<String> t = (TextField<String>) new AssertionsWComponent(p).getSubComponents(TextField.class).first().getTarget();
        t.getModel().setObject(SUPER_TESTE_STRING);
        tester.executeAjaxEvent(new AssertionsWComponent(p).getSubComponentWithId("save-btn").getTarget(), "click");

        return p;
    }

    @WithUserDetails("vinicius.nunes")
    @Test
    public void testSendForm() {
        tester = new SingularWicketTester(singularApplication);
        FormPage p = sendRequirement(tester, SPackageFOO.STypeFOO.FULL_NAME, this::fillForm);

        RequirementInstance requirement = getRequirementFrom(p);
        assertNotNull(requirement.getFlowInstance());
    }

    private void fillForm(Page page) {
        TextField<String> t = (TextField<String>) new AssertionsWComponent(page).getSubComponents(TextField.class).first().getTarget();
        t.getModel().setObject(SUPER_TESTE_STRING);
    }

    @WithUserDetails("vinicius.nunes")
    @Test
    public void testLoadDraft() {
        tester = new SingularWicketTester(singularApplication);

        FormPage p = saveDraft();

        RequirementInstance requirement = getRequirementFrom(p);

        ActionContext context = new ActionContext();
        context.setFormName(SPackageFOO.STypeFOO.FULL_NAME);
        context.setFormAction(FormAction.FORM_FILL);
        context.setRequirementId(requirement.getCod());

        FormPage p2 = new FormPage(context);
        tester.startPage(p2);
        tester.assertRenderedPage(FormPage.class);

        TextField<String> t2 = (TextField<String>) new AssertionsWComponent(p2).getSubComponents(TextField.class).first().getTarget();
        assertEquals(SUPER_TESTE_STRING, t2.getDefaultModelObject());
    }

    public RequirementInstance getRequirementFrom(FormPage p) {
        return (RequirementInstance) new Mirror().on(p).invoke().method("getRequirement").withoutArgs();
    }

    @WithUserDetails("vinicius.nunes")
    @Test
    public void testExecuteTransition() {
        tester = new SingularWicketTester(singularApplication);

        FormPage p = sendRequirement(tester, SPackageFOO.STypeFOO.FULL_NAME, this::fillForm);

        RequirementInstance requirement = getRequirementFrom(p);

        ActionContext context = new ActionContext();
        context.setFormName(SPackageFOO.STypeFOO.FULL_NAME);
        context.setFormAction(FormAction.FORM_ANALYSIS);
        context.setRequirementId(requirement.getCod());

        FormPage p2 = new FormPage(context);
        tester.startPage(p2);
        tester.assertRenderedPage(FormPage.class);

        Component transitionButton = new AssertionsWComponent(p2)
                .getSubComponentWithId("custom-buttons")
                .getSubComponents(SingularButton.class)
                .first()
                .getTarget();
        tester.executeAjaxEvent(transitionButton, "click");

        Component confirmationButton = new AssertionsWComponent(p2)
                .getSubComponentWithId("modals")
                .getSubComponents(TemplatePanel.class)
                .last()
                .getSubComponents(SingularButton.class)
                .first()
                .getTarget();
        tester.executeAjaxEvent(confirmationButton, "click");

        RequirementInstance requirementFrom = getRequirementFrom(p2);
        TaskInstance     currentTask = requirementFrom.getCurrentTaskOrException();
        assertEquals("No more bar", currentTask.getName());
    }

    @WithUserDetails("vinicius.nunes")
    @Test
    public void testFormPageWithoutContext() {
        tester = new SingularWicketTester(singularApplication);
        ActionContext context = new ActionContext();
        context.setFormName(SPackageFOO.STypeFOO.FULL_NAME);
        FormPage p = new FormPage(context);
        tester.startPage(p);
        tester.assertRenderedPage(Page500.class);
    }

    @WithUserDetails("vinicius.nunes")
    @Test
    public void testOpenAnnotationAfterOpeningModal() {
        tester = new SingularWicketTester(singularApplication);

        ActionContext context = new ActionContext();
        context.setFormName(SPackageFOO.STypeFOOModal.FULL_NAME);
        context.setFormAction(FormAction.FORM_FILL_WITH_ANALYSIS_FILL);
        context.setRequirementDefinitionId(requirementDefinitionEntity.getCod());
        FormPage p = new FormPage(context);
        tester.startPage(p);
        tester.assertRenderedPage(FormPage.class);

        Form f = (Form) new AssertionsWComponent(p).getSubComponents(Form.class).first().getTarget();
        f.setMultiPart(false);

        Component addPessoa = p.get("save-form:singular-panel:generated:_:1:_:1:_:1:_:1:_:2:_:1:_:3:_:1:_:1:panel:form:footer:addButton");
        tester.executeAjaxEvent(addPessoa, "click");

        Component linkAnnotation = tester.getAssertionsPage().getSubComponents(ActionAjaxLink.class).first().getTarget();
        tester.executeAjaxEvent(linkAnnotation, "click");

        Component justificativa = tester.getAssertionsPage().getSubComponents(TextArea.class).first().getTarget();

        assertEquals(justificativa.getId(), "justificativa");
        assertTrue(justificativa.isVisibleInHierarchy());

    }

}
