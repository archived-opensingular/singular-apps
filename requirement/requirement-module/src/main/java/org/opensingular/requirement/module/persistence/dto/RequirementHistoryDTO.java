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

package org.opensingular.requirement.module.persistence.dto;

import com.querydsl.core.annotations.QueryProjection;
import org.apache.commons.lang3.StringUtils;
import org.opensingular.flow.core.entity.IEntityTaskVersion;
import org.opensingular.flow.persistence.entity.AbstractTaskInstanceEntity;
import org.opensingular.flow.persistence.entity.AbstractTaskTransitionVersionEntity;
import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.flow.persistence.entity.TaskInstanceEntity;
import org.opensingular.flow.persistence.entity.TaskVersionEntity;
import org.opensingular.requirement.module.persistence.entity.form.RequirementContentHistoryEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class RequirementHistoryDTO implements Serializable, Comparable<RequirementHistoryDTO> {

    private final RequirementContentHistoryEntity requirementContentHistory;
    private final TaskInstanceEntity taskInstanceEntity;

    @QueryProjection
    public RequirementHistoryDTO(RequirementContentHistoryEntity requirementContentHistory, TaskInstanceEntity taskInstanceEntity) {
        this.requirementContentHistory = requirementContentHistory;
        this.taskInstanceEntity = taskInstanceEntity;
    }

    @QueryProjection
    public RequirementHistoryDTO(RequirementContentHistoryEntity requirementContentHistory) {
        this.requirementContentHistory = requirementContentHistory;
        this.taskInstanceEntity = null;
    }

    @QueryProjection
    public RequirementHistoryDTO(TaskInstanceEntity taskInstanceEntity) {
        this.requirementContentHistory = null;
        this.taskInstanceEntity = taskInstanceEntity;
    }

    private Optional<TaskInstanceEntity> getTaskVersion() {
        if (taskInstanceEntity != null) {
            return Optional.of(taskInstanceEntity);
        }
        if (requirementContentHistory != null) {
            return Optional.of(requirementContentHistory.getTaskInstanceEntity());
        }
        return Optional.empty();
    }

    public String getTaskAbbreviation() {
        return getTaskVersion()
                .map(AbstractTaskInstanceEntity::getTaskVersion)
                .map(IEntityTaskVersion::getAbbreviation).orElse(StringUtils.EMPTY);
    }

    public String getTaskName() {
        return getTaskVersion()
                .map(TaskInstanceEntity::getTaskVersion)
                .map(TaskVersionEntity::getName)
                .orElse(StringUtils.EMPTY);
    }

    public String getActor() {
        if (requirementContentHistory != null) {
            return Optional.of(requirementContentHistory)
                    .map(RequirementContentHistoryEntity::getActor)
                    .map(Actor::getNome)
                    .orElse(StringUtils.EMPTY);
        }
        return getTaskVersion()
                .map(AbstractTaskInstanceEntity::getAllocatedUser)
                .map(Actor::getNome)
                .orElse(StringUtils.EMPTY);
    }

    public Date getBeginDate() {
        return getTaskVersion()
                .map(TaskInstanceEntity::getBeginDate)
                .orElse(null);
    }

    public Date getEndDate() {
        return getTaskVersion()
                .map(TaskInstanceEntity::getEndDate)
                .orElse(null);
    }

    public String getExecutedTransition() {
        return getTaskVersion()
                .map(TaskInstanceEntity::getExecutedTransition)
                .map(AbstractTaskTransitionVersionEntity::getName)
                .orElse(StringUtils.EMPTY);
    }

    public RequirementContentHistoryEntity getRequirementContentHistory() {
        return requirementContentHistory;
    }

    @Override
    public int compareTo(RequirementHistoryDTO o) {
        return getBeginDate().compareTo(o.getBeginDate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequirementHistoryDTO that = (RequirementHistoryDTO) o;
        return Objects.equals(requirementContentHistory, that.requirementContentHistory) && Objects.equals(
                taskInstanceEntity, that.taskInstanceEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requirementContentHistory, taskInstanceEntity);
    }
}