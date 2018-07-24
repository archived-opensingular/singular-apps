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

package org.opensingular.requirement.module.persistence.entity.form;

import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.form.persistence.entity.FormTypeEntity;
import org.opensingular.lib.support.persistence.entity.BaseEntity;
import org.opensingular.lib.support.persistence.util.Constants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 */
@Entity
@SequenceGenerator(name = RequirementDefinitionEntity.PK_GENERATOR_NAME, sequenceName = Constants.SCHEMA + ".SQ_CO_DEFINICAO_REQUISICAO", schema = Constants.SCHEMA)
@Table(schema = Constants.SCHEMA, name = "TB_DEFINICAO_REQUISICAO", indexes = {
        @Index(columnList = "CO_MODULO ASC, NO_DEFINICAO_REQUISICAO ASC", name = "TB_DEFINICAO_REQUISICAO")
})
public class RequirementDefinitionEntity extends BaseEntity<Long> {

    public static final String PK_GENERATOR_NAME = "GENERATED_CO_DEFINICAO_REQUISICAO";

    @Id
    @Column(name = "CO_DEFINICAO_REQUISICAO")
    @GeneratedValue(generator = PK_GENERATOR_NAME, strategy = GenerationType.AUTO)
    private Long cod;

    @Column(name = "SG_DEFINICAO_REQUISICAO", length = 200, nullable = false)
    private String key;

    @ManyToOne
    @JoinColumn(name = "CO_TIPO_FORMULARIO", foreignKey = @ForeignKey(name = "FK_DEFI_REQ_TIPO_FORMULARIO"), nullable = false)
    private FormTypeEntity formType;

    @ManyToOne
    @JoinColumn(name = "CO_MODULO", foreignKey = @ForeignKey(name = "FK_DEFI_REQ_MODULO"), nullable = false)
    private ModuleEntity module;

    @Column(name = "NO_DEFINICAO_REQUISICAO", nullable = false, length = 300)
    private String name;

    @Override
    public Long getCod() {
        return cod;
    }

    public void setCod(Long cod) {
        this.cod = cod;
    }

    public FormTypeEntity getFormType() {
        return formType;
    }

    public void setFormType(FormTypeEntity formType) {
        this.formType = formType;
    }

    public ModuleEntity getModule() {
        return module;
    }

    public void setModule(ModuleEntity module) {
        this.module = module;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
