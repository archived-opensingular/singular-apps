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

package org.opensingular.server.commons.persistence.dto;

import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.flow.persistence.entity.TaskInstanceEntity;
import org.opensingular.server.commons.persistence.entity.form.RequirementContentHistoryEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

public class RequirementHistoryDTO implements Serializable {

    private TaskInstanceEntity           task;
    private RequirementContentHistoryEntity requirementContentHistory;

    public TaskInstanceEntity getTask() {
        return task;
    }

    public RequirementHistoryDTO setTask(TaskInstanceEntity task) {
        this.task = task;
        return this;
    }

    public RequirementContentHistoryEntity getRequirementContentHistory() {
        return requirementContentHistory;
    }

    public RequirementHistoryDTO setRequirementContentHistory(RequirementContentHistoryEntity requirementContentHistory) {
        this.requirementContentHistory = requirementContentHistory;
        return this;
    }


    public String getTaskName() {
        return task.getTaskVersion().getName();
    }

    public Date getBeginDate() {
        return Optional
                .ofNullable(requirementContentHistory)
                .map(RequirementContentHistoryEntity::getHistoryDate)
                .orElse(task.getBeginDate());
    }


    public String getAllocatedUser() {
        return Optional
                .ofNullable(requirementContentHistory)
                .map(RequirementContentHistoryEntity::getActor)
                .map(Actor::getNome)
                .orElse(null);
    }

    public Date getEndDate() {
        return task.getEndDate();
    }
}