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

        PetitionInstance petitionInitial = petitionService.createNewPetitionWithoutSave(null, null, p -> {});
        petitionInitial.setProcessDefinition(FOOFlowWithTransition.class);

        petitionService.saveOrUpdate(petitionInitial, instance, true);

        Assert.assertNotNull(petitionInitial.getMainForm());

        Assert.assertTrue(petitionInitial.getMainForm(STypeFOO.class).getType() instanceof STypeFOO);

        Assert.assertNotNull(petitionInitial.getMainFormAndCast(SIComposite.class));

        Assert.assertNotNull(petitionInitial.getProcessDefinition());

        Assert.assertTrue(petitionInitial.getProcessDefinitionOpt().isPresent());

        Assert.assertFalse(petitionInitial.getParentPetition().isPresent());

        Assert.assertNotNull(petitionInitial.getMainFormCurrentFormVersion());

        Assert.assertEquals(new Long(1), petitionInitial.getMainFormCurrentFormVersionCod());

        Assert.assertEquals(STypeFOO.FULL_NAME, petitionInitial.getMainFormTypeName());
    }
}
