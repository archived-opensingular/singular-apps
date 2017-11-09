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

package org.opensingular.server.p.commons.admin.healthsystem;

import org.apache.wicket.Page;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Test;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.server.commons.test.CommonsApplicationMock;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.opensingular.server.commons.test.SingularServletContextTestExecutionListener;
import org.opensingular.server.commons.admin.healthsystem.HealthSystemPage;
import org.opensingular.server.commons.admin.healthsystem.extension.CacheAdminEntry;
import org.opensingular.server.commons.admin.healthsystem.extension.JobsAdminEntry;
import org.opensingular.server.commons.admin.healthsystem.extension.WebAdminEntry;
import org.opensingular.server.commons.admin.healthsystem.extension.CacheAdminEntry;
import org.opensingular.server.commons.admin.healthsystem.extension.JobsAdminEntry;
import org.opensingular.server.commons.admin.healthsystem.extension.WebAdminEntry;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static org.opensingular.server.commons.admin.healthsystem.HealthSystemPage.ENTRY_PATH_PARAM;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class HealthSystemPanelsTest extends SingularCommonsBaseTest {

    @Inject
    CommonsApplicationMock singularApplication;

    private SingularWicketTester tester;

    private void startPageTest(String key) {
        tester = new SingularWicketTester(singularApplication);
        Page p = new HealthSystemPage(new PageParameters().add(ENTRY_PATH_PARAM, key));
        tester.startPage(p);
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void cachePanelTest() {
        startPageTest(new CacheAdminEntry().getKey());
        tester.executeAjaxEvent(tester.getAssertionsForSubComp("clearAllCaches").getTarget(), "click");
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void jobPanelTest() {
        startPageTest(new JobsAdminEntry().getKey());
        tester.executeAjaxEvent(tester.getAssertionsForSubComp("runAllJobs").getTarget(), "click");
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void webPanelTest() {
        startPageTest(new WebAdminEntry().getKey());
        tester.executeAjaxEvent(tester.getAssertionsForSubComp("checkButtonWeb").getTarget(), "click");
        tester.executeAjaxEvent(tester.getAssertionsForSubComp("saveButtonWeb").getTarget(), "click");
    }
}
