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

package org.opensingular.requirement.commons.admin.healthsystem;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Assert;
import org.junit.Test;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SIList;
import org.opensingular.form.SInstance;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.requirement.commons.CommonsApplicationMock;
import org.opensingular.requirement.commons.SingularCommonsBaseTest;
import org.opensingular.requirement.module.admin.healthsystem.extension.DatabaseTablesAdminEntry;
import org.opensingular.requirement.module.test.SingularServletContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;

import static org.opensingular.requirement.commons.admin.healthsystem.HealthSystemPage.ENTRY_PATH_PARAM;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class SDbHealthTest extends SingularCommonsBaseTest {
    @Inject
    CommonsApplicationMock singularApplication;

    private SingularWicketTester tester;

    private void reachDbPanel() {
        tester = new SingularWicketTester(singularApplication);
        HealthSystemPage healthSystemPage = new HealthSystemPage(new PageParameters().add(ENTRY_PATH_PARAM, new DatabaseTablesAdminEntry().getKey()));
        tester.startPage(healthSystemPage);
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void validateTablesWithErrorTest() {
        reachDbPanel();
        tester.executeAjaxEvent(tester.getAssertionsForSubComp("checkButtonDB").isNotNull().getTarget(), "click");

        SInstance panelDB = tester.getAssertionsInstance().getTarget();
        Assert.assertEquals(0, ((SIList) (((SIComposite) panelDB).getAllFields().get(0))).get(0).getValidationErrors().size());
    }

}
