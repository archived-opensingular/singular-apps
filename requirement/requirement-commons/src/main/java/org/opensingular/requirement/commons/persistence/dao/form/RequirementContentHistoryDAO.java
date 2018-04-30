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

package org.opensingular.requirement.commons.persistence.dao.form;

import org.hibernate.Query;
import org.opensingular.flow.persistence.entity.TaskInstanceEntity;
import org.opensingular.form.SType;
import org.opensingular.lib.support.persistence.BaseDAO;
import org.opensingular.requirement.commons.persistence.dto.RequirementHistoryDTO;
import org.opensingular.requirement.commons.persistence.entity.form.FormVersionHistoryEntity;
import org.opensingular.requirement.commons.persistence.entity.form.RequirementContentHistoryEntity;
import org.opensingular.requirement.commons.service.RequirementUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RequirementContentHistoryDAO extends BaseDAO<RequirementContentHistoryEntity, Long> {

    public RequirementContentHistoryDAO() {
        super(RequirementContentHistoryEntity.class);
    }

    public List<RequirementHistoryDTO> listRequirementContentHistoryByCodRequirement(Long codRequirement) {

        final List<TaskInstanceEntity>              tasks;
        final List<RequirementContentHistoryEntity> histories;
        final List<RequirementHistoryDTO>           requirementHistoryDTOS;
        final List<Integer>                         requirementHistoryTaskCods;

        tasks = getSession()
                .createQuery("select task from RequirementEntity p " +
                        " inner join p.flowInstanceEntity.tasks as task where p.cod = :codRequirement")
                .setParameter("codRequirement", codRequirement).list();

        histories = getSession()
                .createQuery("select h from RequirementContentHistoryEntity h " +
                        " where h.requirementEntity.cod = :codRequirement")
                .setParameter("codRequirement", codRequirement).list();

        requirementHistoryDTOS = new ArrayList<>();

        histories.forEach(history -> requirementHistoryDTOS
                .add(new RequirementHistoryDTO().setRequirementContentHistory(history).setTask(history.getTaskInstanceEntity())));

        requirementHistoryTaskCods = histories
                .stream()
                .map(RequirementContentHistoryEntity::getTaskInstanceEntity)
                .map(TaskInstanceEntity::getCod)
                .collect(Collectors.toList());

        tasks
                .stream()
                .filter(task -> !requirementHistoryTaskCods.contains(task.getCod()))
                .forEach(task -> requirementHistoryDTOS.add(new RequirementHistoryDTO().setTask(task)));


        return requirementHistoryDTOS
                .stream()
                .sorted(Comparator.comparing(a -> a.getTask().getBeginDate()))
                .collect(Collectors.toList());

    }

    public Optional<FormVersionHistoryEntity> findLastByCodRequirementAndType(Class<? extends SType<?>> typeClass, Long cod) {
        return findLastByCodRequirementAndType(RequirementUtil.getTypeName(typeClass), cod);
    }

    public Optional<FormVersionHistoryEntity> findLastByCodRequirementAndType(String typeName, Long cod) {
        return findLastByCodRequirementCodTaskInstanceAndType(typeName, cod, null);
    }


    public Optional<FormVersionHistoryEntity> findLastByCodRequirementCodTaskInstanceAndType(Class<? extends SType<?>> typeClass, Long codRequirement, Integer codTaskInstance) {
        return findLastByCodRequirementCodTaskInstanceAndType(RequirementUtil.getTypeName(typeClass), codRequirement, codTaskInstance);
    }

    public Optional<FormVersionHistoryEntity> findLastByCodRequirementCodTaskInstanceAndType(@Nonnull String typeName, @Nonnull Long codRequirement, @Nullable Integer codTaskInstance) {
        boolean filterByTask = codTaskInstance != null;
        StringBuilder query = new StringBuilder()
                .append(" select fvhe from RequirementContentHistoryEntity p ")
                .append(" inner join p.formVersionHistoryEntities  fvhe ")
                .append(" inner join fvhe.formVersion fv  ")
                .append(" inner join fv.formEntity fe  ")
                .append(" inner join fe.formType ft  ");
        if (filterByTask) {
            query.append(" inner join p.taskInstanceEntity tie ");
        }
        query.append(" where ft.abbreviation = :typeName and p.requirementEntity.cod = :codRequirement ");
        if (filterByTask) {
            query.append(" and tie.cod = :codTaskInstance ");
        }
        query.append(" order by p.historyDate desc ");


        Query hql = getSession().createQuery(query.toString());

        hql
                .setParameter("typeName", typeName)
                .setParameter("codRequirement", codRequirement);
        if (filterByTask) {
            hql.setParameter("codTaskInstance", codTaskInstance);
        }

        return findUniqueResult(FormVersionHistoryEntity.class, hql);
    }
}