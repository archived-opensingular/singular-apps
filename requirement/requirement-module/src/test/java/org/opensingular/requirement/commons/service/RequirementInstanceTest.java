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

package org.opensingular.requirement.commons.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opensingular.form.SFormUtil;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.document.RefSDocumentFactory;
import org.opensingular.form.document.RefType;
import org.opensingular.form.document.SDocumentFactory;
import org.opensingular.lib.commons.context.SingularContextSetup;
import org.opensingular.lib.commons.context.SingularSingletonStrategy;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.requirement.commons.FOOFlowWithTransition;
import org.opensingular.requirement.commons.SingularCommonsBaseTest;
import org.opensingular.requirement.module.service.DefaultRequirementService;
import org.opensingular.requirement.module.service.RequirementInstance;
import org.opensingular.singular.pet.module.foobar.stuff.FOOFlowWithTransition;
import org.opensingular.requirement.module.test.SingularServletContextTestExecutionListener;
import org.opensingular.singular.pet.module.foobar.stuff.STypeFoo;
import org.springframework.context.ApplicationContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;

import javax.inject.Inject;
import javax.transaction.Transactional;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class RequirementInstanceTest extends SingularCommonsBaseTest {

    @Inject
    private DefaultRequirementService requirementService;


    @Test
    @WithUserDetails("edgar.fabiano")
    @Transactional
    public void testSomeFunctions() {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(STypeFoo.class));
        ((SIComposite) instance).getField(0).setValue("value");

        RequirementInstance<?, ?> requirement = getRequirementDefinition().newRequirement();
        requirement.setFlowDefinition(FOOFlowWithTransition.class);

        requirement.saveForm(instance);

        Assert.assertNotNull(requirement.getDraft());

        Assert.assertTrue(requirement.getDraft(STypeFoo.class).get().getType() instanceof STypeFoo);


        Assert.assertNotNull(requirement.getFlowDefinition());

        Assert.assertTrue(requirement.getFlowDefinitionOpt().isPresent());

        Assert.assertFalse(requirement.getParentRequirement().isPresent());

        Assert.assertNotNull(requirement.getMainFormCurrentFormVersion());

        Assert.assertEquals(SFormUtil.getTypeName(STypeFoo.class), requirement.getMainFormTypeName());
    }


    @Test
    public void sendNewRequirement() {
        RefSDocumentFactory documentFactoryRef  = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance           instance            = documentFactoryRef.get().createInstance(RefType.of(STypeFoo.class));
        RequirementInstance requirementInstance = getRequirementDefinition().newRequirement();
        requirementInstance.saveForm(instance);
        requirementInstance.send("vinicius.nunes");
        requirementService.executeTransition("No more bar", requirementInstance, null, null, null);
    }

}
