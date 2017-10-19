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

package org.opensingular.server.commons.persistence.dao.flow;

import org.hibernate.Query;
import org.opensingular.flow.core.TaskType;
import org.opensingular.flow.persistence.entity.TaskInstanceEntity;
import org.opensingular.lib.support.persistence.BaseDAO;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;

import java.util.List;

public class TaskInstanceDAO extends BaseDAO<TaskInstanceEntity, Integer> {

    public TaskInstanceDAO() {
        super(TaskInstanceEntity.class);
    }

    protected Class<? extends PetitionEntity> getPetitionEntityClass() {
        return PetitionEntity.class;
    }

    @SuppressWarnings("unchecked")
    public List<TaskInstanceEntity> findCurrentTasksByPetitionId(Long petitionId) {
        String sb = " select ti " + " from " + getPetitionEntityClass().getName() + " pet " +
                " inner join pet.flowInstanceEntity pi " +
                " inner join pi.tasks ti " +
                " inner join ti.task task " +
                " where pet.cod = :petitionId  " +
                "   and (ti.endDate is null OR task.type = :tipoEnd)  ";

        final Query query = getSession().createQuery(sb);
        query.setParameter("petitionId", petitionId);
        query.setParameter("tipoEnd", TaskType.END);
        return query.list();
    }

}