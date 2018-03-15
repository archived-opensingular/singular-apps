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
import org.opensingular.form.persistence.FormKey;
import org.opensingular.form.persistence.entity.FormTypeEntity;
import org.opensingular.form.service.FormService;
import org.opensingular.form.spring.SpringSDocumentFactory;
import org.opensingular.lib.commons.base.SingularException;
import org.opensingular.server.commons.SPackageFOO;
import org.opensingular.server.commons.persistence.dao.form.RequirementDAO;
import org.opensingular.server.commons.persistence.dto.RequirementHistoryDTO;
import org.opensingular.server.commons.persistence.entity.form.RequirementEntity;
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

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.*;

@Transactional
public class RequirementServiceTest extends SingularCommonsBaseTest {

    @Inject
    public RequirementService<RequirementEntity, RequirementInstance> requirementService;

    @Inject
    public RequirementSender requirementSender;

    @Inject
    protected RequirementDAO<RequirementEntity> requirementDAO;

    @Inject
    protected FormService formService;

    @Inject
    SpringSDocumentFactory documentFactory;

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
    public void newRequirementEntity() {
        RequirementEntity requirementEntity = requirementService.newRequirementEntityFor(requirementDefinitionEntity);
        assertNotNull(requirementEntity);
    }

    @Test
    public void newRequirementInstance() {
        RequirementEntity requirementEntity = requirementService.newRequirementEntityFor(requirementDefinitionEntity);
        RequirementInstance requirementInstance = requirementService.newRequirementInstance(requirementEntity);
        assertNotNull(requirementInstance);
        assertEquals(requirementEntity, requirementInstance.getEntity());
    }

    @Test
    public void saveNewRequirement() {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(SPackageFOO.STypeFOO.class));
        RequirementEntity requirementEntity = requirementService.newRequirementEntityFor(requirementDefinitionEntity);
        RequirementInstance requirementInstance = requirementService.newRequirementInstance(requirementEntity);

        requirementService.saveOrUpdate(requirementInstance, instance, true);

        SIComposite mainFormAsInstance = requirementService.getMainFormAsInstance(requirementEntity);
        new AssertionsSInstance(instance).isValueEquals(mainFormAsInstance);
    }

    @Test
    public void sendNewRequirement() {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(SPackageFOO.STypeFOO.class));
        RequirementEntity requirementEntity = requirementService.newRequirementEntityFor(requirementDefinitionEntity);
        RequirementInstance requirementInstance = requirementService.newRequirementInstance(requirementEntity);
        requirementService.saveOrUpdate(requirementInstance, instance, true);
        requirementInstance.setFlowDefinition(FOOFlow.class);

        requirementSender.send(requirementInstance, instance, "vinicius.nunes");
        requirementService.executeTransition("No more bar", requirementInstance, null, null, null);
    }

    @Test
    public void testFindRequirement() {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(SPackageFOO.STypeFOO.class));
        RequirementEntity requirementEntity = requirementService.newRequirementEntityFor(requirementDefinitionEntity);
        RequirementInstance requirementInstance = requirementService.newRequirementInstance(requirementEntity);
        requirementService.saveOrUpdate(requirementInstance, instance, true);

        Optional<RequirementInstance> requirement = requirementService.findRequirement(requirementInstance.getCod());
    }

    @Test
    public void testGetRequirement() {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(SPackageFOO.STypeFOO.class));
        RequirementEntity requirementEntity = requirementService.newRequirementEntityFor(requirementDefinitionEntity);
        RequirementInstance requirementInstance = requirementService.newRequirementInstance(requirementEntity);
        requirementService.saveOrUpdate(requirementInstance, instance, true);

        RequirementInstance requirement = requirementService.getRequirement(requirementInstance.getCod());
        Assert.assertEquals(requirementInstance.getEntity(), requirement.getEntity());
    }

    @Test
    public void testDeleteRequirement() {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(SPackageFOO.STypeFOO.class));
        RequirementEntity requirementEntity = requirementService.newRequirementEntityFor(requirementDefinitionEntity);
        RequirementInstance requirementInstance = requirementService.newRequirementInstance(requirementEntity);
        requirementService.saveOrUpdate(requirementInstance, instance, true);

        requirementService.deleteRequirement(requirementInstance.getCod());
        Assert.assertFalse(requirementService.findRequirement(requirementInstance.getCod()).isPresent());
    }

    @Test
    public void testDeleteRequirementWithRequirementDTO() {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(SPackageFOO.STypeFOO.class));
        RequirementEntity requirementEntity = requirementService.newRequirementEntityFor(requirementDefinitionEntity);
        RequirementInstance requirementInstance = requirementService.newRequirementInstance(requirementEntity);
        requirementService.saveOrUpdate(requirementInstance, instance, true);

        requirementService.deleteRequirement(requirementInstance.getCod());
        Assert.assertFalse(requirementService.findRequirement(requirementInstance.getCod()).isPresent());
    }

    @Test
    public void testListCurrentTaskTransitionsWithEmptyTransitions() {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(SPackageFOO.STypeFOO.class));
        RequirementEntity requirementEntity = requirementService.newRequirementEntityFor(requirementDefinitionEntity);
        RequirementInstance requirementInstance = requirementService.newRequirementInstance(requirementEntity);
        requirementService.saveOrUpdate(requirementInstance, instance, true);

        Assert.assertEquals(0, requirementService.findCurrentTaskInstanceByRequirementId(requirementInstance.getCod())
                .flatMap(TaskInstance::getFlowTask)
                .map(STask::getTransitions)
                .orElse(Collections.emptyList()).size());
    }

    @Test(expected = SingularException.class)
    @Rollback
    public void testGetRequirementException() {
        requirementService.getRequirement((long) 0);
    }

    @Test
    public void quickSearchTests() {

        long                 qtdEnviada       = 0;
        long                 qtdRascunho      = 0;
        List<RequirementEntity> requirementEntities = requirementDAO.listAll();

        for (RequirementEntity requirementEntity : requirementEntities) {
            if (requirementEntity.getFlowInstanceEntity() == null) {
                qtdRascunho++;
            } else {
                qtdEnviada++;
            }
        }

        QuickFilter                     f1    = new QuickFilter();
        List<Map<String, Serializable>> maps1 = requirementService.quickSearchMap(f1);
        assertEquals(qtdEnviada, maps1.size());

        QuickFilter f2 = new QuickFilter();
        f2.withRascunho(true).withSortProperty("description");
        List<Map<String, Serializable>> maps2 = requirementService.quickSearchMap(f2);
        assertEquals(qtdRascunho, maps2.size());

        QuickFilter f3    = new QuickFilter();
        Long        count = requirementService.countQuickSearch(f3);
        assertTrue(count == qtdEnviada);
    }

    @Test
    @Rollback
    public void countTasks() {
        QuickFilter filter = new QuickFilter();
        filter.withFilter("filter");
        filter.withProcessesAbbreviation(Arrays.asList("task1", "task2"));

        SingularPermission permission = new SingularPermission("singularId", "internalId");

        Assert.assertEquals(new Long(0), requirementService.countTasks(filter, Arrays.asList(permission)));
    }

    @Test
    public void listTasks() {
        String description = "Descrição XYZ única - " + System.nanoTime();
        sendRequirement(description);

        QuickFilter filter = new QuickFilter();
        filter.withFilter(description);
        List<Map<String, Serializable>> maps = requirementService.listTasks(filter, Collections.emptyList());

        assertEquals(1, maps.size());
        Map<String, Serializable> task = maps.get(0);
        assertNull(task.get("codUsuarioAlocado"));
        assertNull(task.get("nomeUsuarioAlocado"));
        assertEquals("Do bar", task.get("taskName"));
        assertEquals(TaskType.HUMAN, task.get("taskType"));
        assertEquals("foooooo.StypeFoo", task.get("type"));
        assertEquals(description, task.get("description"));
    }

    public RequirementInstance sendRequirement(String description) {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(SPackageFOO.STypeFOO.class));
        RequirementEntity requirementEntity = requirementService.newRequirementEntityFor(requirementDefinitionEntity);
        RequirementInstance requirementInstance = requirementService.newRequirementInstance(requirementEntity);
        requirementInstance.setDescription(description);
        requirementService.saveOrUpdate(requirementInstance, instance, true);
        requirementInstance.setFlowDefinition(FOOFlow.class);
        requirementSender.send(requirementInstance, instance, "vinicius.nunes");

        return requirementInstance;
    }

    @Test
    public void testSearchs() {
        RequirementInstance requirementInstance = sendRequirement("Descrição XYZ única - " + System.nanoTime());
        FlowInstance flowInstance = requirementInstance.getFlowInstance();

        RequirementInstance p2 = requirementService.getRequirementInstance(flowInstance);
        RequirementInstance p3 = requirementService.getRequirementInstance(flowInstance.getCurrentTaskOrException());
        RequirementInstance p4 = requirementService.getRequirement(flowInstance);
        RequirementInstance p5 = requirementService.getRequirement(flowInstance.getCurrentTaskOrException());

        assertEquals(requirementInstance.getCod(), p2.getCod());
        assertEquals(requirementInstance.getCod(), p3.getCod());
        assertEquals(requirementInstance.getCod(), p4.getCod());
        assertEquals(requirementInstance.getCod(), p5.getCod());
    }

    @Test
    public void createRequirementWithoutSave() {
        RequirementInstance parent = sendRequirement("Descrição XYZ única - " + System.nanoTime());

        RequirementInstance requirement = requirementService.createNewRequirementWithoutSave(FOOFlow.class, parent, RequirementInstance::getCod, requirementDefinitionEntity);

        assertNull(requirement.getCod());
    }

    @Test
    public void searchRequirementHistory() {
        RequirementInstance requirement  = sendRequirement("Descrição XYZ única - " + System.nanoTime());
        List<RequirementHistoryDTO> histories = requirementService.listRequirementContentHistoryByCodRequirement(requirement.getCod());

        assertTrue(histories.isEmpty());
    }

    @Test
    public void previousTransition() {
        RequirementInstance requirement             = sendRequirement("Descrição XYZ única - " + System.nanoTime());
        boolean          isPreviousTransition = requirementService.isPreviousTransition(requirement.getCurrentTaskOrException(), "teste");

        assertFalse(isPreviousTransition);
    }

    @Test
    public void findLastFormInstanceByType() {
        RequirementInstance requirement                  = sendRequirement("Descrição XYZ única - " + System.nanoTime());
        Optional<SIComposite> lastFormInstanceByType = requirementService.findLastFormInstanceByType(requirement, SPackageFOO.STypeFOO.class);

        assertTrue(lastFormInstanceByType.isPresent());
    }

    @Test
    public void findLastFormInstanceByTypes() {
        RequirementInstance requirement                  = sendRequirement("Descrição XYZ única - " + System.nanoTime());
        Optional<SIComposite> lastFormInstanceByType = requirementService.findLastFormInstanceByType(requirement, Collections.singletonList(SPackageFOO.STypeFOO.class));

        assertTrue(lastFormInstanceByType.isPresent());
    }

    @Test
    public void updateRequirement() {
        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(SPackageFOO.STypeFOO.class));
        ((SIComposite) instance).setValue(SPackageFOO.STypeFOO.FIELD_NOME, "Teste");
        RequirementEntity requirementEntity = requirementService.newRequirementEntityFor(requirementDefinitionEntity);
        RequirementInstance requirementInstance = requirementService.newRequirementInstance(requirementEntity);

        FormKey formKey = requirementService.saveOrUpdate(requirementInstance, instance, true);

        SInstance firstVersion = formService.loadSInstance(formKey, RefType.of(SPackageFOO.STypeFOO.class), documentFactory);
        firstVersion.getDocument().setLastId(firstVersion.getDocument().getLastId() + 1000);

        FormKey secondFormKey = requirementService.saveOrUpdate(requirementInstance, firstVersion, true);

        SInstance secondVersion = formService.loadSInstance(secondFormKey, RefType.of(SPackageFOO.STypeFOO.class), documentFactory);

        assertThat("Segunda versão não pode ter o lastId menor que a primeira versão",
                secondVersion.getDocument().getLastId(), greaterThanOrEqualTo(firstVersion.getDocument().getLastId()));
    }
}
