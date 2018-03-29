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


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.opensingular.lib.support.persistence.entity.BaseEntity;
import org.opensingular.lib.support.persistence.util.Constants;
import org.opensingular.lib.support.persistence.util.GenericEnumUserType;
import org.opensingular.requirement.commons.persistence.entity.enums.PersonType;

@Entity
@Table(schema = Constants.SCHEMA, name = "TB_REQUISITANTE")
@Check(constraints ="TP_PESSOA IN ('J','F')")
@SequenceGenerator(name = ApplicantEntity.PK_GENERATOR_NAME, sequenceName = "SQ_CO_REQUISITANTE", schema = Constants.SCHEMA)
public class ApplicantEntity extends BaseEntity<Long> {


    public static final String PK_GENERATOR_NAME = "GENERATED_CO_REQUISITANTE";

    @Id
    @Column(name = "CO_REQUISITANTE")
    @GeneratedValue(generator = PK_GENERATOR_NAME, strategy = GenerationType.AUTO)
    private Long cod;

    @Column(name = "DS_NOME", length = 200, nullable = false)
    private String name;

    @Column(name = "ID_PESSOA", length = 200, nullable = false)
    private String idPessoa;

    @Column(name = "NU_CPF_CNPJ", length = 14)
    private String cpfCNPJ;

    @Type(type = GenericEnumUserType.CLASS_NAME, parameters = {
            @Parameter(name = "enumClass", value = PersonType.CLASS_NAME),
            @Parameter(name = "identifierMethod", value = "getCod"),
            @Parameter(name = "valueOfMethod", value = "valueOfEnum")})
    @Column(name = "TP_PESSOA", length = 1, nullable = false)
    @ColumnDefault(value = "'J'")
    private PersonType personType;

    @Override
    public Long getCod() {
        return cod;
    }

    public void setCod(Long cod) {
        this.cod = cod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(String idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getCpfCNPJ() {
        return cpfCNPJ;
    }

    public void setCpfCNPJ(String cpfCNPJ) {
        this.cpfCNPJ = cpfCNPJ;
    }

    public PersonType getPersonType() {
        return personType;
    }

    public void setPersonType(PersonType personType) {
        this.personType = personType;
    }
}
