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

package org.opensingular.server.core.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.form.TextField;
import org.junit.Test;
import org.opensingular.form.wicket.helpers.AssertionsWComponent;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.internal.lib.wicket.test.SingularFormTester;
import org.opensingular.server.commons.config.PServerContext;
import org.opensingular.server.commons.test.ContextUtil;
import org.opensingular.server.commons.test.SingularServletContextTestExecutionListener;
import org.opensingular.server.core.test.ServerApplicationMock;
import org.opensingular.server.core.test.SingularServerBaseTest;
import org.opensingular.server.core.wicket.box.BoxPage;
import org.opensingular.server.core.wicket.history.HistoryPage;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;

import javax.inject.Inject;


@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class BoxPageTest extends SingularServerBaseTest {

    public static final String SINGULAR = "/singular";

    static {
        ContextUtil.setContextPath(SINGULAR);
        ContextUtil.setPathInfo(SINGULAR + PServerContext.REQUIREMENT.getUrlPath());
    }

    private SingularWicketTester tester;

    @Inject
    private ServerApplicationMock singularApplication;

    @WithUserDetails("vinicius.nunes")
    @Test(expected = RestartResponseException.class)
    public void renderTestPageWithMenu() {
        tester = new SingularWicketTester(singularApplication);
        BoxPage boxPage = new BoxPage(null);
        tester.startPage(boxPage);
        tester.assertRenderedPage(BoxPage.class);
        tester.assertNoErrorMessage();
    }

    @WithUserDetails("vinicius.nunes")
    @Test(expected = RestartResponseException.class)
    public void deleteItem() {
        tester = new SingularWicketTester(singularApplication);
        BoxPage boxPage = new BoxPage(null);
        tester.startPage(boxPage);
        tester.assertRenderedPage(BoxPage.class);
        tester.assertNoErrorMessage();

        Component deleteButton = tester.getAssertionsPage()
                .getSubComponentWithId("actions")
                .getSubComponentWithId("3")
                .getSubComponentWithId("link")
                .getTarget();
        tester.executeAjaxEvent(deleteButton, "click");
        tester.assertNoErrorMessage();

        Component confirmButton = tester.getAssertionsPage()
                .getSubComponentWithId("confirm-btn")
                .getTarget();
        tester.executeAjaxEvent(confirmButton, "click");
        tester.assertNoErrorMessage();

        tester.assertRenderedPage(BoxPage.class);
        tester.assertNoErrorMessage();
    }

    @WithUserDetails("vinicius.nunes")
    @Test(expected = RestartResponseException.class)
    public void cancelDeleteItem() {
        tester = new SingularWicketTester(singularApplication);
        BoxPage boxPage = new BoxPage(null);
        tester.startPage(boxPage);
        tester.assertRenderedPage(BoxPage.class);

        Component deleteButton = tester.getAssertionsPage()
                .getSubComponentWithId("actions")
                .getSubComponentWithId("3")
                .getSubComponentWithId("link")
                .getTarget();
        tester.executeAjaxEvent(deleteButton, "click");
        tester.assertNoErrorMessage();

        Component confirmButton = tester.getAssertionsPage()
                .getSubComponentWithId("cancel-btn")
                .getTarget();
        tester.executeAjaxEvent(confirmButton, "click");

        tester.assertRenderedPage(BoxPage.class);
        tester.assertNoErrorMessage();
    }

    @WithUserDetails("vinicius.nunes")
    @Test(expected = RestartResponseException.class)
    public void relocateItem() {
        tester = new SingularWicketTester(singularApplication);
        BoxPage boxPage = new BoxPage(null);
        tester.startPage(boxPage);
        tester.assertRenderedPage(BoxPage.class);

        Component deleteButton = tester.getAssertionsPage()
                .getSubComponentWithId("actions")
                .getSubComponentWithId("4")
                .getSubComponentWithId("link")
                .getTarget();
        tester.executeAjaxEvent(deleteButton, "click");
        tester.assertNoErrorMessage();

        Component confirmationForm = tester.getAssertionsPage()
                .getSubComponentWithId("confirmationForm")
                .getTarget();
        SingularFormTester formTester = tester.newSingularFormTester(confirmationForm.getPageRelativePath());
        formTester.select("usersDropDownChoice", 0);
        tester.executeAjaxEvent(confirmationForm.get("confirmationModal:dialog:body:confirmationModal_body:usersDropDownChoice"), "change");
        Component confirmButton = tester.getAssertionsPage()
                .getSubComponentWithId("confirm-btn")
                .getTarget();
        tester.executeAjaxEvent(confirmButton, "click");

        tester.assertRenderedPage(BoxPage.class);
        tester.assertNoErrorMessage();
    }

    @WithUserDetails("vinicius.nunes")
    @Test(expected = RestartResponseException.class)
    public void historyForm() {
        tester = new SingularWicketTester(singularApplication);
        sendRequirement(tester, SPackageFOO.STypeFOO.FULL_NAME, this::fillForm);

        BoxPage boxPage = new BoxPage(null);
        tester.startPage(boxPage);
        Component historyLink = tester.getAssertionsPage()
                .getSubComponentWithId("actions")
                .getSubComponentWithId("5")
                .getSubComponentWithId("link")
                .getTarget();
        tester.clickLink(historyLink);

        tester.assertRenderedPage(HistoryPage.class);
    }

    private void fillForm(Page page) {
        TextField<String> t = (TextField<String>) new AssertionsWComponent(page).getSubComponents(TextField.class).first().getTarget();
        t.getModel().setObject("teste");
    }

}
