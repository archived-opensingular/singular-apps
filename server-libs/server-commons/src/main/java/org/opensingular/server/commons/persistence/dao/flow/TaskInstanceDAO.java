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

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.opensingular.flow.core.TaskType;
import org.opensingular.flow.persistence.entity.TaskInstanceEntity;
import org.opensingular.lib.support.persistence.BaseDAO;
import org.opensingular.lib.support.persistence.enums.SimNao;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.spring.security.SingularPermission;
import org.opensingular.server.commons.util.JPAQueryUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskInstanceDAO extends BaseDAO<TaskInstanceEntity, Integer> {


    public TaskInstanceDAO() {
        super(TaskInstanceEntity.class);
    }

    protected Map<String, String> getSortPropertyToAliases() {
        Map<String, String> sortProperties = new HashMap<>();

        sortProperties.put("id", "ti.cod");
        sortProperties.put("creationDate", "pi.beginDate");
        sortProperties.put("protocolDate", "pi.beginDate");
        sortProperties.put("description", "pi.description");
        sortProperties.put("state", "tv.name");
        sortProperties.put("taskName", "tv.name");
        sortProperties.put("user", "au.nome");
        sortProperties.put("nomeUsuarioAlocado", "au.nome");
        sortProperties.put("codUsuarioAlocado", "au.cod");
        sortProperties.put("situationBeginDate", "ti.beginDate");
        sortProperties.put("processBeginDate", "pi.beginDate");

        return sortProperties;
    }

    protected Class<? extends PetitionEntity> getPetitionEntityClass() {
        return PetitionEntity.class;
    }


    public List<Map<String, Serializable>> findTasks(QuickFilter filter, List<SingularPermission> permissions) {
        return buildQuery(filter, permissions, false)
                .setMaxResults(filter.getCount())
                .setFirstResult(filter.getFirst())
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .list();
    }

    protected Query buildQuery(QuickFilter quickFilter, List<SingularPermission> permissions, boolean count) {

        String selectClause =
                count ?
                        " count( distinct ti )" :
                        " pi.cod as processInstanceId," +
                                " ti.cod as taskInstanceId," +
                                " td.cod as taskId," +
                                " ti.versionStamp as versionStamp, " +
                                " pi.beginDate as creationDate," +
                                " pi.description as description, " +
                                " au.codUsuario as codUsuarioAlocado, " +
                                " au.nome as nomeUsuarioAlocado, " +
                                " tv.name as taskName, " +
                                " form.formType.abbreviation as type, " +
                                " pd.key as processType, " +
                                " p.cod as codPeticao," +
                                " ti.beginDate as situationBeginDate,  " +
                                " pi.beginDate as processBeginDate, " +
                                " tv.type as taskType," +
                                " pg.cod as processGroupCod, " +
                                " pg.connectionURL as processGroupContext" +
                                " ";

        String condition;

        if (quickFilter.getEndedTasks() == null) {
            condition = " and (tv.type = :tipoEnd or (tv.type <> :tipoEnd and ti.endDate is null)) ";
        } else if (Boolean.TRUE.equals(quickFilter.getEndedTasks())) {
            condition = " and tv.type = :tipoEnd ";
        } else {
            condition = " and ti.endDate is null ";
        }

        String taskFilter = "";
        if (!CollectionUtils.isEmpty(quickFilter.getTasks())) {
            taskFilter += " AND tv.name in (:tasks)";
        }

        String sb = " select " +
                selectClause +
                " from " +
                getPetitionEntityClass().getName() + " p " +
                " inner join p.processInstanceEntity pi " +
                " inner join pi.processVersion pv " +
                " inner join pv.processDefinition pd " +
                " inner join pd.processGroup pg " +
                " left join pi.tasks ti " +
                " left join ti.allocatedUser au " +
                " left join ti.task tv " +
                " left join tv.taskDefinition td  " +
                " left join p.formPetitionEntities formPetitionEntity on formPetitionEntity.mainForm = :sim " +
                " left join formPetitionEntity.form form " +
                " where 1 = 1" +
                condition +
                taskFilter +
                addQuickFilter(quickFilter.getFilter()) +
                getOrderBy(quickFilter.getSortProperty(), quickFilter.isAscending(), count);


        Query query = getSession().createQuery(sb);

        if (!CollectionUtils.isEmpty(quickFilter.getTasks())) {
            query.setParameterList("tasks", quickFilter.getTasks());
        }

        query.setParameter("sim", SimNao.SIM);

        if (Boolean.TRUE.equals(quickFilter.getEndedTasks())) {
            query.setParameter("tipoEnd", TaskType.END);
        }

        return addFilterParameter(query, quickFilter.getFilter());
    }

    protected Query addFilterParameter(Query query, String filter) {
        return filter == null ? query : query
                .setParameter("filter", "%" + filter + "%");
    }

    protected String addQuickFilter(String filtro) {
        if (filtro != null) {
            String like = " like upper(:filter) ";
            return " and (  " +
                    "    ( " + JPAQueryUtil.formattDateTimeClause("ti.beginDate", "filter") + " ) " +
                    " or ( " + JPAQueryUtil.formattDateTimeClause("pi.beginDate", "filter") + " ) " +
                    " or ( upper(pi.description)  " + like + " ) " +
                    " or ( upper(tv.name) " + like + " ) " +
                    " or ( upper(au.nome) " + like + " ) " +
                    ") ";
        }
        return "";
    }

    protected String getOrderBy(String sortProperty, boolean ascending, boolean count) {
        boolean asc  = ascending;
        String  sort = sortProperty;
        if (count) {
            return "";
        }
        if (sort == null) {
            sort = "processBeginDate";
            asc = true;
        }
        return " order by " + getSortPropertyToAliases().get(sort) + (asc ? " ASC " : " DESC ");
    }


    public Long countTasks(QuickFilter filter, List<SingularPermission> permissions) {
        return ((Number) buildQuery(filter, permissions, true).uniqueResult()).longValue();
    }

    @SuppressWarnings("unchecked")
    public List<TaskInstanceEntity> findCurrentTasksByPetitionId(Long petitionId) {
        String sb = " select ti " + " from " + getPetitionEntityClass().getName() + " pet " +
                " inner join pet.processInstanceEntity pi " +
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