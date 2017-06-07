/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http: *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.server.commons.persistence.dao.form;


import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DslExpression;
import com.querydsl.jpa.hibernate.HibernateQuery;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.opensingular.flow.core.TaskType;
import org.opensingular.form.persistence.entity.FormAttachmentEntity;
import org.opensingular.form.persistence.entity.FormEntity;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.lib.support.persistence.BaseDAO;
import org.opensingular.lib.support.persistence.enums.SimNao;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.persistence.context.PetitionSearchAliases;
import org.opensingular.server.commons.persistence.context.PetitionSearchContext;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.spring.security.PetitionAuthMetadataDTO;
import org.opensingular.server.commons.spring.security.SingularPermission;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


public class PetitionDAO<T extends PetitionEntity> extends BaseDAO<T, Long> {

    public PetitionDAO() {
        super((Class<T>) PetitionEntity.class);
    }

    public PetitionDAO(Class<T> tipo) {
        super(tipo);
    }

    @SuppressWarnings("unchecked")
    public List<T> list(String type) {
        Criteria crit = getSession().createCriteria(tipo);
        crit.add(Restrictions.eq("type", type));
        return crit.list();
    }

    public Long countQuickSearch(QuickFilter filter, List<SingularPermission> permissions) {
        return countQuickSearch(new PetitionSearchContext(filter).setCount(Boolean.TRUE).setEvaluatePermissions(Boolean.TRUE).addPermissions(permissions));
    }

    public Long countQuickSearch(QuickFilter filter) {
        return countQuickSearch(new PetitionSearchContext(filter).setCount(Boolean.TRUE));
    }

    public Long countQuickSearch(PetitionSearchContext query) {
        return (Long) createQuery(query).uniqueResult();
    }

    public List<Map<String, Serializable>> quickSearchMap(QuickFilter filter) {
        return quickSearchMap(new PetitionSearchContext(filter).setCount(Boolean.FALSE));
    }

    public List<Map<String, Serializable>> quickSearchMap(QuickFilter filter, List<SingularPermission> permissions) {
        return quickSearchMap(new PetitionSearchContext(filter).setCount(Boolean.FALSE).setEvaluatePermissions(Boolean.TRUE).addPermissions(permissions));
    }

    public List<Map<String, Serializable>> quickSearchMap(PetitionSearchContext query) {
        return createQuery(query)
                .setFirstResult(query.getQuickFilter().getFirst())
                .setMaxResults(query.getQuickFilter().getCount())
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .list();
    }


    private void mountSearchpetitionQuery(PetitionSearchContext ctx) {

        HibernateQuery<Map<String, Object>> query         = ctx.createQuery(getSession());
        PetitionSearchAliases               aliases       = ctx.getPetitionSearchAliases();
        Map<String, DslExpression<?>>       selects       = ctx.getSelects();
        List<BooleanExpression>             whereClasuses = ctx.getWhereClasuses();

        if (Boolean.TRUE.equals(ctx.getCount())) {
            selects.put("total", aliases.petition.count());
        } else {
            selects.put("codPeticao", aliases.petition.cod);
            selects.put("description", aliases.petition.description);
            selects.put("situation", aliases.taskVersion.name);
            selects.put("taskName", aliases.taskVersion.name);
            selects.put("taskType", aliases.taskVersion.type);
            selects.put("processName", aliases.processDefinitionEntity.name);
            selects.put("creationDate", new CaseBuilder().when(aliases.currentFormDraftVersionEntity.isNull()).then(aliases.currentFormVersion.inclusionDate).otherwise(aliases.currentFormDraftVersionEntity.inclusionDate));
            selects.put("type", new CaseBuilder().when(aliases.formType.abbreviation.isNull()).then(aliases.formDraftType.abbreviation).otherwise(aliases.formType.abbreviation));
            selects.put("processType", aliases.processDefinitionEntity.key);
            selects.put("situationBeginDate", aliases.task.beginDate);
            selects.put("taskInstanceId", aliases.task.cod);
            selects.put("processBeginDate", aliases.processInstance.beginDate);
            selects.put("editionDate", aliases.currentDraftEntity.editionDate);
            selects.put("processInstanceId", aliases.processInstance.cod);
            selects.put("rootPetition", aliases.petition.rootPetition.cod);
            selects.put("parentPetition", aliases.petition.parentPetition.cod);
            selects.put("taskId", aliases.taskDefinition.cod);
            selects.put("versionStamp", aliases.task.versionStamp);
            selects.put("codUsuarioAlocado", aliases.allocatedUser.codUsuario);
            selects.put("nomeUsuarioAlocado", aliases.allocatedUser.nome);
            selects.put("processGroupCod", aliases.processGroup.cod);
            selects.put("processGroupContext", aliases.processGroup.connectionURL);
        }

        query
                .from(aliases.petition)
                .leftJoin(aliases.petition.petitioner, aliases.petitionerEntity)
                .leftJoin(aliases.petition.processInstanceEntity, aliases.processInstance)
                .leftJoin(aliases.petition.formPetitionEntities, aliases.formPetitionEntity).on(aliases.formPetitionEntity.mainForm.eq(SimNao.SIM))
                .leftJoin(aliases.formPetitionEntity.form, aliases.formEntity)
                .leftJoin(aliases.formPetitionEntity.currentDraftEntity, aliases.currentDraftEntity)
                .leftJoin(aliases.currentDraftEntity.form, aliases.formDraftEntity)
                .leftJoin(aliases.formDraftEntity.currentFormVersionEntity, aliases.currentFormDraftVersionEntity)
                .leftJoin(aliases.formEntity.currentFormVersionEntity, aliases.currentFormVersion)
                .leftJoin(aliases.petition.processDefinitionEntity, aliases.processDefinitionEntity)
                .leftJoin(aliases.processDefinitionEntity.processGroup, aliases.processGroup)
                .leftJoin(aliases.formEntity.formType, aliases.formType)
                .leftJoin(aliases.formDraftEntity.formType, aliases.formDraftType)
                .leftJoin(aliases.processInstance.tasks, aliases.task)
                .leftJoin(aliases.task.task, aliases.taskVersion)
                .leftJoin(aliases.taskVersion.taskDefinition, aliases.taskDefinition)
                .leftJoin(aliases.task.allocatedUser, aliases.allocatedUser);

        QuickFilter quickFilter = ctx.getQuickFilter();

        if (quickFilter.getIdPessoa() != null) {
            whereClasuses.add(aliases.petitionerEntity.idPessoa.eq(quickFilter.getIdPessoa()));
        }

        if (!quickFilter.isRascunho()
                && quickFilter.getProcessesAbbreviation() != null
                && !quickFilter.getProcessesAbbreviation().isEmpty()) {
            BooleanExpression expr = aliases.processDefinitionEntity.key.in(quickFilter.getProcessesAbbreviation());
            if (quickFilter.getTypesNames() != null && !quickFilter.getTypesNames().isEmpty()) {
                expr = expr.or(aliases.formType.abbreviation.in(quickFilter.getTypesNames()));
            }
            whereClasuses.add(expr);
        }

        String filterAnywhere = "%" + ctx.getQuickFilter().getFilter() + "%";
        if (ctx.getQuickFilter().hasFilter()) {
            BooleanExpression expr = aliases.petition.description.likeIgnoreCase(filterAnywhere);
            expr = expr.or(aliases.processDefinitionEntity.name.likeIgnoreCase(filterAnywhere));
            expr = expr.or(aliases.taskVersion.name.likeIgnoreCase(filterAnywhere));
            expr = expr.or(aliases.petition.cod.like(filterAnywhere));
            whereClasuses.add(expr);
//            if (quickFilter.isRascunho()) {
//                query.append(" OR ").append(JPAQueryUtil.formattDateTimeClause("currentFormVersion.inclusionDate", "filter"));
//                query.append(" OR ").append(JPAQueryUtil.formattDateTimeClause("currentDraftEntity.editionDate", "filter"));
//            } else {
//                query.append(" OR ").append(JPAQueryUtil.formattDateTimeClause("task.beginDate", "filter"));
//                query.append(" OR ").append(JPAQueryUtil.formattDateTimeClause("processInstance.beginDate", "filter"));
//            }
        }

        if (!CollectionUtils.isEmpty(quickFilter.getTasks())) {
            whereClasuses.add(aliases.taskVersion.name.in(quickFilter.getTasks()));
        }

        if (quickFilter.isRascunho()) {
            whereClasuses.add(aliases.petition.processInstanceEntity.isNull());
        } else {
            whereClasuses.add(aliases.petition.processInstanceEntity.isNotNull());
            if (quickFilter.getEndedTasks() == null) {
                whereClasuses.add(aliases.taskVersion.type.eq(TaskType.END).or(aliases.taskVersion.type.ne(TaskType.END).and(aliases.task.endDate.isNull())));
            } else if (Boolean.TRUE.equals(quickFilter.getEndedTasks())) {
                whereClasuses.add(aliases.taskVersion.type.eq(TaskType.END));
            } else {
                whereClasuses.add(aliases.task.endDate.isNull());
            }
        }

        customizeQuery(ctx);

        if (quickFilter.getSortProperty() != null) {
            Order order = quickFilter.isAscending() ? Order.ASC : Order.DESC;
            query.orderBy(new OrderSpecifier(order, selects.get(quickFilter.getSortProperty())));
        } else if (!Boolean.TRUE.equals(ctx.getCount())) {
            if (quickFilter.isRascunho()) {
                query.orderBy(new OrderSpecifier(Order.ASC, selects.get("creationDate")));
            } else {
                query.orderBy(new OrderSpecifier(Order.ASC, selects.get("processBeginDate")));
            }
        }

        DslExpression<?>[] selectExpressions = selects.entrySet()
                .stream()
                .map((entry) -> entry.getValue().as(entry.getKey()))
                .collect(Collectors.toList())
                .toArray(new DslExpression<?>[]{});

        if (selectExpressions.length > 1) {
            query.select(selectExpressions);
        } else if (selects.size() == 1) {
            query.select(selectExpressions[0]);
        }

        query.where(whereClasuses.toArray(new BooleanExpression[]{}));

    }

    protected void customizeQuery(PetitionSearchContext ctx) {

    }


    private Query createQuery(PetitionSearchContext ctx) {
        mountSearchpetitionQuery(ctx);
        return ctx.getQuery().createQuery();
    }

    protected String mountSort(String sortProperty, boolean ascending) {
        return " ORDER BY " + sortProperty + (ascending ? " asc " : " desc ");
    }

    public T findByProcessCodOrException(Integer cod) {
        return findByProcessCod(cod).orElseThrow(
                () -> new SingularServerException("Não foi encontrado a petição com processInstanceEntity.cod=" + cod));
    }

    public Optional<T> findByProcessCod(Integer cod) {
        Objects.requireNonNull(cod);
        return findUniqueResult(tipo, getSession()
                .createCriteria(tipo)
                .add(Restrictions.eq("processInstanceEntity.cod", cod)));
    }

    public T findByFormEntity(FormEntity formEntity) {
        return (T) getSession()
                .createQuery(" select p from " + tipo.getName() + " p inner join p.formPetitionEntities fpe where fpe.form = :form ")
                .setParameter("form", formEntity)
                .setMaxResults(1)
                .uniqueResult();
    }

    @Override
    public void delete(T obj) {
        findFormAttachmentByPetitionCod(obj.getCod()).forEach(getSession()::delete);
        FormEntity        mainForm          = obj.getMainForm();
        FormVersionEntity formVersionEntity = mainForm.getCurrentFormVersionEntity();
        getSession().delete(formVersionEntity);
        mainForm.setCurrentFormVersionEntity(null);
        getSession().flush();
        super.delete(obj);
    }

    public PetitionAuthMetadataDTO findPetitionAuthMetadata(Long petitionId) {
        StringBuilder query = new StringBuilder();
        query.append(" select distinct new ").append(PetitionAuthMetadataDTO.class.getName()).append("(ft.abbreviation, ftm.abbreviation, td.abbreviation, pd.key, ct.cod) from ");
        query.append(' ').append(PetitionEntity.class.getName()).append(" pe ");
        query.append(" left join pe.processDefinitionEntity pd  ");
        query.append(" left join pe.processInstanceEntity pi  ");
        query.append(" left join pi.tasks ct  ");
        query.append(" left join ct.task t  ");
        query.append(" left join t.taskDefinition td  ");
        query.append(" left join pe.formPetitionEntities fpe ");
        query.append(" left join fpe.form for ");
        query.append(" left join for.formType ftm ");
        query.append(" left join fpe.currentDraftEntity cde  ");
        query.append(" left join cde.form  f ");
        query.append(" left join f.formType ft ");
        query.append(" where pe.cod = :petitionId and (ftm is null or fpe.mainForm = :sim ) AND (ct.endDate is null or t.type = :fim )");
        query.append(" order by ct.cod DESC ");
        return (PetitionAuthMetadataDTO) Optional.ofNullable(getSession().createQuery(query.toString())
                .setParameter("sim", SimNao.SIM)
                .setParameter("fim", TaskType.END)
                .setParameter("petitionId", petitionId)
                .setMaxResults(1)
                .list())
                .filter(l -> !l.isEmpty())
                .map(l -> l.get(0))
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    public List<PetitionEntity> findByRootPetition(T rootPetition) {
        String hql = "FROM " + PetitionEntity.class.getName() + " pe "
                + " WHERE pe.rootPetition = :rootPetition ";

        Query query = getSession().createQuery(hql);
        query.setParameter("rootPetition", rootPetition);
        return query.list();
    }

    public List<FormAttachmentEntity> findFormAttachmentByPetitionCod(Long petitionCod) {
        return getSession()
                .createQuery(" select distinct(fa) from PetitionEntity p " +
                        " inner join p.formPetitionEntities fp " +
                        " left join fp.form f " +
                        " left join fp.currentDraftEntity cd " +
                        " left join cd.form cdf, " +
                        " FormVersionEntity fv," +
                        " FormAttachmentEntity fa  " +
                        " where (fv.formEntity.cod = f.cod or fv.formEntity.cod = cdf.cod) " +
                        " and fa.formVersionEntity.cod = fv.cod " +
                        " and p.cod = :petitionCod ")
                .setParameter("petitionCod", petitionCod)
                .list();
    }

    public boolean containChildren(Long petitionCod) {
        return ((Long) getSession()
                .createQuery("select count(p) from PetitionEntity p where p.parentPetition.cod = :petitionCod")
                .setParameter("petitionCod", petitionCod)
                .uniqueResult()) > 0;
    }

    public T findPetitionInstanceByRootPetitionAndType(Long rootPetition, String type) {
        return (T) getSession()
                .createQuery(" select p from PetitionEntity p" +
                        " inner join p.formPetitionEntities formPetitionEntity " +
                        " inner join formPetitionEntity.form form " +
                        " inner join form.formType formType " +
                        " where 1=1 " +
                        " and formPetitionEntity.mainForm = :sim  " +
                        " and p.rootPetition.id = :rootPetition " +
                        " and formType.abbreviation = :type ")
                .setParameter("sim", SimNao.SIM)
                .setParameter("rootPetition", rootPetition)
                .setParameter("type", type)
                .setMaxResults(1)
                .uniqueResult();
    }

}