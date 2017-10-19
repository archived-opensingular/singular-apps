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

package org.opensingular.server.commons.service;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.document.RefSDocumentFactory;
import org.opensingular.form.document.RefType;
import org.opensingular.form.document.SDocumentFactory;
import org.opensingular.server.commons.STypeFOO;
import org.opensingular.server.commons.test.FOOFlowWithTransition;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;

import javax.inject.Inject;
import javax.transaction.Transactional;

public class PetitionInstanceTest extends SingularCommonsBaseTest {

    @Inject
    private DefaultPetitionService petitionService;

    @Test
    @Transactional
    public void testSomeFunctions(){
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance instance = documentFactoryRef.get().createInstance(RefType.of(STypeFOO.class));
        ((SIComposite) instance).getField(0).setValue("value");

        PetitionInstance petitionInitial = petitionService.createNewPetitionWithoutSave(null, null, p -> {}, requirementDefinitionEntity);
        petitionInitial.setFlowDefinition(FOOFlowWithTransition.class);

        petitionService.saveOrUpdate(petitionInitial, instance, true);

        Assert.assertNotNull(petitionInitial.getMainForm());

        Assert.assertTrue(petitionInitial.getMainForm(STypeFOO.class).getType() instanceof STypeFOO);

        Assert.assertNotNull(petitionInitial.getMainFormAndCast(SIComposite.class));

        Assert.assertNotNull(petitionInitial.getFlowDefinition());

        Assert.assertTrue(petitionInitial.getFlowDefinitionOpt().isPresent());

        Assert.assertFalse(petitionInitial.getParentPetition().isPresent());

        Assert.assertNotNull(petitionInitial.getMainFormCurrentFormVersion());

        Assert.assertEquals(STypeFOO.FULL_NAME, petitionInitial.getMainFormTypeName());
    }
}
