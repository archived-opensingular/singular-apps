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
import org.opensingular.flow.persistence.entity.FlowDefinitionEntity;
import org.opensingular.flow.persistence.entity.FlowInstanceEntity;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.form.SingularFormException;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.exception.SingularRequirementException;
import org.opensingular.server.commons.persistence.entity.enums.PersonType;
import org.opensingular.server.commons.persistence.entity.form.ApplicantEntity;
import org.opensingular.server.commons.persistence.entity.form.RequirementEntity;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Daniel C. Bordin on 07/03/2017.
 */
public class RequirementInstance implements Serializable {

    private final RequirementEntity requirementEntity;

    private transient FlowInstance flowInstance;

    private transient SIComposite mainForm;

    public RequirementInstance(RequirementEntity requirementEntity) {
        this.requirementEntity = Objects.requireNonNull(requirementEntity);
    }

    public FlowInstance getFlowInstance() {
        if (flowInstance == null && requirementEntity.getFlowInstanceEntity() != null) {
            flowInstance = RequirementUtil.getFlowInstance(requirementEntity);
        }
        return flowInstance;
    }

    final void setFlowInstance(FlowInstance flowInstance) {
        this.flowInstance = flowInstance;
    }

    public boolean isFlowInstanceCreated() {
        return requirementEntity.getFlowInstanceEntity() != null;
    }

    @Nonnull
    public SIComposite getMainForm() {
        if (mainForm == null) {
            mainForm = getRequirementService().getMainFormAsInstance(requirementEntity);
        }
        return mainForm;
    }

    @Nonnull
    public <I extends SInstance, K extends SType<? extends I>> I getMainForm(@Nonnull Class<K> expectedType) {
        return FormRequirementService.checkIfExpectedType(getMainForm(), expectedType);
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

    @Nonnull
    private RequirementService<?, ?> getRequirementService() {
        return ApplicationContextProvider.get().getBean(RequirementService.class);
    }

    public FlowDefinition<?> getFlowDefinition() {
        return RequirementUtil.getFlowDefinition(requirementEntity);
    }

    public void setFlowDefinition(@Nonnull Class<? extends FlowDefinition> clazz) {
        FlowDefinition<?> flowDefinition = Flow.getFlowDefinition(clazz);
        requirementEntity.setFlowDefinitionEntity((FlowDefinitionEntity) flowDefinition.getEntityFlowDefinition());
    }

    public Optional<FlowDefinition<?>> getFlowDefinitionOpt() {
        return RequirementUtil.getFlowDefinitionOpt(requirementEntity);
    }

    public Long getCod() {
        return requirementEntity.getCod();
    }

    public TaskInstance getCurrentTaskOrException() {
        return getFlowInstance().getCurrentTaskOrException();
    }

    public String getCurrentTaskNameOrException() {
        return getFlowInstance().getCurrentTaskOrException().getName();
    }

    @Nonnull
    public Optional<RequirementInstance> getParentRequirement() {
        return Optional.ofNullable(requirementEntity.getParentRequirement()).map(
                parent -> ((RequirementService<RequirementEntity, ?>) getRequirementService()).newRequirementInstance(parent));
    }

    @Nonnull
    public RequirementInstance getParentRequirementOrException() {
        return getParentRequirement().orElseThrow(
                () -> new SingularRequirementException("A petição pai está null para esse requerimento", this));
    }

    public String getDescription() {
        return requirementEntity.getDescription();
    }

    public void setDescription(String description) {
        requirementEntity.setDescription(description);
    }

    public void setNewFlow(FlowInstance newFlowInstance) {
        FlowInstanceEntity flowEntity = newFlowInstance.saveEntity();
        requirementEntity.setFlowInstanceEntity(flowEntity);
        requirementEntity.setFlowDefinitionEntity(flowEntity.getFlowVersion().getFlowDefinition());

    }

    public FlowInstance startNewFlow(@Nonnull FlowDefinition flowDefinition) {
        flowInstance = getRequirementService().startNewFlow(this, flowDefinition, null);
        return flowInstance;
    }

    public RequirementEntity getEntity() {
        return requirementEntity;
    }

    public ApplicantEntity getApplicant() {
        return requirementEntity.getApplicant();
    }

    //TODO REFACTOR
    public String getIdPessoaSeForPessoaJuridica() {
        if (PersonType.JURIDICA == getApplicant().getPersonType()) {
            return getApplicant().getIdPessoa();
        }
        return null;
    }

    public FormVersionEntity getMainFormCurrentFormVersion() {
        return requirementEntity.getMainForm().getCurrentFormVersionEntity();
    }

    public Long getMainFormCurrentFormVersionCod() {
        return getMainFormCurrentFormVersion().getCod();
    }

    public String getMainFormTypeName(){
        return getEntity().getMainForm().getFormType().getAbbreviation();
    }

    public String getApplicantName(){
        return Optional.of(getEntity()).map(RequirementEntity::getApplicant).map(ApplicantEntity::getName).orElse(null);
    }


}