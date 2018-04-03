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


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.Type;
import org.opensingular.flow.persistence.entity.TaskDefinitionEntity;
import org.opensingular.form.persistence.entity.FormEntity;
import org.opensingular.lib.support.persistence.entity.BaseEntity;
import org.opensingular.lib.support.persistence.enums.SimNao;
import org.opensingular.lib.support.persistence.util.Constants;
import org.opensingular.lib.support.persistence.util.GenericEnumUserType;

@Entity
@Table(schema = Constants.SCHEMA, name = "TB_FORMULARIO_REQUISICAO",
        indexes = {
                @Index(columnList = "ST_FORM_PRINCIPAL ASC, CO_REQUISICAO ASC", name = "IX_FORMULARIO_PRINCIPAL")
        })
@SequenceGenerator(name = FormRequirementEntity.PK_GENERATOR_NAME, sequenceName = Constants.SCHEMA + ".SQ_CO_FORMULARIO_REQUISICAO", schema = Constants.SCHEMA)
@Check(constraints ="ST_FORM_PRINCIPAL IN ('S','N')")
public class FormRequirementEntity extends BaseEntity<Long> implements Comparable<FormRequirementEntity> {

    public static final String PK_GENERATOR_NAME = "GENERATED_CO_FORMULARIO_REQUISICAO";

    @Id
    @Column(name = "CO_FORMULARIO_REQUISICAO")
    @GeneratedValue(generator = PK_GENERATOR_NAME, strategy = GenerationType.AUTO)
    private Long cod;

    @ManyToOne
    @JoinColumn(name = "CO_REQUISICAO", foreignKey = @ForeignKey(name = "FK_FORMO_REQ_REQUISICAO"), nullable = false)
    private RequirementEntity requirement;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "CO_FORMULARIO", foreignKey = @ForeignKey(name = "FK_FORM_REQ_FORMULARIO"))
    private FormEntity form;

    @Column(name = "ST_FORM_PRINCIPAL", length = 1, nullable = false)
    @Type(type = GenericEnumUserType.CLASS_NAME, parameters = {
            @org.hibernate.annotations.Parameter(name = "enumClass", value = SimNao.ENUM_CLASS_NAME),
            @org.hibernate.annotations.Parameter(name = "identifierMethod", value = "getCodigo"),
            @org.hibernate.annotations.Parameter(name = "valueOfMethod", value = "valueOfEnum")})
    private SimNao mainForm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CO_DEFINICAO_TAREFA", foreignKey = @ForeignKey(name = "FK_FORM_REQ_DEFINICAO_TAREFA"))
    private TaskDefinitionEntity taskDefinitionEntity;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "CO_RASCUNHO_ATUAL", foreignKey = @ForeignKey(name = "FK_FORM_REQ_RASCUNHO_ATUAL"))
    private DraftEntity currentDraftEntity;

    @Override
    public Long getCod() {
        return cod;
    }

    public void setCod(Long cod) {
        this.cod = cod;
    }

    public RequirementEntity getRequirement() {
        return requirement;
    }

    public void setRequirement(RequirementEntity requirement) {
        this.requirement = requirement;
    }

    public FormEntity getForm() {
        return form;
    }

    public void setForm(FormEntity form) {
        this.form = form;
    }

    public SimNao getMainForm() {
        return mainForm;
    }

    public void setMainForm(SimNao mainForm) {
        this.mainForm = mainForm;
    }

    public TaskDefinitionEntity getTaskDefinitionEntity() {
        return taskDefinitionEntity;
    }

    public void setTaskDefinitionEntity(TaskDefinitionEntity taskDefinitionEntity) {
        this.taskDefinitionEntity = taskDefinitionEntity;
    }

    @Override
    public int compareTo(FormRequirementEntity o) {
        return BaseEntity.compare(this, o);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o); //Apenas para o Sonar não reclamar
    }

    @Override
    public int hashCode() {
        return super.hashCode(); //Apenas para o Sonar não reclamar
    }

    public DraftEntity getCurrentDraftEntity() {
        return currentDraftEntity;
    }

    public void setCurrentDraftEntity(DraftEntity currentDraftEntity) {
        this.currentDraftEntity = currentDraftEntity;
    }
}