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


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.hibernate.HibernateQuery;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.opensingular.flow.core.TaskType;
import org.opensingular.form.persistence.entity.FormAttachmentEntity;
import org.opensingular.form.persistence.entity.FormEntity;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.form.persistence.entity.QFormAttachmentEntity;
import org.opensingular.form.persistence.entity.QFormEntity;
import org.opensingular.form.persistence.entity.QFormTypeEntity;
import org.opensingular.form.persistence.entity.QFormVersionEntity;
import org.opensingular.lib.support.persistence.BaseDAO;
import org.opensingular.lib.support.persistence.enums.SimNao;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.persistence.context.PetitionSearchAliases;
import org.opensingular.server.commons.persistence.context.PetitionSearchContext;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.persistence.entity.form.QDraftEntity;
import org.opensingular.server.commons.persistence.entity.form.QFormPetitionEntity;
import org.opensingular.server.commons.persistence.entity.form.QPetitionEntity;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.persistence.query.RequirementQuery;
import org.opensingular.server.commons.spring.security.PetitionAuthMetadataDTO;
import org.opensingular.server.commons.spring.security.SingularPermission;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


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

    private Long countQuickSearch(PetitionSearchContext query) {
        return (Long) mountSearchpetitionQuery(query).uniqueResult();
    }

    public List<Map<String, Serializable>> quickSearchMap(QuickFilter filter) {
        return quickSearchMap(new PetitionSearchContext(filter).setCount(Boolean.FALSE));
    }

    public List<Map<String, Serializable>> quickSearchMap(QuickFilter filter, List<SingularPermission> permissions) {
        return quickSearchMap(new PetitionSearchContext(filter).setCount(Boolean.FALSE).setEvaluatePermissions(Boolean.TRUE).addPermissions(permissions));
    }

    private List<Map<String, Serializable>> quickSearchMap(PetitionSearchContext query) {
        return mountSearchpetitionQuery(query)
                .setFirstResult(query.getQuickFilter().getFirst())
                .setMaxResults(query.getQuickFilter().getCount())
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .list();
    }


    private Query mountSearchpetitionQuery(PetitionSearchContext ctx) {

        RequirementQuery      query             = ctx.createQuery(getSession());
        PetitionSearchAliases aliases           = query.getRequirementAliases();
        BooleanBuilder        whereClause       = query.getWhereClause();
        BooleanBuilder        quickFilterClause = query.getQuickFilterClause();

        if (Boolean.TRUE.equals(ctx.getCount())) {
            query.addSelect(aliases.petition.count());
        } else {
            query.addSelect(aliases.petition.cod.as("codPeticao"))
                    .addSelect(aliases.petition.description.as("description"))
                    .addSelect(aliases.taskVersion.name.as("situation"))
                    .addSelect(aliases.taskVersion.name.as("taskName"))
                    .addSelect(aliases.taskVersion.type.as("taskType"))
                    .addSelect(aliases.processDefinitionEntity.name.as("processName"))
                    .addSelectCase($case -> $case
                            .when(aliases.currentFormDraftVersionEntity.isNull())
                            .then(aliases.currentFormVersion.inclusionDate)
                            .otherwise(aliases.currentFormDraftVersionEntity.inclusionDate).as("creationDate"))
                    .addSelectCase($case -> $case
                            .when(aliases.formType.abbreviation.isNull())
                            .then(aliases.formDraftType.abbreviation)
                            .otherwise(aliases.formType.abbreviation).as("type"))
                    .addSelect(aliases.processDefinitionEntity.key.as("processType"))
                    .addSelect(aliases.task.beginDate.as("situationBeginDate"))
                    .addSelect(aliases.task.cod.as("taskInstanceId"))
                    .addSelect(aliases.processInstance.beginDate.as("processBeginDate"))
                    .addSelect(aliases.currentDraftEntity.editionDate.as("editionDate"))
                    .addSelect(aliases.processInstance.cod.as("processInstanceId"))
                    .addSelect(aliases.petition.rootPetition.cod.as("rootPetition"))
                    .addSelect(aliases.petition.parentPetition.cod.as("parentPetition"))
                    .addSelect(aliases.taskDefinition.cod.as("taskId"))
                    .addSelect(aliases.task.versionStamp.as("versionStamp"))
                    .addSelect(aliases.allocatedUser.codUsuario.as("codUsuarioAlocado"))
                    .addSelect(aliases.allocatedUser.nome.as("nomeUsuarioAlocado"))
                    .addSelect(aliases.processGroup.cod.as("processGroupCod"))
                    .addSelect(aliases.processGroup.connectionURL.as("processGroupContext"));
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
            whereClause.and(aliases.petitionerEntity.idPessoa.eq(quickFilter.getIdPessoa()));
        }

        if (!quickFilter.isRascunho()
                && quickFilter.getProcessesAbbreviation() != null
                && !quickFilter.getProcessesAbbreviation().isEmpty()) {
            BooleanExpression expr = aliases.processDefinitionEntity.key.in(quickFilter.getProcessesAbbreviation());
            if (quickFilter.getTypesNames() != null && !quickFilter.getTypesNames().isEmpty()) {
                expr = expr.or(aliases.formType.abbreviation.in(quickFilter.getTypesNames()));
            }
            whereClause.and(expr);
        }

        String filterAnywhere = "%" + ctx.getQuickFilter().getFilter() + "%";
        if (ctx.getQuickFilter().hasFilter()) {
            quickFilterClause.or(aliases.petition.description.likeIgnoreCase(filterAnywhere));
            quickFilterClause.or(aliases.processDefinitionEntity.name.likeIgnoreCase(filterAnywhere));
            quickFilterClause.or(aliases.taskVersion.name.likeIgnoreCase(filterAnywhere));
            quickFilterClause.or(aliases.petition.cod.like(filterAnywhere));
            String toCharDate = "TO_CHAR({0}, 'DD/MM/YYYY HH24:MI')";
            if (quickFilter.isRascunho()) {
                quickFilterClause.or(Expressions.stringTemplate(toCharDate, aliases.currentFormVersion.inclusionDate).like(filterAnywhere));
                quickFilterClause.or(Expressions.stringTemplate(toCharDate, aliases.currentFormDraftVersionEntity.inclusionDate).like(filterAnywhere));
                quickFilterClause.or(Expressions.stringTemplate(toCharDate, aliases.currentDraftEntity.editionDate).like(filterAnywhere));
            } else {
                quickFilterClause.or(Expressions.stringTemplate(toCharDate, aliases.task.beginDate).like(filterAnywhere));
                quickFilterClause.or(Expressions.stringTemplate(toCharDate, aliases.processInstance.beginDate).like(filterAnywhere));
            }
        }

        if (!CollectionUtils.isEmpty(quickFilter.getTasks())) {
            whereClause.and(aliases.taskVersion.name.in(quickFilter.getTasks()));
        }

        if (quickFilter.isRascunho()) {
            whereClause.and(aliases.petition.processInstanceEntity.isNull());
        } else {
            whereClause.and(aliases.petition.processInstanceEntity.isNotNull());
            if (quickFilter.getEndedTasks() == null) {
                whereClause.and(aliases.taskVersion.type.eq(TaskType.END).or(aliases.taskVersion.type.ne(TaskType.END).and(aliases.task.endDate.isNull())));
            } else if (Boolean.TRUE.equals(quickFilter.getEndedTasks())) {
                whereClause.and(aliases.taskVersion.type.eq(TaskType.END));
            } else {
                whereClause.and(aliases.task.endDate.isNull());
            }
        }

        customizeQuery(ctx);

        if (quickFilter.getSortProperty() != null) {
            Order order = quickFilter.isAscending() ? Order.ASC : Order.DESC;
            query.orderBy(new OrderSpecifier<>(order, Expressions.stringPath(quickFilter.getSortProperty())));
        } else if (!Boolean.TRUE.equals(ctx.getCount())) {
            if (quickFilter.isRascunho()) {
                query.orderBy(new OrderSpecifier<>(Order.ASC, Expressions.stringPath("creationDate")));
            } else {
                query.orderBy(new OrderSpecifier<>(Order.ASC, Expressions.stringPath("processBeginDate")));
            }
        }

        return query.toHibernateQuery();
    }

    protected void customizeQuery(PetitionSearchContext ctx) {

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
        QPetitionEntity       petition       = new QPetitionEntity("petitionEntity");
        QFormPetitionEntity   formPetition   = new QFormPetitionEntity("formPetitionEntity");
        QFormEntity           formEntity     = new QFormEntity("formEntity");
        QDraftEntity          currentDraft   = new QDraftEntity("draftEntity");
        QFormEntity           draftForm      = new QFormEntity("draftFormEntity");
        QFormVersionEntity    formVersion    = new QFormVersionEntity("formVersionEntity");
        QFormAttachmentEntity formAttachment = new QFormAttachmentEntity("formAttachmentEntity");

        return new HibernateQueryFactory(getSession())
                .selectDistinct(formAttachment)
                .from(petition)
                .innerJoin(petition.formPetitionEntities, formPetition)
                .leftJoin(formPetition.form, formEntity)
                .leftJoin(formPetition.currentDraftEntity, currentDraft)
                .leftJoin(currentDraft.form, draftForm)
                .from(formVersion)
                .from(formAttachment)
                .where((formVersion.cod.eq(formEntity.cod).or(formVersion.formEntity.cod.eq(currentDraft.cod)))
                        .and(formAttachment.formVersionEntity.cod.eq(formVersion.cod))
                        .and(petition.cod.eq(petitionCod)))
                .fetch();
    }

    public boolean containChildren(Long petitionCod) {
        QPetitionEntity petitionEntity = QPetitionEntity.petitionEntity;
        return new HibernateQueryFactory(getSession())
                .selectFrom(petitionEntity)
                .where(petitionEntity.parentPetition.cod.eq(petitionCod))
                .fetchCount() > 0;
    }

    public T findPetitionInstanceByRootPetitionAndType(Long rootPetition, String type) {
        QPetitionEntity     petition       = new QPetitionEntity("petition");
        QFormPetitionEntity formPetition   = new QFormPetitionEntity("formPetition");
        QFormEntity         form           = new QFormEntity("form");
        QFormTypeEntity     formTypeEntity = new QFormTypeEntity("formType");

        HibernateQuery<PetitionEntity> hibernateQuery = new HibernateQueryFactory(getSession())
                .selectFrom(petition)
                .innerJoin(petition.formPetitionEntities, formPetition)
                .innerJoin(formPetition.form, form)
                .innerJoin(form.formType, formTypeEntity)
                .where(formPetition.mainForm.eq(SimNao.SIM)
                        .and(petition.rootPetition.cod.eq(rootPetition))
                        .and(formTypeEntity.abbreviation.eq(type)));
        hibernateQuery.getMetadata().setLimit(1L);

        return (T) hibernateQuery.fetchOne();
    }

}