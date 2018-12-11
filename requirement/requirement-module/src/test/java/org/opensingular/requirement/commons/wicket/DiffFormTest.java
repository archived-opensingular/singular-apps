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

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.wicket.Page;
import org.junit.Test;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.document.RefSDocumentFactory;
import org.opensingular.form.document.RefType;
import org.opensingular.form.document.SDocumentFactory;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.requirement.commons.CommonsApplicationMock;
import org.opensingular.requirement.commons.SingularCommonsBaseTest;
import org.opensingular.requirement.module.persistence.dao.form.ApplicantDAO;
import org.opensingular.requirement.module.service.DefaultRequirementService;
import org.opensingular.requirement.module.service.RequirementInstance;
import org.opensingular.requirement.module.test.SingularServletContextTestExecutionListener;
import org.opensingular.requirement.module.wicket.view.form.DiffFormPage;
import org.opensingular.requirement.module.wicket.view.util.ActionContext;
import org.opensingular.singular.pet.module.foobar.stuff.STypeFoo;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;

import java.util.ArrayList;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class DiffFormTest extends SingularCommonsBaseTest {

    @Inject
    private CommonsApplicationMock singularApplication;

    @Inject
    private ApplicantDAO applicantDAO;

    @Inject
    private DefaultRequirementService requirementService;

    private SingularWicketTester tester;

    @Test
    @Transactional
    @WithUserDetails("vinicius.nunes")
    public void renderDiffPage() {
        Long codRequirement = createMockRequirementAndReturnCodRequirement();

        tester = new SingularWicketTester(singularApplication);
        ActionContext actionContext = new ActionContext();
        actionContext.setRequirementId(codRequirement);

        Page p = new DiffFormPage(actionContext);
        tester.startPage(p);
        tester.assertRenderedPage(DiffFormPage.class);
    }

    private Long createMockRequirementAndReturnCodRequirement() {
        SInstance instance = createInstanceToRequirement();

        RequirementInstance requirement = createNewRequirementInstance(instance);

        executeTransition(requirement);

        return requirement.getCod();
    }

    private SInstance createInstanceToRequirement() {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(STypeFoo.class));
        ((SIComposite) instance).getField(0).setValue("value");
        return instance;
    }

    @Nonnull
    private RequirementInstance createNewRequirementInstance(SInstance instance) {
        RequirementInstance requirementInstance = getRequirementDefinition().newRequirement("user");
        requirementInstance.saveForm(instance);
        requirementInstance.send("vinicius.nunes");
        requirementService.executeTransition("Transition bar", requirementInstance, new ArrayList<>(), new ArrayList<>());
        return requirementInstance;
    }

    private void executeTransition(RequirementInstance requirement) {
        SIComposite mainFormAsInstance = requirementService.getMainFormAsInstance(requirement.getEntity());
        mainFormAsInstance.getField(0).setValue("new value");
        requirementService.saveOrUpdate(requirement, mainFormAsInstance, true);

        requirementService.executeTransition("End bar", requirement, new ArrayList<>(), new ArrayList<>());
    }

}
