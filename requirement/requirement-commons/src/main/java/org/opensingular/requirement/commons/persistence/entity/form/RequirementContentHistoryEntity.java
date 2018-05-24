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

package org.opensingular.requirement.commons.persistence.entity.form;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.flow.persistence.entity.TaskInstanceEntity;
import org.opensingular.form.persistence.entity.FormAnnotationVersionEntity;
import org.opensingular.lib.support.persistence.entity.BaseEntity;
import org.opensingular.lib.support.persistence.util.Constants;

@Entity
@Table(schema = Constants.SCHEMA, name = "TB_HISTORICO_CONTEUDO_REQUISIC")
@SequenceGenerator(name = RequirementContentHistoryEntity.PK_GENERATOR_NAME, sequenceName = Constants.SCHEMA + ".SQ_CO_HISTORICO", schema = Constants.SCHEMA)
public class RequirementContentHistoryEntity extends BaseEntity<Long> {

    public static final String PK_GENERATOR_NAME = "GENERATED_CO_HISTORICO";

    @Id
    @Column(name = "CO_HISTORICO")
    @GeneratedValue(generator = PK_GENERATOR_NAME, strategy = GenerationType.AUTO)
    private Long cod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CO_REQUISICAO", foreignKey = @ForeignKey(name = "FK_HIST_CTD_REQ_REQ"), nullable = false)
    private RequirementEntity requirementEntity;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DT_HISTORICO", nullable = false)
    private Date historyDate;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "RL_HIST_CONT_REQ_VER_ANOTACAO", schema = Constants.SCHEMA,
            uniqueConstraints = {@UniqueConstraint(name = "UK_HIST_CONT_REQ_VER_ANOT", columnNames = "CO_VERSAO_ANOTACAO")},
            joinColumns = @JoinColumn(name = "CO_HISTORICO"),
            inverseJoinColumns = @JoinColumn(name = "CO_VERSAO_ANOTACAO"))
    @org.hibernate.annotations.ForeignKey(name = "FK_HIST_CONT_REQ_VER_ANOTACAO", inverseName = "FK_VER_ANOTACAO_HIST_CONT_REQ")
    private List<FormAnnotationVersionEntity> formAnnotationsVersions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CO_INSTANCIA_TAREFA", foreignKey = @ForeignKey(name = "FK_HIST_CTD_REQ_INSTANCIA_TRF"))
    private TaskInstanceEntity taskInstanceEntity;

    @ManyToOne
    @JoinColumn(name = "CO_AUTOR", foreignKey = @ForeignKey(name = "FK_HIST_CTD_REQ_REQ_AUTOR"))
    private Actor actor;

    @ManyToOne
    @JoinColumn(name = "CO_REQUISITANTE", foreignKey = @ForeignKey(name = "FK_HIST_CTD_REQ_REQUISITANTE"))
    private ApplicantEntity applicantEntity;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "requirementContentHistory")
    private List<FormVersionHistoryEntity> formVersionHistoryEntities;

    @Override
    public Long getCod() {
        return cod;
    }

    public void setCod(Long cod) {
        this.cod = cod;
    }

    public RequirementEntity getRequirementEntity() {
        return requirementEntity;
    }

    public void setRequirementEntity(RequirementEntity requirementEntity) {
        this.requirementEntity = requirementEntity;
    }

    public Date getHistoryDate() {
        return historyDate;
    }

    public void setHistoryDate(Date historyDate) {
        this.historyDate = historyDate;
    }

    public List<FormAnnotationVersionEntity> getFormAnnotationsVersions() {
        return formAnnotationsVersions;
    }

    public void setFormAnnotationsVersions(List<FormAnnotationVersionEntity> formAnnotationsVersions) {
        this.formAnnotationsVersions = formAnnotationsVersions;
    }

    public TaskInstanceEntity getTaskInstanceEntity() {
        return taskInstanceEntity;
    }

    public void setTaskInstanceEntity(TaskInstanceEntity taskInstanceEntity) {
        this.taskInstanceEntity = taskInstanceEntity;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public ApplicantEntity getApplicantEntity() {
        return applicantEntity;
    }

    public void setApplicantEntity(ApplicantEntity applicantEntity) {
        this.applicantEntity = applicantEntity;
    }

    public List<FormVersionHistoryEntity> getFormVersionHistoryEntities() {
        return formVersionHistoryEntities;
    }

    public void setFormVersionHistoryEntities(List<FormVersionHistoryEntity> formVersionHistoryEntities) {
        this.formVersionHistoryEntities = formVersionHistoryEntities;
    }

}