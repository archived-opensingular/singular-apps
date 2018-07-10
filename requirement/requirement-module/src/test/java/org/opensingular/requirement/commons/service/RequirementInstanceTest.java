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
import org.junit.Test;
import org.opensingular.form.SFormUtil;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.document.RefSDocumentFactory;
import org.opensingular.form.document.RefType;
import org.opensingular.form.document.SDocumentFactory;
import org.opensingular.requirement.commons.FOOFlowWithTransition;
import org.opensingular.requirement.commons.SingularCommonsBaseTest;
import org.opensingular.requirement.module.service.DefaultRequirementService;
import org.opensingular.requirement.module.service.RequirementInstance;
import org.opensingular.singular.pet.module.foobar.stuff.SPackageFoo;
import org.opensingular.singular.pet.module.foobar.stuff.STypeFoo;


import javax.inject.Inject;
import javax.transaction.Transactional;

public class RequirementInstanceTest extends SingularCommonsBaseTest {

    @Inject
    private DefaultRequirementService requirementService;

    @Test
    @Transactional
    public void testSomeFunctions(){
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance instance = documentFactoryRef.get().createInstance(RefType.of(STypeFoo.class));
        ((SIComposite) instance).getField(0).setValue("value");

        RequirementInstance requirement = requirementService.createNewRequirementWithoutSave(null, null, p -> {}, getRequirementDefinition());
        requirement.setFlowDefinition(FOOFlowWithTransition.class);

        requirementService.saveOrUpdate(requirement, instance, true);

        Assert.assertNotNull(requirement.getMainForm());

        Assert.assertTrue(requirement.getMainForm(STypeFoo.class).getType() instanceof STypeFoo);

        Assert.assertNotNull(requirement.getMainFormAndCast(SIComposite.class));

        Assert.assertNotNull(requirement.getFlowDefinition());

        Assert.assertTrue(requirement.getFlowDefinitionOpt().isPresent());

        Assert.assertFalse(requirement.getParentRequirement().isPresent());

        Assert.assertNotNull(requirement.getMainFormCurrentFormVersion());

        Assert.assertEquals(SFormUtil.getTypeName(STypeFoo.class), requirement.getMainFormTypeName());
    }
}
