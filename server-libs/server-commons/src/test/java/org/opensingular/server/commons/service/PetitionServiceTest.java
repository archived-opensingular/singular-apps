package org.opensingular.server.commons.service;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.flow.core.STask;
import org.opensingular.flow.core.TaskInstance;
import org.opensingular.flow.core.TaskType;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.document.RefSDocumentFactory;
import org.opensingular.form.document.RefType;
import org.opensingular.form.document.SDocumentFactory;
import org.opensingular.form.helpers.AssertionsSInstance;
import org.opensingular.form.persistence.entity.FormTypeEntity;
import org.opensingular.lib.commons.base.SingularException;
import org.opensingular.server.commons.STypeFOO;
import org.opensingular.server.commons.persistence.dao.form.PetitionDAO;
import org.opensingular.server.commons.persistence.dto.PetitionDTO;
import org.opensingular.server.commons.persistence.dto.PetitionHistoryDTO;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.spring.security.SingularPermission;
import org.opensingular.server.commons.test.FOOFlow;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.springframework.test.annotation.Rollback;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@Transactional
public class PetitionServiceTest extends SingularCommonsBaseTest {

    @Inject
    public PetitionService<PetitionEntity, PetitionInstance> petitionService;

    @Inject
    public PetitionSender petitionSender;

    @Inject
    protected PetitionDAO<PetitionEntity> petitionDAO;

    @Test
    public void testName() throws Exception {
        Session                   s              = sessionFactory.openSession();
        org.hibernate.Transaction t              = s.beginTransaction();
        FormTypeEntity            formTypeEntity = new FormTypeEntity();
        formTypeEntity.setAbbreviation("nada");
        formTypeEntity.setLabel("nada");
        formTypeEntity.setCacheVersionNumber(1l);
        s.saveOrUpdate(formTypeEntity);
        s.flush();
        t.commit();

    }

    @Test
    public void newPetitionEntity() {
        PetitionEntity petitionEntity = petitionService.newPetitionEntityFor(requirementDefinitionEntity);
        assertNotNull(petitionEntity);
    }

    @Test
    public void newPetitionInstance() {
        PetitionEntity   petitionEntity   = petitionService.newPetitionEntityFor(requirementDefinitionEntity);
        PetitionInstance petitionInstance = petitionService.newPetitionInstance(petitionEntity);
        assertNotNull(petitionInstance);
        assertEquals(petitionEntity, petitionInstance.getEntity());
    }

    @Test
    public void saveNewPetition() {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(STypeFOO.class));
        PetitionEntity      petitionEntity     = petitionService.newPetitionEntityFor(requirementDefinitionEntity);
        PetitionInstance    petitionInstance   = petitionService.newPetitionInstance(petitionEntity);

        petitionService.saveOrUpdate(petitionInstance, instance, true);

        SIComposite mainFormAsInstance = petitionService.getMainFormAsInstance(petitionEntity);
        new AssertionsSInstance(instance).isValueEquals(mainFormAsInstance);
    }

    @Test
    public void sendNewPetition() {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(STypeFOO.class));
        PetitionEntity      petitionEntity     = petitionService.newPetitionEntityFor(requirementDefinitionEntity);
        PetitionInstance    petitionInstance   = petitionService.newPetitionInstance(petitionEntity);
        petitionService.saveOrUpdate(petitionInstance, instance, true);
        petitionInstance.setFlowDefinition(FOOFlow.class);

        petitionSender.send(petitionInstance, instance, "vinicius.nunes");
        petitionService.executeTransition("No more bar", petitionInstance, null, null, null);
    }

    @Test
    public void testFindPetition() {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(STypeFOO.class));
        PetitionEntity      petitionEntity     = petitionService.newPetitionEntityFor(requirementDefinitionEntity);
        PetitionInstance    petitionInstance   = petitionService.newPetitionInstance(petitionEntity);
        petitionService.saveOrUpdate(petitionInstance, instance, true);

        Optional<PetitionInstance> petition = petitionService.findPetition(petitionInstance.getCod());
    }

    @Test
    public void testGetPetition() {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(STypeFOO.class));
        PetitionEntity      petitionEntity     = petitionService.newPetitionEntityFor(requirementDefinitionEntity);
        PetitionInstance    petitionInstance   = petitionService.newPetitionInstance(petitionEntity);
        petitionService.saveOrUpdate(petitionInstance, instance, true);

        PetitionInstance petition = petitionService.getPetition(petitionInstance.getCod());
        Assert.assertEquals(petitionInstance.getEntity(), petition.getEntity());
    }

    @Test
    public void testDeletePetition() {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(STypeFOO.class));
        PetitionEntity      petitionEntity     = petitionService.newPetitionEntityFor(requirementDefinitionEntity);
        PetitionInstance    petitionInstance   = petitionService.newPetitionInstance(petitionEntity);
        petitionService.saveOrUpdate(petitionInstance, instance, true);

        petitionService.deletePetition(petitionInstance.getCod());
        Assert.assertFalse(petitionService.findPetition(petitionInstance.getCod()).isPresent());
    }

    @Test
    public void testDeletePetitionWithPetitionDTO() {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(STypeFOO.class));
        PetitionEntity      petitionEntity     = petitionService.newPetitionEntityFor(requirementDefinitionEntity);
        PetitionInstance    petitionInstance   = petitionService.newPetitionInstance(petitionEntity);
        petitionService.saveOrUpdate(petitionInstance, instance, true);

        PetitionDTO dto = new PetitionDTO();
        dto.setCodPeticao(petitionInstance.getCod());

        petitionService.deletePetition(dto);
        Assert.assertFalse(petitionService.findPetition(petitionInstance.getCod()).isPresent());
    }

    @Test
    public void testListCurrentTaskTransitionsWithEmptyTransitions() {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(STypeFOO.class));
        PetitionEntity      petitionEntity     = petitionService.newPetitionEntityFor(requirementDefinitionEntity);
        PetitionInstance    petitionInstance   = petitionService.newPetitionInstance(petitionEntity);
        petitionService.saveOrUpdate(petitionInstance, instance, true);

        Assert.assertEquals(0, petitionService.findCurrentTaskInstanceByPetitionId(petitionInstance.getCod())
                .flatMap(TaskInstance::getFlowTask)
                .map(STask::getTransitions)
                .orElse(Collections.emptyList()).size());
    }

    @Test(expected = SingularException.class)
    @Rollback
    public void testGetPetitionException() {
        petitionService.getPetition((long) 0);
    }

    @Test
    public void quickSearchTests() {

        long                 qtdEnviada       = 0;
        long                 qtdRascunho      = 0;
        List<PetitionEntity> petitionEntities = petitionDAO.listAll();

        for (PetitionEntity petitionEntity : petitionEntities) {
            if (petitionEntity.getFlowInstanceEntity() == null) {
                qtdRascunho++;
            } else {
                qtdEnviada++;
            }
        }

        QuickFilter                     f1    = new QuickFilter();
        List<Map<String, Serializable>> maps1 = petitionService.quickSearchMap(f1);
        assertEquals(qtdEnviada, maps1.size());

        QuickFilter f2 = new QuickFilter();
        f2.withRascunho(true).withSortProperty("description");
        List<Map<String, Serializable>> maps2 = petitionService.quickSearchMap(f2);
        assertEquals(qtdRascunho, maps2.size());

        QuickFilter f3    = new QuickFilter();
        Long        count = petitionService.countQuickSearch(f3);
        assertTrue(count == qtdEnviada);
    }

    @Test
    @Rollback
    public void countTasks() {
        QuickFilter filter = new QuickFilter();
        filter.withFilter("filter");
        filter.withProcessesAbbreviation(Arrays.asList("task1", "task2"));

        SingularPermission permission = new SingularPermission("singularId", "internalId");

        Assert.assertEquals(new Long(0), petitionService.countTasks(filter, Arrays.asList(permission)));
    }

    @Test
    public void listTasks() {
        String description = "Descrição XYZ única - " + System.nanoTime();
        sendPetition(description);

        QuickFilter filter = new QuickFilter();
        filter.withFilter(description);
        List<Map<String, Serializable>> maps = petitionService.listTasks(filter, Collections.emptyList());

        assertEquals(1, maps.size());
        Map<String, Serializable> task = maps.get(0);
        assertNull(task.get("codUsuarioAlocado"));
        assertNull(task.get("nomeUsuarioAlocado"));
        assertEquals("Do bar", task.get("taskName"));
        assertEquals(TaskType.HUMAN, task.get("taskType"));
        assertEquals("foooooo.StypeFoo", task.get("type"));
        assertEquals(description, task.get("description"));
    }

    public PetitionInstance sendPetition(String description) {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(STypeFOO.class));
        PetitionEntity      petitionEntity     = petitionService.newPetitionEntityFor(requirementDefinitionEntity);
        PetitionInstance    petitionInstance   = petitionService.newPetitionInstance(petitionEntity);
        petitionInstance.setDescription(description);
        petitionService.saveOrUpdate(petitionInstance, instance, true);
        petitionInstance.setFlowDefinition(FOOFlow.class);
        petitionSender.send(petitionInstance, instance, "vinicius.nunes");

        return petitionInstance;
    }

    @Test
    public void testSearchs() {
        PetitionInstance petitionInstance = sendPetition("Descrição XYZ única - " + System.nanoTime());
        FlowInstance flowInstance = petitionInstance.getFlowInstance();

        PetitionInstance p2 = petitionService.getPetitionInstance(flowInstance);
        PetitionInstance p3 = petitionService.getPetitionInstance(flowInstance.getCurrentTaskOrException());
        PetitionInstance p4 = petitionService.getPetition(flowInstance);
        PetitionInstance p5 = petitionService.getPetition(flowInstance.getCurrentTaskOrException());

        assertEquals(petitionInstance.getCod(), p2.getCod());
        assertEquals(petitionInstance.getCod(), p3.getCod());
        assertEquals(petitionInstance.getCod(), p4.getCod());
        assertEquals(petitionInstance.getCod(), p5.getCod());
    }

    @Test
    public void createPetitionWithoutSave() {
        PetitionInstance parent = sendPetition("Descrição XYZ única - " + System.nanoTime());

        PetitionInstance petition = petitionService.createNewPetitionWithoutSave(FOOFlow.class, parent, PetitionInstance::getCod, requirementDefinitionEntity);

        assertNull(petition.getCod());
    }

    @Test
    public void searchPetitionHistory() {
        PetitionInstance         petition  = sendPetition("Descrição XYZ única - " + System.nanoTime());
        List<PetitionHistoryDTO> histories = petitionService.listPetitionContentHistoryByPetitionCod(petition.getCod(), "", true);

        assertTrue(histories.isEmpty());
    }

    @Test
    public void previousTransition() {
        PetitionInstance petition             = sendPetition("Descrição XYZ única - " + System.nanoTime());
        boolean          isPreviousTransition = petitionService.isPreviousTransition(petition.getCurrentTaskOrException(), "teste");

        assertFalse(isPreviousTransition);
    }

    @Test
    public void findLastestFormInstanceByType() {
        PetitionInstance      petition                  = sendPetition("Descrição XYZ única - " + System.nanoTime());
        Optional<SIComposite> lastestFormInstanceByType = petitionService.findLastestFormInstanceByType(petition, STypeFOO.class);

        assertTrue(lastestFormInstanceByType.isPresent());
    }

    @Test
    public void findLastestFormInstanceByTypes() {
        PetitionInstance      petition                  = sendPetition("Descrição XYZ única - " + System.nanoTime());
        Optional<SIComposite> lastestFormInstanceByType = petitionService.findLastestFormInstanceByType(petition, Collections.singletonList(STypeFOO.class));

        assertTrue(lastestFormInstanceByType.isPresent());
    }
}
