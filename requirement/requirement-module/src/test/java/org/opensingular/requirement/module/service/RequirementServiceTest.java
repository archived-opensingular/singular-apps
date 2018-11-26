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
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.flow.core.STask;
import org.opensingular.flow.core.TaskInstance;
import org.opensingular.flow.core.TaskType;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.document.RefSDocumentFactory;
import org.opensingular.form.document.SDocumentFactory;
import org.opensingular.form.helpers.AssertionsSInstance;
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
import org.opensingular.singular.pet.module.foobar.stuff.STypeFoo;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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


    @Test
    public void newRequirementEntity() {
        RequirementEntity requirementEntity = getRequirementDefinition().newRequirement("user").getEntity();
        assertNotNull(requirementEntity);
    }

    @Test
    public void newRequirementInstance() {
        RequirementInstance requirementInstance = getRequirementDefinition().newRequirement("user");
        assertNotNull(requirementInstance);
    }

    @Test
    public void saveNewRequirement() {
        RequirementInstance requirementInstance = getRequirementDefinition().newRequirement("user");
        SInstance           instance            = requirementInstance.newForm();
        requirementInstance.saveForm(instance);

        SIComposite mainFormAsInstance = requirementService.getMainFormAsInstance(requirementInstance.getEntity());
        new AssertionsSInstance(instance).isValueEquals(mainFormAsInstance);
    }


    @Test
    public void testFindRequirement() {
        RequirementInstance requirementInstance = createAndSaveRequirement();
        Assert.assertEquals(requirementInstance.getCod(), requirementService.loadRequirementInstance(requirementInstance.getCod()).getCod());
    }

    @Test
    public void testGetRequirement() {
        RequirementInstance requirementInstance = createAndSaveRequirement();
        RequirementInstance requirement = requirementService.loadRequirementInstance(requirementInstance.getCod());
        Assert.assertEquals(requirementInstance.getEntity(), requirement.getEntity());
    }

    @Test
    public void testDeleteRequirement() {
        RequirementInstance requirementInstance = createAndSaveRequirement();
        requirementService.deleteRequirement(requirementInstance.getCod());
        Assert.assertFalse(requirementService.findRequirementEntity(requirementInstance.getCod()).isPresent());
    }

    private RequirementInstance createAndSaveRequirement() {
        RequirementInstance requirementInstance = getRequirementDefinition().newRequirement("user");
        SInstance           instance            = requirementInstance.newForm();
        requirementInstance.saveForm(instance);
        return requirementInstance;
    }

    @Test
    public void testDeleteRequirementWithRequirementDTO() {
        RequirementInstance requirementInstance = createAndSaveRequirement();

        requirementService.deleteRequirement(requirementInstance.getCod());
        Assert.assertFalse(requirementService.findRequirementEntity(requirementInstance.getCod()).isPresent());
    }

    @Test
    public void testListCurrentTaskTransitionsWithEmptyTransitions() {
        RequirementInstance requirementInstance = createAndSaveRequirement();

        Assert.assertEquals(0, requirementService.findCurrentTaskInstanceByRequirementId(requirementInstance.getCod())
                .flatMap(TaskInstance::getFlowTask)
                .map(STask::getTransitions)
                .orElse(Collections.emptyList()).size());
    }

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

        BoxFilter                       f1    = new BoxFilter();
        List<Map<String, Serializable>> maps1 = requirementService.quickSearchMap(f1);
        assertEquals(qtdEnviada, maps1.size());

        BoxFilter f2 = new BoxFilter();
        f2.showDraft(true).sortProperty("description");
        List<Map<String, Serializable>> maps2 = requirementService.quickSearchMap(f2);
        assertEquals(qtdRascunho, maps2.size());

        BoxFilter f3    = new BoxFilter();
        Long      count = requirementService.countQuickSearch(f3);
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
        String ator = "user";
        sendRequirement(ator);

        BoxFilter filter = new BoxFilter();
        filter.filter(ator);
        List<Map<String, Serializable>> maps = requirementService.listTasks(filter, Collections.emptyList());

        assertEquals(1, maps.size());
        Map<String, Serializable> task = maps.get(0);
        assertNull(task.get("codUsuarioAlocado"));
        assertNull(task.get("nomeUsuarioAlocado"));
        assertEquals("Start bar", task.get("taskName"));
        assertEquals(TaskType.HUMAN, task.get("taskType"));
        assertEquals("foooooo.STypeFoo", task.get("type"));
        assertEquals(ator, task.get("solicitante"));
    }

    public RequirementInstance sendRequirement(String codAtor) {
        RequirementInstance instance = createAndSaveRequirement();
        instance.send(codAtor);
        return instance;
    }

    @Test
    public void testSearchs() {
        RequirementInstance requirementInstance = sendRequirement("Descrição XYZ única - " + System.nanoTime());
        FlowInstance        flowInstance        = requirementInstance.getFlowInstance();

        RequirementInstance p2 = requirementService.getRequirementInstance(flowInstance);
        RequirementInstance p4 = requirementService.getRequirementInstance(flowInstance);

        assertEquals(requirementInstance.getCod(), p2.getCod());
        assertEquals(requirementInstance.getCod(), p4.getCod());
    }

    @Test
    public void createRequirementWithoutSave() {
        RequirementInstance parent = sendRequirement("Descrição XYZ única - " + System.nanoTime());

        RequirementInstance requirement = getRequirementDefinition().newRequirement("user", parent);

        assertNull(requirement.getCod());
    }


}
