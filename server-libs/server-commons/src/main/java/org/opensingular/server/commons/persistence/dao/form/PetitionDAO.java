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
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.hibernate.HibernateQuery;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.jetbrains.annotations.NotNull;
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
import org.opensingular.server.commons.persistence.context.RequirementSearchAliases;
import org.opensingular.server.commons.persistence.context.RequirementSearchContext;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.persistence.entity.form.QDraftEntity;
import org.opensingular.server.commons.persistence.entity.form.QFormPetitionEntity;
import org.opensingular.server.commons.persistence.entity.form.QPetitionEntity;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.persistence.query.RequirementSearchQuery;
import org.opensingular.server.commons.persistence.query.RequirementSearchQueryFactory;
import org.opensingular.server.commons.persistence.requirement.RequirementSearchExtender;
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

    public Long countQuickSearch(QuickFilter filter,
                                 List<SingularPermission> permissions,
                                 List<RequirementSearchExtender> extenders) {
        return countQuickSearch(new RequirementSearchContext(filter)
                .setCount(Boolean.TRUE)
                .setEvaluatePermissions(Boolean.TRUE)
                .setExtenders(extenders)
                .addPermissions(permissions));
    }

    public Long countQuickSearch(QuickFilter filter, List<RequirementSearchExtender> extenders) {
        return countQuickSearch(new RequirementSearchContext(filter)
                .setExtenders(extenders)
                .setCount(Boolean.TRUE));
    }

    private Long countQuickSearch(RequirementSearchContext query) {
        return (Long) makeRequirementSearchQuery(query).uniqueResult();
    }

    private Query makeRequirementSearchQuery(RequirementSearchContext ctx) {
        RequirementSearchQueryFactory searchQueryFactory = new RequirementSearchQueryFactory(ctx);
        RequirementSearchQuery requirementSearchQuery = searchQueryFactory.make(getSession());
        return requirementSearchQuery.toHibernateQuery(ctx.getCount());
    }

    public List<Map<String, Serializable>> quickSearchMap(QuickFilter filter,
                                                          List<RequirementSearchExtender> extenders) {
        return quickSearchMap(new RequirementSearchContext(filter)
                .setExtenders(extenders)
                .setCount(Boolean.FALSE));
    }

    public List<Map<String, Serializable>> quickSearchMap(QuickFilter filter,
                                                          List<SingularPermission> permissions,
                                                          List<RequirementSearchExtender> extenders) {
        return quickSearchMap(new RequirementSearchContext(filter)
                .setCount(Boolean.FALSE)
                .setEvaluatePermissions(Boolean.TRUE)
                .setExtenders(extenders)
                .addPermissions(permissions));
    }

    private List<Map<String, Serializable>> quickSearchMap(RequirementSearchContext query) {
        return makeRequirementSearchQuery(query)
                .setFirstResult(query.getQuickFilter().getFirst())
                .setMaxResults(query.getQuickFilter().getCount())
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .list();
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
    public void delete(T petition) {
        findFormAttachmentByPetitionCod(petition.getCod()).forEach(getSession()::delete);
        petition.getFormPetitionEntities().forEach(formPetitionEntity -> {
            FormEntity formEntity = formPetitionEntity.getForm();
            if (formEntity == null && formPetitionEntity.getCurrentDraftEntity() != null) {
                formEntity = formPetitionEntity.getCurrentDraftEntity().getForm();
            }
            if (formEntity != null) {
                FormVersionEntity formVersionEntity = formEntity.getCurrentFormVersionEntity();
                getSession().delete(formVersionEntity);
                formEntity.setCurrentFormVersionEntity(null);
            }
        });
        getSession().flush();
        super.delete(petition);
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
        QPetitionEntity petition = new QPetitionEntity("petitionEntity");
        QFormPetitionEntity formPetition = new QFormPetitionEntity("formPetitionEntity");
        QFormEntity form = new QFormEntity("formEntity");
        QDraftEntity currentDraft = new QDraftEntity("draftEntity");
        QFormEntity draftForm = new QFormEntity("draftFormEntity");
        QFormVersionEntity formVersion = new QFormVersionEntity("formVersionEntity");
        QFormAttachmentEntity formAttachment = new QFormAttachmentEntity("formAttachmentEntity");

        return new HibernateQueryFactory(getSession())
                .selectDistinct(formAttachment)
                .from(petition)
                .innerJoin(petition.formPetitionEntities, formPetition)
                .leftJoin(formPetition.form, form)
                .leftJoin(formPetition.currentDraftEntity, currentDraft)
                .leftJoin(currentDraft.form, draftForm)
                .from(formVersion)
                .from(formAttachment)
                .where(new BooleanBuilder()
                        .and(formVersion.formEntity.cod.eq(form.cod)
                                .or(formVersion.formEntity.cod.eq(draftForm.cod)))
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
        QPetitionEntity petition = new QPetitionEntity("petition");
        QFormPetitionEntity formPetition = new QFormPetitionEntity("formPetition");
        QFormEntity form = new QFormEntity("form");
        QFormTypeEntity formTypeEntity = new QFormTypeEntity("formType");

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