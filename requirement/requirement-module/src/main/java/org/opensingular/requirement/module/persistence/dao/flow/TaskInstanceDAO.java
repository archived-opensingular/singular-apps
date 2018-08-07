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

package org.opensingular.requirement.module.persistence.dao.flow;

import java.util.List;

import org.hibernate.Query;
import org.opensingular.flow.core.TaskType;
import org.opensingular.flow.persistence.entity.TaskInstanceEntity;
import org.opensingular.lib.support.persistence.BaseDAO;
import org.opensingular.requirement.module.persistence.entity.form.RequirementEntity;

public class TaskInstanceDAO extends BaseDAO<TaskInstanceEntity, Integer> {

    public TaskInstanceDAO() {
        super(TaskInstanceEntity.class);
    }

    protected Class<? extends RequirementEntity> getRequirementEntityClass() {
        return RequirementEntity.class;
    }

    @SuppressWarnings("unchecked")
    public List<TaskInstanceEntity> findCurrentTasksByRequirementId(Long requirementId) {
        String sb = " select ti " + " from " + getRequirementEntityClass().getName() + " pet " +
                " inner join pet.flowInstanceEntity pi " +
                " inner join pi.tasks ti " +
                " inner join ti.task task " +
                " where pet.cod = :requirementId  " +
                "   and (ti.endDate is null OR task.type = :tipoEnd)  ";

        final Query query = getSession().createQuery(sb);
        query.setParameter("requirementId", requirementId);
        query.setParameter("tipoEnd", TaskType.END);
        return query.list();
    }

}