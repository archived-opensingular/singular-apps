/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
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

package org.opensingular.requirement.module.service;

import org.hibernate.SessionFactory;
import org.opensingular.flow.core.Flow;
import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.flow.core.TaskInstance;
import org.opensingular.flow.persistence.entity.FlowDefinitionEntity;
import org.opensingular.flow.persistence.entity.FlowInstanceEntity;
import org.opensingular.form.SFormUtil;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.form.document.RefType;
import org.opensingular.form.document.SDocumentConsumer;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.requirement.module.RequirementDefinition;
import org.opensingular.requirement.module.exception.SingularRequirementException;
import org.opensingular.requirement.module.flow.ProcessServiceSetup;
import org.opensingular.requirement.module.persistence.entity.enums.PersonType;
import org.opensingular.requirement.module.persistence.entity.form.ApplicantEntity;
import org.opensingular.requirement.module.persistence.entity.form.RequirementEntity;
import org.opensingular.requirement.module.service.dto.RequirementSubmissionResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Daniel C. Bordin on 07/03/2017.
 */
public class RequirementInstance<SELF extends RequirementInstance<SELF, RD>, RD extends RequirementDefinition<SELF>> implements Serializable {

    @Inject
    private RequirementService requirementService;

    @Inject
    private FormRequirementService formRequirementService;

    private RD requirementDefinition;

    private RequirementEntity requirementEntity;

    private transient FlowInstance flowInstance;

    public RequirementInstance(RequirementEntity requirementEntity, RD requirementDefinition) {
        this.requirementEntity = Objects.requireNonNull(requirementEntity);
        this.requirementDefinition = requirementDefinition;
    }

    public RD getRequirementDefinition() {
        return requirementDefinition;
    }

    public FlowInstance getFlowInstance() {
        if (flowInstance == null && getEntity().getFlowInstanceEntity() != null) {
            flowInstance = RequirementUtil.getFlowInstance(getEntity());
        }
        return flowInstance;
    }

    final void setFlowInstance(FlowInstance flowInstance) {
        this.flowInstance = flowInstance;
    }

    public boolean isFlowInstanceCreated() {
        return getEntity().getFlowInstanceEntity() != null;
    }


    @Nonnull
    public void saveForm(@Nonnull SInstance instance) {
        boolean mainForm = getRequirementDefinition().getMainForm().equals(instance.getType().getClass());//TODO reqdef idetificar isso de uma maneira melhor
        requirementService.saveOrUpdate(this, instance, mainForm);
    }

    public FlowDefinition<?> getFlowDefinition() {
        return RequirementUtil.getFlowDefinition(getEntity());
    }

    public void setFlowDefinition(@Nonnull Class<? extends FlowDefinition> clazz) {
        FlowDefinition<?> flowDefinition = Flow.getFlowDefinition(clazz);
        getEntity().setFlowDefinitionEntity((FlowDefinitionEntity) flowDefinition.getEntityFlowDefinition());
    }

    public Optional<FlowDefinition<?>> getFlowDefinitionOpt() {
        return RequirementUtil.getFlowDefinitionOpt(getEntity());
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
        return Optional.ofNullable(this.getEntity())
                .map(RequirementEntity::getParentRequirement)
                .map(RequirementEntity::getCod)
                .map(cod -> requirementService.loadRequirementInstance(cod));
    }

    @Nonnull
    public RequirementInstance getParentRequirementOrException() {
        return getParentRequirement().orElseThrow(
                () -> new SingularRequirementException("A petição pai está null para esse requerimento", this));
    }

    public String getDescription() {
        return getEntity().getDescription();
    }

    public void setDescription(String description) {
        getEntity().setDescription(description);
    }

    public void setNewFlow(FlowInstance newFlowInstance) {
        FlowInstanceEntity flowEntity = newFlowInstance.saveEntity();
        getEntity().setFlowInstanceEntity(flowEntity);
        getEntity().setFlowDefinitionEntity(flowEntity.getFlowVersion().getFlowDefinition());

    }

    /**
     * @return
     * @deprecated must not be used, it will be turned into private method on future releases.
     */
    @Deprecated
    public RequirementEntity getEntity() {
        if (getCod() != null) {
            requirementEntity = (RequirementEntity) ApplicationContextProvider.get().getBean(SessionFactory.class).getCurrentSession().load(RequirementEntity.class, getCod());
        }
        return requirementEntity;
    }

    public ApplicantEntity getApplicant() {
        return getEntity().getApplicant();
    }

    //TODO REFACTOR
    public String getIdPessoaSeForPessoaJuridica() {
        if (PersonType.JURIDICA == getApplicant().getPersonType()) {
            return getApplicant().getIdPessoa();
        }
        return null;
    }

    public FormVersionEntity getMainFormCurrentFormVersion() {
        return getEntity().getMainForm().getCurrentFormVersionEntity();
    }

    public Long getMainFormCurrentFormVersionCod() {
        return getMainFormCurrentFormVersion().getCod();
    }

    @Deprecated
    public String getMainFormTypeName() {
        return getEntity().getMainForm().getFormType().getAbbreviation();
    }

    @Deprecated
    public String getRequirementDefinitionName() {
        return getEntity().getRequirementDefinitionEntity().getName();
    }

    @Deprecated
    public String getApplicantName() {
        return Optional.of(getApplicant()).map(ApplicantEntity::getName).orElse(null);
    }

    public RequirementSubmissionResponse send(@Nullable String codSubmitterActor) {
        return getRequirementDefinition().send((SELF) this, codSubmitterActor);
    }

    /**
     * @return returns requirement main form last version.
     */
    @Nonnull
    public final <SI extends SInstance> Optional<SI> getForm() {
        return getForm(getRequirementDefinition().getMainForm());
    }

    /**
     * Return the given form type last version
     *
     * @param formName
     * @return
     */
    public Optional<SIComposite> getForm(@Nonnull String formName) {
        return requirementService.findLastFormInstanceByType(this, formName);
    }

    /**
     * Return the given form type last version
     *
     * @param form
     * @return
     */
    public final <SI extends SInstance> Optional<SI> getForm(@Nonnull Class<? extends SType<SI>> form) {
        return (Optional<SI>) getForm(SFormUtil.getTypeName(form));
    }

    /**
     * Returns the current open draft for the main form or exception if it does not exists
     *
     * @param
     * @return
     */
    public final Optional<SIComposite> getDraft() {
        return getDraft(SFormUtil.getTypeName(getRequirementDefinition().getMainForm()));
    }


    /**
     * Returns the current open draft for the given type or exception if it does not exists
     *
     * @param formName
     * @return
     */
    public Optional<SIComposite> getDraft(@Nonnull String formName) {
        return requirementService.findCurrentDraftForType(this, formName);
    }

    public final Optional<SIComposite> getDraft(@Nonnull Class<? extends SType<?>> form) {
        return getDraft(RequirementUtil.getTypeName(form));
    }

    public final SInstance newForm() {
        return newForm(getRequirementDefinition().getMainForm());
    }

    public final SInstance newForm(@Nonnull Class<? extends SType<?>> form) {
        return newForm(RequirementUtil.getTypeName(form));
    }

    private SInstance newForm(@Nonnull RefType refType) {
        return formRequirementService.createInstance(refType, localServicesBinder());
    }

    public SInstance newForm(@Nonnull String formName) {
        return newForm(formRequirementService.loadRefType(formName));
    }

    private SDocumentConsumer localServicesBinder() {
        return SDocumentConsumer.of(new ProcessServiceSetup(getCod()));
    }
}