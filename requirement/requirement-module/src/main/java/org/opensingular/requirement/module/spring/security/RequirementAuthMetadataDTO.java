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

package org.opensingular.requirement.module.spring.security;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class RequirementAuthMetadataDTO implements Serializable {

    private String formTypeAbbreviation;

    private String currentTaskAbbreviation;

    private String definitionKey;

    private Integer taskInstanceCod;

    public RequirementAuthMetadataDTO(String formTypeAbbreviationDraft, String formTypeAbbreviationProcess, String currentTaskAbbreviation, String definitionKey, Integer taskInstanceCod) {
        this.formTypeAbbreviation = formTypeAbbreviationDraft == null ? formTypeAbbreviationProcess :  formTypeAbbreviationDraft;
        this.currentTaskAbbreviation = currentTaskAbbreviation;
        this.definitionKey = definitionKey;
        this.taskInstanceCod  = taskInstanceCod;
    }

    public RequirementAuthMetadataDTO() {
    }

    public String getCurrentTaskAbbreviation() {
        return currentTaskAbbreviation;
    }

    public void setCurrentTaskAbbreviation(String currentTaskAbbreviation) {
        this.currentTaskAbbreviation = currentTaskAbbreviation;
    }

    public String getDefinitionKey() {
        return definitionKey;
    }

    public void setDefinitionKey(String definitionKey) {
        this.definitionKey = definitionKey;
    }

    public String getFormTypeAbbreviation() {
        return formTypeAbbreviation;
    }

    public void setFormTypeAbbreviation(String formTypeAbbreviation) {
        this.formTypeAbbreviation = formTypeAbbreviation;
    }

    public Integer getTaskInstanceCod() {
        return taskInstanceCod;
    }

    public void setTaskInstanceCod(Integer taskInstanceCod) {
        this.taskInstanceCod = taskInstanceCod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RequirementAuthMetadataDTO that = (RequirementAuthMetadataDTO) o;

        return new EqualsBuilder()
                .append(formTypeAbbreviation, that.formTypeAbbreviation)
                .append(currentTaskAbbreviation, that.currentTaskAbbreviation)
                .append(definitionKey, that.definitionKey)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(formTypeAbbreviation)
                .append(currentTaskAbbreviation)
                .append(definitionKey)
                .toHashCode();
    }
}
