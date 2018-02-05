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

import org.hibernate.annotations.Type;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.lib.support.persistence.entity.BaseEntity;
import org.opensingular.lib.support.persistence.enums.SimNao;
import org.opensingular.lib.support.persistence.util.Constants;
import org.opensingular.lib.support.persistence.util.GenericEnumUserType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(FormVersionHistoryPK.class)
@Table(name = "TB_HISTORICO_VERSAO_FORMULARIO", schema = Constants.SCHEMA)
public class FormVersionHistoryEntity extends BaseEntity<FormVersionHistoryPK> {

    @Id
    @Column(name = "CO_HISTORICO")
    private Long codRequirementContentHistory;

    @Id
    @Column(name = "CO_VERSAO_FORMULARIO")
    private Long codFormVersion;

    @Column(name = "ST_FORM_PRINCIPAL", length = 1)
    @Type(type = GenericEnumUserType.CLASS_NAME, parameters = {
            @org.hibernate.annotations.Parameter(name = "enumClass", value = SimNao.ENUM_CLASS_NAME),
            @org.hibernate.annotations.Parameter(name = "identifierMethod", value = "getCodigo"),
            @org.hibernate.annotations.Parameter(name = "valueOfMethod", value = "valueOfEnum")})
    private SimNao mainForm;

    @ManyToOne
    @JoinColumn(name = "CO_HISTORICO", insertable = false, updatable = false)
    private RequirementContentHistoryEntity requirementContentHistory;

    @ManyToOne
    @JoinColumn(name = "CO_VERSAO_FORMULARIO", insertable = false, updatable = false)
    private FormVersionEntity formVersion;

    @Override
    public FormVersionHistoryPK getCod() {
        return new FormVersionHistoryPK(codRequirementContentHistory, codFormVersion);
    }

    public RequirementContentHistoryEntity getRequirementContentHistory() {
        return requirementContentHistory;
    }

    public void setRequirementContentHistory(RequirementContentHistoryEntity requirementContentHistory) {
        this.requirementContentHistory = requirementContentHistory;
    }

    public FormVersionEntity getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(FormVersionEntity formVersion) {
        this.formVersion = formVersion;
    }

    public Long getCodRequirementContentHistory() {
        return codRequirementContentHistory;
    }

    public void setCodRequirementContentHistory(Long codRequirementContentHistory) {
        this.codRequirementContentHistory = codRequirementContentHistory;
    }

    public Long getCodFormVersion() {
        return codFormVersion;
    }

    public void setCodFormVersion(Long codFormVersion) {
        this.codFormVersion = codFormVersion;
    }

    public SimNao getMainForm() {
        return mainForm;
    }

    public void setMainForm(SimNao mainForm) {
        this.mainForm = mainForm;
    }

}