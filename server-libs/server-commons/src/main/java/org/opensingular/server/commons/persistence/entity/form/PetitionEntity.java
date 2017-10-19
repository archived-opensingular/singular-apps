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

package org.opensingular.server.commons.persistence.entity.form;

import org.hibernate.annotations.GenericGenerator;
import org.opensingular.flow.persistence.entity.FlowDefinitionEntity;
import org.opensingular.flow.persistence.entity.FlowInstanceEntity;
import org.opensingular.form.SType;
import org.opensingular.form.persistence.entity.FormEntity;
import org.opensingular.lib.support.persistence.entity.BaseEntity;
import org.opensingular.lib.support.persistence.enums.SimNao;
import org.opensingular.lib.support.persistence.util.Constants;
import org.opensingular.lib.support.persistence.util.HybridIdentityOrSequenceGenerator;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.service.PetitionUtil;

import javax.annotation.Nonnull;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity
@Table(schema = Constants.SCHEMA, name = "TB_REQUISICAO")
@GenericGenerator(name = PetitionEntity.PK_GENERATOR_NAME, strategy = HybridIdentityOrSequenceGenerator.CLASS_NAME)
public class PetitionEntity extends BaseEntity<Long> {

    public static final String PK_GENERATOR_NAME = "GENERATED_CO_REQUISICAO";

    @Id
    @Column(name = "CO_REQUISICAO")
    @GeneratedValue(generator = PK_GENERATOR_NAME)
    private Long cod;

    @ManyToOne
    @JoinColumn(name = "CO_INSTANCIA_PROCESSO")
    private FlowInstanceEntity flowInstanceEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CO_DEFINICAO_PROCESSO")
    private FlowDefinitionEntity flowDefinitionEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CO_REQUISITANTE")
    private PetitionerEntity petitioner;

    @Column(name = "DS_REQUISICAO")
    private String description;

    @OneToMany(mappedBy = "petition", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy(" CO_FORMULARIO_REQUISICAO ASC ")
    private SortedSet<FormPetitionEntity> formPetitionEntities;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "CO_REQUISICAO_RAIZ")
    private PetitionEntity rootPetition;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "CO_REQUISICAO_PAI")
    private PetitionEntity parentPetition;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name =  "CO_DEFINICAO_REQUISICAO")
    private RequirementDefinitionEntity requirementDefinitionEntity;

    @Override
    public Long getCod() {
        return cod;
    }

    public void setCod(Long cod) {
        this.cod = cod;
    }

    public FlowInstanceEntity getFlowInstanceEntity() {
        return flowInstanceEntity;
    }

    public void setFlowInstanceEntity(FlowInstanceEntity flowInstanceEntity) {
        this.flowInstanceEntity = flowInstanceEntity;
    }

    public FlowDefinitionEntity getFlowDefinitionEntity() {
        return flowDefinitionEntity;
    }

    public void setFlowDefinitionEntity(FlowDefinitionEntity flowDefinitionEntity) {
        this.flowDefinitionEntity = flowDefinitionEntity;
    }

    public PetitionerEntity getPetitioner() {
        return petitioner;
    }

    public void setPetitioner(PetitionerEntity petitioner) {
        this.petitioner = petitioner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PetitionEntity getRootPetition() {
        return rootPetition;
    }

    public void setRootPetition(PetitionEntity rootPetition) {
        this.rootPetition = rootPetition;
    }

    public PetitionEntity getParentPetition() {
        return parentPetition;
    }

    public void setParentPetition(PetitionEntity parentPetition) {
        this.parentPetition = parentPetition;
    }

    public RequirementDefinitionEntity getRequirementDefinitionEntity() {
        return requirementDefinitionEntity;
    }

    public void setRequirementDefinitionEntity(RequirementDefinitionEntity requirementDefinitionEntity) {
        this.requirementDefinitionEntity = requirementDefinitionEntity;
    }

    public SortedSet<FormPetitionEntity> getFormPetitionEntities() {
        if (formPetitionEntities == null) {
            formPetitionEntities = new TreeSet<>();
        }
        return formPetitionEntities;
    }

    public void setFormPetitionEntities(SortedSet<FormPetitionEntity> formPetitionEntities) {
        this.formPetitionEntities = formPetitionEntities;
    }

    @Nonnull
    public FormEntity getMainForm() {
        FormEntity form = null;
        if (formPetitionEntities != null) {
            form = formPetitionEntities.stream()
                    .filter(f -> SimNao.SIM == f.getMainForm())
                    .map(f -> {
                        if (f.getForm() != null) {
                            return f.getForm();
                        } else if (f.getCurrentDraftEntity() != null) {
                            return f.getCurrentDraftEntity().getForm();
                        }
                        return null;
                    })
                    .findFirst()
                    .orElse(null);
        }
        if (form == null) {
            throw SingularServerException.rethrow("Base incossistente. Não foi encontrado o main form da petição.");
        }
        return form;
    }

    @Nonnull
    public Optional<DraftEntity> currentEntityDraftByType(@Nonnull Class<? extends SType<?>> typeClass) {
        return currentEntityDraftByType(PetitionUtil.getTypeName(typeClass));
    }

    @Nonnull
    public Optional<DraftEntity> currentEntityDraftByType(@Nonnull String typeName) {
        return Optional
                .ofNullable((Set<FormPetitionEntity>) formPetitionEntities)
                .orElse(Collections.emptySet())
                .stream()
                .filter(f -> f.getCurrentDraftEntity() != null && PetitionUtil.getTypeName(f.getCurrentDraftEntity()).equals(typeName))
                .findFirst()
                .map(FormPetitionEntity::getCurrentDraftEntity);

    }

}