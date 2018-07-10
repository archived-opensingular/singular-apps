package org.opensingular.requirement.module.service.dto;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.flow.persistence.entity.TaskInstanceEntity;
import org.opensingular.flow.persistence.entity.TaskVersionEntity;
import org.opensingular.requirement.module.persistence.entity.form.RequirementContentHistoryEntity;

public class AnaliseAnteriorDTO implements Serializable {

    private String                name;
    private String                actor;
    private Date                  date;
    private List<TypeFormVersion> typeFormVersions;

    public AnaliseAnteriorDTO(RequirementContentHistoryEntity nullableHistoryEntity) {
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