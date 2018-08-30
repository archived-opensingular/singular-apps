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

package org.opensingular.requirement.module.service;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;
import org.opensingular.flow.core.TaskType;
import org.opensingular.form.persistence.entity.FormTypeEntity;
import org.opensingular.form.service.FormService;
import org.opensingular.form.spring.SpringSDocumentFactory;
import org.opensingular.lib.commons.base.SingularException;
import org.opensingular.requirement.commons.SingularCommonsBaseTest;
import org.opensingular.requirement.module.persistence.dao.form.RequirementDAO;
import org.opensingular.requirement.module.persistence.dao.form.RequirementDefinitionDAO;
import org.opensingular.requirement.module.persistence.entity.form.RequirementDefinitionEntity;
import org.opensingular.requirement.module.persistence.entity.form.RequirementEntity;
import org.opensingular.requirement.module.persistence.filter.BoxFilter;
import org.opensingular.requirement.module.spring.security.SingularPermission;
import org.springframework.test.annotation.Rollback;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@Transactional
public class RequirementServiceTest extends SingularCommonsBaseTest {

    @Inject
    public RequirementService requirementService;

    @Inject
    protected RequirementDAO requirementDAO;

    @Inject
    protected FormService formService;

    @Inject
    private RequirementDefinitionDAO<RequirementDefinitionEntity> requirementDefinitionDAO;

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

    //TODO reqdef
//
//    @Test
//    public void newRequirementEntity() {
//        RequirementEntity requirementEntity = requirementService.newRequirementEntityFor(getRequirementDefinition());
//        assertNotNull(requirementEntity);
//    }
//
//    @Test
//    public void newRequirementInstance() {
//        RequirementEntity requirementEntity = requirementService.newRequirementEntityFor(getRequirementDefinition());
//        RequirementInstance requirementInstance = requirementService.newRequirementInstance(requirementEntity);
//        assertNotNull(requirementInstance);
//        assertEquals(requirementEntity, requirementInstance.getEntity());
//    }
//
//    @Test
//    public void saveNewRequirement() {
//        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
//        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(STypeFoo.class));
//        RequirementEntity requirementEntity = requirementService.newRequirementEntityFor(getRequirementDefinition());
//        RequirementInstance requirementInstance = requirementService.newRequirementInstance(requirementEntity);
//
//        requirementService.saveOrUpdate(requirementInstance, instance, true);
//
//        SIComposite mainFormAsInstance = requirementService.getMainFormAsInstance(requirementEntity);
//        new AssertionsSInstance(instance).isValueEquals(mainFormAsInstance);
//    }
//

//
//    @Test
//    public void testFindRequirement() {
//        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
//        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(STypeFoo.class));
//        RequirementEntity requirementEntity = requirementService.newRequirementEntityFor(getRequirementDefinition());
//        RequirementInstance requirementInstance = requirementService.newRequirementInstance(requirementEntity);
//        requirementService.saveOrUpdate(requirementInstance, instance, true);
//
//        Optional<RequirementInstance> requirement = requirementService.findRequirementEntity(requirementInstance.getCod());
//    }
//
//    @Test
//    public void testGetRequirement() {
//        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
//        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(STypeFoo.class));
//        RequirementEntity requirementEntity = requirementService.newRequirementEntityFor(getRequirementDefinition());
//        RequirementInstance requirementInstance = requirementService.newRequirementInstance(requirementEntity);
//        requirementService.saveOrUpdate(requirementInstance, instance, true);
//
//        RequirementInstance requirement = requirementService.getRequirementEntity(requirementInstance.getCod());
//        Assert.assertEquals(requirementInstance.getEntity(), requirement.getEntity());
//    }
//
//    @Test
//    public void testDeleteRequirement() {
//        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
//        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(STypeFoo.class));
//        RequirementEntity requirementEntity = requirementService.newRequirementEntityFor(getRequirementDefinition());
//        RequirementInstance requirementInstance = requirementService.newRequirementInstance(requirementEntity);
//        requirementService.saveOrUpdate(requirementInstance, instance, true);
//
//        requirementService.deleteRequirement(requirementInstance.getCod());
//        Assert.assertFalse(requirementService.findRequirementEntity(requirementInstance.getCod()).isPresent());
//    }
//
//    @Test
//    public void testDeleteRequirementWithRequirementDTO() {
//        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
//        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(STypeFoo.class));
//        RequirementEntity requirementEntity = requirementService.newRequirementEntityFor(getRequirementDefinition());
//        RequirementInstance requirementInstance = requirementService.newRequirementInstance(requirementEntity);
//        requirementService.saveOrUpdate(requirementInstance, instance, true);
//
//        requirementService.deleteRequirement(requirementInstance.getCod());
//        Assert.assertFalse(requirementService.findRequirementEntity(requirementInstance.getCod()).isPresent());
//    }
//
//    @Test
//    public void testListCurrentTaskTransitionsWithEmptyTransitions() {
//        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
//        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(STypeFoo.class));
//        RequirementEntity requirementEntity = requirementService.newRequirementEntityFor(getRequirementDefinition());
//        RequirementInstance requirementInstance = requirementService.newRequirementInstance(requirementEntity);
//        requirementService.saveOrUpdate(requirementInstance, instance, true);
//
//        Assert.assertEquals(0, requirementService.findCurrentTaskInstanceByRequirementId(requirementInstance.getCod())
//                .flatMap(TaskInstance::getFlowTask)
//                .map(STask::getTransitions)
//                .orElse(Collections.emptyList()).size());
//    }

    @Test(expected = SingularException.class)
    @Rollback
    public void testGetRequirementException() {
        requirementService.getRequirementEntity((long) 0);
    }

    @Test
    public void quickSearchTests() {

        long                    qtdEnviada          = 0;
        long                    qtdRascunho         = 0;
        List<RequirementEntity> requirementEntities = requirementDAO.listAll();

        for (RequirementEntity requirementEntity : requirementEntities) {
            if (requirementEntity.getFlowInstanceEntity() == null) {
                qtdRascunho++;
            } else {
                qtdEnviada++;
            }
        }

        BoxFilter f1    = new BoxFilter();
        List<Map<String, Serializable>> maps1 = requirementService.quickSearchMap(f1);
        assertEquals(qtdEnviada, maps1.size());

        BoxFilter f2 = new BoxFilter();
        f2.showDraft(true).sortProperty("description");
        List<Map<String, Serializable>> maps2 = requirementService.quickSearchMap(f2);
        assertEquals(qtdRascunho, maps2.size());

        BoxFilter f3    = new BoxFilter();
        Long        count = requirementService.countQuickSearch(f3);
        assertTrue(count == qtdEnviada);
    }

    @Test
    @Rollback
    public void countTasks() {
        BoxFilter filter = new BoxFilter();
        filter.filter("filter");
        filter.processesAbbreviation(Arrays.asList("task1", "task2"));

        SingularPermission permission = new SingularPermission("singularId", "internalId");

        Assert.assertEquals(new Long(0), requirementService.countTasks(filter, Arrays.asList(permission)));
    }

    @Test
    public void listTasks() {
        String description = "Descrição XYZ única - " + System.nanoTime();
        sendRequirement(description);

        BoxFilter filter = new BoxFilter();
        filter.filter(description);
        List<Map<String, Serializable>> maps = requirementService.listTasks(filter, Collections.emptyList());

        assertEquals(1, maps.size());
        Map<String, Serializable> task = maps.get(0);
        assertNull(task.get("codUsuarioAlocado"));
        assertNull(task.get("nomeUsuarioAlocado"));
        assertEquals("Do bar", task.get("taskName"));
        assertEquals(TaskType.HUMAN, task.get("taskType"));
        assertEquals("foooooo.STypeFoo", task.get("type"));
        assertEquals(description, task.get("description"));
    }

    public RequirementInstance sendRequirement(String description) {
//        RefSDocumentFactory documentFactoryRef = SDocumentFactory.empty().getDocumentFactoryRef();
//        SInstance           instance           = documentFactoryRef.get().createInstance(RefType.of(STypeFoo.class));
//        RequirementEntity requirementEntity = requirementService.newRequirementEntityFor(getRequirementDefinition());
//        RequirementInstance requirementInstance = requirementService.newRequirementInstance(requirementEntity);
//        requirementInstance.setDescription(description);
//        requirementService.saveOrUpdate(requirementInstance, instance, true);
//        requirementInstance.setFlowDefinition(FOOFlow.class);
//        requirementSender.send(requirementInstance, instance, "vinicius.nunes");
//
//        return requirementInstance;
        return null;
    }
//
//    @Test
//    public void testSearchs() {
//        RequirementInstance requirementInstance = sendRequirement("Descrição XYZ única - " + System.nanoTime());
//        FlowInstance flowInstance = requirementInstance.getFlowInstance();
//
//        RequirementInstance p2 = requirementService.getRequirementInstance(flowInstance);
//        RequirementInstance p3 = requirementService.getRequirementInstance(flowInstance.getCurrentTaskOrException());
//        RequirementInstance p4 = requirementService.getRequirementEntity(flowInstance);
//        RequirementInstance p5 = requirementService.getRequirementEntity(flowInstance.getCurrentTaskOrException());
//
//        assertEquals(requirementInstance.getCod(), p2.getCod());
//        assertEquals(requirementInstance.getCod(), p3.getCod());
//        assertEquals(requirementInstance.getCod(), p4.getCod());
//        assertEquals(requirementInstance.getCod(), p5.getCod());
//    }
//
//    @Test
//    public void createRequirementWithoutSave() {
//        RequirementInstance parent = sendRequirement("Descrição XYZ única - " + System.nanoTime());
//
//        RequirementInstance requirement = requirementService.createNewRequirementWithoutSave(FOOFlow.class, parent, RequirementInstance::getCod, getRequirementDefinition());
//
//        assertNull(requirement.getCod());
//    }


}
