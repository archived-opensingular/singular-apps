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

package org.opensingular.requirement.commons.wicket.error;

import org.apache.wicket.Page;
import org.junit.Test;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.requirement.commons.wicket.error.AccessDeniedPage;

import org.opensingular.requirement.commons.test.SingularServletContextTestExecutionListener;
import org.opensingular.requirement.commons.test.CommonsApplicationMock;
import org.opensingular.requirement.commons.test.SingularCommonsBaseTest;
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
