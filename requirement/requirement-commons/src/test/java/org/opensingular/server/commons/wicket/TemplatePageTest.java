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

package org.opensingular.server.commons.wicket;

import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.behavior.Behavior;
import org.junit.Test;
import org.opensingular.form.wicket.helpers.AssertionsWComponent;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.lib.wicket.util.menu.MetronicMenu;

import org.opensingular.requirement.commons.test.SingularServletContextTestExecutionListener;
import org.opensingular.server.commons.test.CommonsApplicationMock;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;

import javax.inject.Inject;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class TemplatePageTest extends SingularCommonsBaseTest {

    @Inject
    private CommonsApplicationMock singularApplication;

    private SingularWicketTester tester;

    @WithUserDetails("vinicius.nunes")
    @Test
    public void testFormPageRendering() {
        tester = new SingularWicketTester(singularApplication);
        FooTemplatePage page = tester.startPage(FooTemplatePage.class);
        MetronicMenu            m    = (MetronicMenu) new AssertionsWComponent(page).getSubComponents(MetronicMenu.class).first().getTarget();
        for (Behavior b : m.getBehaviors()) {
            if (b instanceof AbstractAjaxBehavior) {
                tester.executeBehavior((AbstractAjaxBehavior) b);
            }
        }
        tester.assertRenderedPage(FooTemplatePage.class);
    }


}
