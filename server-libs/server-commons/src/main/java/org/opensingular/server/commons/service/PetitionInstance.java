/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.server.commons.service;

import org.opensingular.flow.core.Flow;
import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.flow.core.ProcessInstance;
import org.opensingular.flow.core.TaskInstance;
import org.opensingular.flow.persistence.entity.ProcessDefinitionEntity;
import org.opensingular.flow.persistence.entity.ProcessInstanceEntity;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.persistence.entity.form.PetitionerEntity;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 *
 * @author Daniel C. Bordin on 07/03/2017.
 */
public class PetitionInstance implements Serializable {

    @Nonnull
    private final PetitionEntity petitionEntity;

    private transient ProcessInstance processInstance;

    private transient SIComposite mainForm;

    public PetitionInstance(PetitionEntity petitionEntity) {
        this.petitionEntity = Objects.requireNonNull(petitionEntity);
    }

    final void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    public ProcessInstance getProcessInstance() {
        if (processInstance == null && petitionEntity.getProcessInstanceEntity() != null) {
            processInstance = PetitionUtil.getProcessInstance(petitionEntity);
        }
        return processInstance;
    }

    public boolean hasProcessInstance() {
        return petitionEntity.getProcessInstanceEntity() != null;
    }

    @Nonnull
    public SIComposite getMainForm() {
        if (mainForm == null) {
            mainForm = getPetitionService().getMainFormAsInstance(petitionEntity);
        }
        return mainForm;
    }

    @Nonnull
    public <I extends SInstance, K extends SType<? extends I>> I getMainForm(@Nonnull Class<K> expectedType) {
        return FormPetitionService.checkIfExpectedType(getMainForm(), expectedType);
    }

    private PetitionService<?,?> getPetitionService() {
        return ApplicationContextProvider.get().getBean(PetitionService.class);
    }

    public ProcessDefinition<?> getProcessDefinition() {
        return PetitionUtil.getProcessDefinition(petitionEntity);
    }

    public Optional<ProcessDefinition<?>> getProcessDefinitionOpt() {
        return PetitionUtil.getProcessDefinitionOpt(petitionEntity);
    }

    public Long getCod() {
        return petitionEntity.getCod();
    }

    public TaskInstance getCurrentTaskOrException() {
        return getProcessInstance().getCurrentTaskOrException();
    }

    public Optional<PetitionInstance> getParentPetition() {
        return Optional.ofNullable(petitionEntity.getParentPetition()).map(
                parent -> ((PetitionService<PetitionEntity,?>) getPetitionService()).newPetitionInstance(parent));
    }

    public String getDescription() {
        return petitionEntity.getDescription();
    }

    public void setNewProcess(ProcessInstance newPrcesssInstance) {
        ProcessInstanceEntity processEntity = newPrcesssInstance.saveEntity();
        petitionEntity.setProcessInstanceEntity(processEntity);
        petitionEntity.setProcessDefinitionEntity(processEntity.getProcessVersion().getProcessDefinition());

    }

    public ProcessInstance startNewProcess(ProcessDefinition processDefinition) {
        processInstance = getPetitionService().startNewProcess(this, processDefinition);
        return processInstance;
    }

    public final PetitionEntity getEntity() {
        return petitionEntity;
    }

    public PetitionerEntity getPetitioner() {
        return petitionEntity.getPetitioner();
    }

    public void setProcessDefinition(Class<? extends ProcessDefinition> clazz) {
        ProcessDefinition<?> processDefinition = Flow.getProcessDefinition(clazz);
        petitionEntity.setProcessDefinitionEntity((ProcessDefinitionEntity) processDefinition.getEntityProcessDefinition());
    }

    public void setDescription(String description) {
        petitionEntity.setDescription(description);
    }
}
