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

package org.opensingular.requirement.module.service.dto;


import org.apache.commons.lang3.StringUtils;
import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.flow.persistence.entity.TaskInstanceEntity;
import org.opensingular.flow.persistence.entity.TaskVersionEntity;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.requirement.module.persistence.entity.form.ApplicantEntity;
import org.opensingular.requirement.module.persistence.entity.form.RequirementContentHistoryEntity;
import org.opensingular.requirement.module.persistence.entity.form.RequirementEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class EmbeddedHistoryDTO implements Serializable {

    private String                name;
    private String                actor;
    private Date                  date;
    private List<TypeFormVersion> typeFormVersions;

    public EmbeddedHistoryDTO() {
    }

    public EmbeddedHistoryDTO(RequirementEntity r, FormVersionEntity formVersionEntity) {
        name = r.getDescription();
        actor = Optional.ofNullable(r.getApplicant()).map(ApplicantEntity::getName).orElse(null);
        date = formVersionEntity.getInclusionDate();
        typeFormVersions = new ArrayList<>();
        typeFormVersions.add(new TypeFormVersion(
                formVersionEntity.getCod(),
                formVersionEntity
                        .getFormEntity()
                        .getFormType()
                        .getLabel()));
    }

    public EmbeddedHistoryDTO(RequirementContentHistoryEntity nullableHistoryEntity) {
        final Optional<RequirementContentHistoryEntity> historyEntity = Optional.ofNullable(nullableHistoryEntity);

        name = historyEntity
                .map(RequirementContentHistoryEntity::getTaskInstanceEntity)
                .map(TaskInstanceEntity::getTaskVersion)
                .map(TaskVersionEntity::getName)
                .orElse(StringUtils.EMPTY);
        actor = historyEntity
                .map(RequirementContentHistoryEntity::getActor)
                .map(Actor::getNome)
                .orElse(StringUtils.EMPTY);
        date = historyEntity
                .map(RequirementContentHistoryEntity::getHistoryDate)
                .orElse(null);

        typeFormVersions = new ArrayList<>();

        historyEntity
                .map(RequirementContentHistoryEntity::getFormVersionHistoryEntities)
                .map(List::stream)
                .ifPresent(list -> list.forEach(formVersionHistoryEntity -> {
                    typeFormVersions.add(new TypeFormVersion(formVersionHistoryEntity.getFormVersion().getCod(), formVersionHistoryEntity
                            .getFormVersion()
                            .getFormEntity()
                            .getFormType()
                            .getLabel()
                    ));
                }));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<TypeFormVersion> getTypeFormVersions() {
        return typeFormVersions;
    }

    public void setTypeFormVersions(List<TypeFormVersion> typeFormVersions) {
        this.typeFormVersions = typeFormVersions;
    }

    public static class TypeFormVersion implements Serializable {

        private Long   formVersionPK;
        private String label;

        public TypeFormVersion(Long formVersionPK, String label) {
            this.formVersionPK = formVersionPK;
            this.label = label;
        }

        public Long getFormVersionPK() {
            return formVersionPK;
        }

        public String getLabel() {
            return label;
        }
    }


}