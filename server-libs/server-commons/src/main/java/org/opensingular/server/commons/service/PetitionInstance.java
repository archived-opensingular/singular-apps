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
import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.flow.core.TaskInstance;
import org.opensingular.flow.persistence.entity.ProcessDefinitionEntity;
import org.opensingular.flow.persistence.entity.ProcessInstanceEntity;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.form.SingularFormException;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.persistence.entity.enums.PersonType;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.persistence.entity.form.PetitionerEntity;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Daniel C. Bordin on 07/03/2017.
 */
public class PetitionInstance implements Serializable {

    private final PetitionEntity petitionEntity;

    private transient FlowInstance flowInstance;

    private transient SIComposite mainForm;

    public PetitionInstance(PetitionEntity petitionEntity) {
        this.petitionEntity = Objects.requireNonNull(petitionEntity);
    }

    public FlowInstance getFlowInstance() {
        if (flowInstance == null && petitionEntity.getProcessInstanceEntity() != null) {
            flowInstance = PetitionUtil.getProcessInstance(petitionEntity);
        }
        return flowInstance;
    }

    final void setFlowInstance(FlowInstance flowInstance) {
        this.flowInstance = flowInstance;
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

    @Nonnull
    public <I extends SInstance> I getMainFormAndCast(@Nonnull Class<I> expectedType) {
        SIComposite i = getMainForm();
        if (expectedType.isAssignableFrom(i.getClass())) {
            return (I) i;
        }
        throw new SingularFormException(
                "Era esperado a instância recuperada fosse do tipo " + expectedType.getName() +
                        " mas ela é do tipo " + i.getClass(), i);
    }

    private PetitionService<?, ?> getPetitionService() {
        return ApplicationContextProvider.get().getBean(PetitionService.class);
    }

    public FlowDefinition<?> getProcessDefinition() {
        return PetitionUtil.getProcessDefinition(petitionEntity);
    }

    public void setProcessDefinition(Class<? extends FlowDefinition> clazz) {
        FlowDefinition<?> flowDefinition = Flow.getProcessDefinition(clazz);
        petitionEntity.setProcessDefinitionEntity((ProcessDefinitionEntity) flowDefinition.getEntityProcessDefinition());
    }

    public Optional<FlowDefinition<?>> getProcessDefinitionOpt() {
        return PetitionUtil.getProcessDefinitionOpt(petitionEntity);
    }

    public Long getCod() {
        return petitionEntity.getCod();
    }

    public TaskInstance getCurrentTaskOrException() {
        return getFlowInstance().getCurrentTaskOrException();
    }

    public String getCurrentTaskNameOrException() {
        return getFlowInstance().getCurrentTaskOrException().getName();
    }

    public Optional<PetitionInstance> getParentPetition() {
        return Optional.ofNullable(petitionEntity.getParentPetition()).map(
                parent -> ((PetitionService<PetitionEntity, ?>) getPetitionService()).newPetitionInstance(parent));
    }

    public String getDescription() {
        return petitionEntity.getDescription();
    }

    public void setDescription(String description) {
        petitionEntity.setDescription(description);
    }

    public void setNewProcess(FlowInstance newPrcesssInstance) {
        ProcessInstanceEntity processEntity = newPrcesssInstance.saveEntity();
        petitionEntity.setProcessInstanceEntity(processEntity);
        petitionEntity.setProcessDefinitionEntity(processEntity.getProcessVersion().getProcessDefinition());

    }

    public FlowInstance startNewProcess(FlowDefinition flowDefinition) {
        flowInstance = getPetitionService().startNewProcess(this, flowDefinition);
        return flowInstance;
    }

    public PetitionEntity getEntity() {
        return petitionEntity;
    }

    public PetitionerEntity getPetitioner() {
        return petitionEntity.getPetitioner();
    }

    //TODO REFACTOR
    public String getIdPessoaSeForPessoaJuridica() {
        if (PersonType.JURIDICA == getPetitioner().getPersonType()) {
            return getPetitioner().getIdPessoa();
        }
        return null;
    }

    public FormVersionEntity getMainFormCurrentFormVersion() {
        return petitionEntity.getMainForm().getCurrentFormVersionEntity();
    }

    public Long getMainFormCurrentFormVersionCod() {
        return getMainFormCurrentFormVersion().getCod();
    }

    public String getMainFormTypeName(){
        return getEntity().getMainForm().getFormType().getAbbreviation();
    }

    public String getPetitionerName(){
        return getEntity().getPetitioner().getName();
    }


}