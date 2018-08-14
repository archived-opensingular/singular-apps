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
import org.opensingular.flow.persistence.entity.AbstractTaskTransitionVersionEntity;
import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.flow.persistence.entity.TaskInstanceEntity;
import org.opensingular.flow.persistence.entity.TaskVersionEntity;
import org.opensingular.requirement.module.persistence.entity.form.RequirementContentHistoryEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

public class RequirementHistoryDTO implements Serializable {

    private final RequirementContentHistoryEntity requirementContentHistory;

    @QueryProjection
    public RequirementHistoryDTO(RequirementContentHistoryEntity requirementContentHistory) {
        this.requirementContentHistory = requirementContentHistory;
    }

    public String getTaskAbbreviation() {
        return Optional.of(requirementContentHistory)
                .map(RequirementContentHistoryEntity::getTaskInstanceEntity)
                .map(TaskInstanceEntity::getTaskVersion)
                .map(TaskVersionEntity::getAbbreviation)
                .orElse(StringUtils.EMPTY);
    }

    public String getTaskName() {
        return Optional.of(requirementContentHistory)
                .map(RequirementContentHistoryEntity::getTaskInstanceEntity)
                .map(TaskInstanceEntity::getTaskVersion)
                .map(TaskVersionEntity::getName)
                .orElse(StringUtils.EMPTY);
    }

    public String getActor() {
        return Optional.of(requirementContentHistory)
                .map(RequirementContentHistoryEntity::getActor)
                .map(Actor::getNome)
                .orElse(StringUtils.EMPTY);
    }

    public Date getBeginDate() {
        return Optional.of(requirementContentHistory)
                .map(RequirementContentHistoryEntity::getTaskInstanceEntity)
                .map(TaskInstanceEntity::getBeginDate)
                .orElse(null);
    }

    public Date getEndDate() {
        return Optional.of(requirementContentHistory)
                .map(RequirementContentHistoryEntity::getTaskInstanceEntity)
                .map(TaskInstanceEntity::getEndDate)
                .orElse(null);
    }

    public String getExecutedTransition() {
        return Optional.of(requirementContentHistory)
                .map(RequirementContentHistoryEntity::getTaskInstanceEntity)
                .map(TaskInstanceEntity::getExecutedTransition)
                .map(AbstractTaskTransitionVersionEntity::getName)
                .orElse(StringUtils.EMPTY);
    }

    public RequirementContentHistoryEntity getRequirementContentHistory() {
        return requirementContentHistory;
    }
}