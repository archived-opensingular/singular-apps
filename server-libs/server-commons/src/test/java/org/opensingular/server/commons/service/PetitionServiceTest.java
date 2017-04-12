package org.opensingular.server.commons.service;

import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.document.RefSDocumentFactory;
import org.opensingular.form.document.RefType;
import org.opensingular.form.document.SDocumentFactory;
import org.opensingular.form.helpers.AssertionsSInstance;
import org.opensingular.form.persistence.entity.FormTypeEntity;
import org.opensingular.server.commons.STypeFOO;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.test.FOOFlow;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;

import static org.junit.Assert.*;

@Transactional
public class PetitionServiceTest extends SingularCommonsBaseTest {

    @Inject
    public PetitionService<PetitionEntity, PetitionInstance> petitionService;

    @Inject
    public SessionFactory sessionFactory;

    @Test
    public void testName() throws Exception {
        Session s = sessionFactory.openSession();
        org.hibernate.Transaction t = s.beginTransaction();
        FormTypeEntity formTypeEntity = new FormTypeEntity();
        formTypeEntity.setAbbreviation("nada");
        formTypeEntity.setLabel("nada");
        formTypeEntity.setCacheVersionNumber(1l);
        s.saveOrUpdate(formTypeEntity);
        s.flush();
        t.commit();

    }

    @Test
    public void newPetitionEntity() {
        PetitionEntity petitionEntity = petitionService.newPetitionEntity();
        assertNotNull(petitionEntity);
    }

    @Test
    public void newPetitionInstance() {
        PetitionEntity petitionEntity = petitionService.newPetitionEntity();
        PetitionInstance petitionInstance = petitionService.newPetitionInstance(petitionEntity);
        assertNotNull(petitionInstance);
        assertEquals(petitionEntity, petitionInstance.getEntity());
    }

    @Test
    public void saveNewPetition() {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance instance = documentFactoryRef.get().createInstance(RefType.of(STypeFOO.class));
        PetitionEntity petitionEntity = petitionService.newPetitionEntity();
        PetitionInstance petitionInstance = petitionService.newPetitionInstance(petitionEntity);

        petitionService.saveOrUpdate(petitionInstance, instance, true);

        SIComposite mainFormAsInstance = petitionService.getMainFormAsInstance(petitionEntity);
        new AssertionsSInstance(instance).isValueEquals(mainFormAsInstance);
    }

    @Test
    public void sendNewPetition() {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance instance = documentFactoryRef.get().createInstance(RefType.of(STypeFOO.class));
        PetitionEntity petitionEntity = petitionService.newPetitionEntity();
        PetitionInstance petitionInstance = petitionService.newPetitionInstance(petitionEntity);
        petitionService.saveOrUpdate(petitionInstance, instance, true);
        petitionInstance.setProcessDefinition(FOOFlow.class);

        petitionService.send(petitionInstance, instance, "vinicius.nunes");
    }

    @Test
    public void testFindPetition() {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance instance = documentFactoryRef.get().createInstance(RefType.of(STypeFOO.class));
        PetitionEntity petitionEntity = petitionService.newPetitionEntity();
        PetitionInstance petitionInstance = petitionService.newPetitionInstance(petitionEntity);
        petitionService.saveOrUpdate(petitionInstance, instance, true);

        Optional<PetitionInstance> petition = petitionService.findPetition(petitionInstance.getCod());
    }
}
