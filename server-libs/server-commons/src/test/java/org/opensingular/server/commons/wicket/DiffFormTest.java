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

import org.apache.wicket.Page;
import org.junit.Test;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.document.RefSDocumentFactory;
import org.opensingular.form.document.RefType;
import org.opensingular.form.document.SDocumentFactory;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.server.commons.STypeFOO;
import org.opensingular.server.commons.persistence.dao.form.PetitionerDAO;
import org.opensingular.server.commons.service.DefaultPetitionSender;
import org.opensingular.server.commons.service.DefaultPetitionService;
import org.opensingular.server.commons.service.PetitionInstance;
import org.opensingular.server.commons.test.CommonsApplicationMock;
import org.opensingular.server.commons.test.FOOFlowWithTransition;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.opensingular.server.commons.test.SingularServletContextTestExecutionListener;
import org.opensingular.server.commons.wicket.view.form.DiffFormPage;
import org.opensingular.server.commons.wicket.view.util.ActionContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.transaction.Transactional;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class DiffFormTest extends SingularCommonsBaseTest {

    @Inject
    private CommonsApplicationMock singularApplication;

    @Inject
    private PetitionerDAO petitionerDAO;

    @Inject
    private DefaultPetitionService petitionService;

    @Inject
    private DefaultPetitionSender sender;

    private SingularWicketTester tester;

    @Test
    @Transactional
    @WithUserDetails("vinicius.nunes")
    public void renderDiffPage() {
        Long petitionCod = createMockPetitionAndReturnPetitionCod();

        tester = new SingularWicketTester(singularApplication);
        ActionContext actionContext = new ActionContext();
        actionContext.setPetitionId(petitionCod);

        Page p = new DiffFormPage(actionContext);
        tester.startPage(p);
        tester.assertRenderedPage(DiffFormPage.class);
    }

    private Long createMockPetitionAndReturnPetitionCod() {
        SInstance instance = createInstanceToPetition();

        PetitionInstance petitionInitial = createNewPetitionInstance(instance);

        executeTransition(petitionInitial);

        return petitionInitial.getCod();
    }

    private SInstance createInstanceToPetition() {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(STypeFOO.class));
        ((SIComposite) instance).getField(0).setValue("value");
        return instance;
    }

    @Nonnull
    private PetitionInstance createNewPetitionInstance(SInstance instance) {
        PetitionInstance petitionInitial = petitionService.createNewPetitionWithoutSave(null, null, p -> {
        }, requirementDefinitionEntity);
        petitionInitial.setFlowDefinition(FOOFlowWithTransition.class);

        petitionService.saveOrUpdate(petitionInitial, instance, true);

        sender.send(petitionInitial, instance, "vinicius.nunes");
        petitionService.executeTransition("Transition bar", petitionInitial, this::onTransition, null, null);
        return petitionInitial;
    }

    private void executeTransition(PetitionInstance petitionInitial) {
        SIComposite mainFormAsInstance = petitionService.getMainFormAsInstance(petitionInitial.getEntity());
        mainFormAsInstance.getField(0).setValue("new value");
        petitionService.saveOrUpdate(petitionInitial, mainFormAsInstance, true);

        petitionService.executeTransition("End bar", petitionInitial, this::onTransition, null, null);
    }

    private void onTransition(PetitionInstance petitionInstance, String s) {
    }
}
