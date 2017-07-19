package org.opensingular.server.commons.wicket;

import org.apache.wicket.Page;
import org.jetbrains.annotations.NotNull;
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

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.HashMap;

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
    public void renderDiffPage(){
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
        SInstance instance = documentFactoryRef.get().createInstance(RefType.of(STypeFOO.class));
        ((SIComposite) instance).getField(0).setValue("value");
        return instance;
    }

    @NotNull
    private PetitionInstance createNewPetitionInstance(SInstance instance) {
        PetitionInstance petitionInitial = petitionService.createNewPetitionWithoutSave(null, null, p -> {}, requirementDefinitionEntity);
        petitionInitial.setProcessDefinition(FOOFlowWithTransition.class);

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
