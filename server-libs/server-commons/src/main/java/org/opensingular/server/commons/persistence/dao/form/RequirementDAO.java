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
import com.querydsl.jpa.hibernate.HibernateQuery;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;
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
import org.opensingular.server.commons.persistence.context.RequirementSearchContext;
import org.opensingular.server.commons.persistence.entity.form.QDraftEntity;
import org.opensingular.server.commons.persistence.entity.form.QFormRequirementEntity;
import org.opensingular.server.commons.persistence.entity.form.QRequirementEntity;
import org.opensingular.server.commons.persistence.entity.form.RequirementEntity;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.persistence.query.RequirementSearchQuery;
import org.opensingular.server.commons.persistence.query.RequirementSearchQueryFactory;
import org.opensingular.server.commons.persistence.requirement.RequirementSearchExtender;
import org.opensingular.server.commons.spring.security.RequirementAuthMetadataDTO;
import org.opensingular.server.commons.spring.security.SingularPermission;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


public class RequirementDAO<T extends RequirementEntity> extends BaseDAO<T, Long> {

    public RequirementDAO() {
        super((Class<T>) RequirementEntity.class);
    }

    public RequirementDAO(Class<T> entityClass) {
        super(entityClass);
    }

    @SuppressWarnings("unchecked")
    public List<T> list(String type) {
        Criteria criteria = getSession().createCriteria(this.entityClass);
        criteria.add(Restrictions.eq("type", type));
        return criteria.list();
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
        requirementSearchQuery.addEnabledFieldFilterNames(ctx.getQuickFilter().getEnabledFieldFilters());
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

    public T findByFlowCodOrException(Integer cod) {
        return findByFlowCod(cod).orElseThrow(
                () -> new SingularServerException("Não foi encontrado a petição com flowInstanceEntity.cod=" + cod));
    }

    public Optional<T> findByFlowCod(Integer cod) {
        Objects.requireNonNull(cod);
        return findUniqueResult(entityClass, getSession()
                .createCriteria(entityClass)
                .add(Restrictions.eq("flowInstanceEntity.cod", cod)));
    }

    public T findByFormEntity(FormEntity formEntity) {
        return (T) getSession()
                .createQuery(" select p from " + entityClass.getName() + " p inner join p.formRequirementEntities fpe where fpe.form = :form ")
                .setParameter("form", formEntity)
                .setMaxResults(1)
                .uniqueResult();
    }

    @Override
    public void delete(T requirement) {
        findFormAttachmentByCodRequirement(requirement.getCod()).forEach(getSession()::delete);
        requirement.getFormRequirementEntities().forEach(formRequirementEntity -> {
            FormEntity formEntity = formRequirementEntity.getForm();
            if (formEntity == null && formRequirementEntity.getCurrentDraftEntity() != null) {
                formEntity = formRequirementEntity.getCurrentDraftEntity().getForm();
            }
            if (formEntity != null) {
                FormVersionEntity formVersionEntity = formEntity.getCurrentFormVersionEntity();
                getSession().delete(formVersionEntity);
                formEntity.setCurrentFormVersionEntity(null);
            }
        });
        getSession().flush();
        super.delete(requirement);
    }

    public RequirementAuthMetadataDTO findRequirementAuthMetadata(Long requirementId) {
        StringBuilder query = new StringBuilder();
        query.append(" select distinct new ").append(RequirementAuthMetadataDTO.class.getName()).append("(ft.abbreviation, ftm.abbreviation, td.abbreviation, pd.key, ct.cod) from ");
        query.append(' ').append(RequirementEntity.class.getName()).append(" pe ");
        query.append(" left join pe.flowDefinitionEntity pd  ");
        query.append(" left join pe.flowInstanceEntity pi  ");
        query.append(" left join pi.tasks ct  ");
        query.append(" left join ct.task t  ");
        query.append(" left join t.taskDefinition td  ");
        query.append(" left join pe.formRequirementEntities fpe ");
        query.append(" left join fpe.form for ");
        query.append(" left join for.formType ftm ");
        query.append(" left join fpe.currentDraftEntity cde  ");
        query.append(" left join cde.form  f ");
        query.append(" left join f.formType ft ");
        query.append(" where pe.cod = :requirementId and fpe.mainForm = :sim AND (ct.endDate is null or t.type = :fim )");
        query.append(" order by ct.cod DESC ");
        return (RequirementAuthMetadataDTO) Optional.ofNullable(getSession().createQuery(query.toString())
                .setParameter("sim", SimNao.SIM)
                .setParameter("fim", TaskType.END)
                .setParameter("requirementId", requirementId)
                .setMaxResults(1)
                .list())
                .filter(l -> !l.isEmpty())
                .map(l -> l.get(0))
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    public List<RequirementEntity> findByRootRequirement(T rootRequirement) {
        String hql = "FROM " + RequirementEntity.class.getName() + " pe "
                + " WHERE pe.rootRequirement = :rootRequirement ";

        Query query = getSession().createQuery(hql);
        query.setParameter("rootRequirement", rootRequirement);
        return query.list();
    }

    public List<FormAttachmentEntity> findFormAttachmentByCodRequirement(Long codRequirement) {
        QRequirementEntity requirement = new QRequirementEntity("requirementEntity");
        QFormRequirementEntity formRequirement = new QFormRequirementEntity("formRequirementEntity");
        QFormEntity form = new QFormEntity("formEntity");
        QDraftEntity currentDraft = new QDraftEntity("draftEntity");
        QFormEntity draftForm = new QFormEntity("draftFormEntity");
        QFormVersionEntity formVersion = new QFormVersionEntity("formVersionEntity");
        QFormAttachmentEntity formAttachment = new QFormAttachmentEntity("formAttachmentEntity");

        return new HibernateQueryFactory(getSession())
                .selectDistinct(formAttachment)
                .from(requirement)
                .innerJoin(requirement.formRequirementEntities, formRequirement)
                .leftJoin(formRequirement.form, form)
                .leftJoin(formRequirement.currentDraftEntity, currentDraft)
                .leftJoin(currentDraft.form, draftForm)
                .from(formVersion)
                .from(formAttachment)
                .where(new BooleanBuilder()
                        .and(formVersion.formEntity.cod.eq(form.cod)
                                .or(formVersion.formEntity.cod.eq(draftForm.cod)))
                        .and(formAttachment.formVersionEntity.cod.eq(formVersion.cod))
                        .and(requirement.cod.eq(codRequirement)))
                .fetch();
    }

    public boolean containChildren(Long codRequirement) {
        QRequirementEntity requirementEntity = QRequirementEntity.requirementEntity;
        return new HibernateQueryFactory(getSession())
                .selectFrom(requirementEntity)
                .where(requirementEntity.parentRequirement.cod.eq(codRequirement))
                .fetchCount() > 0;
    }

    public T findRequirementByRootRequirementAndType(Long rootRequirement, String type) {
        QRequirementEntity requirement = new QRequirementEntity("requirement");
        QFormRequirementEntity formRequirement = new QFormRequirementEntity("formRequirement");
        QFormEntity form = new QFormEntity("form");
        QFormTypeEntity formTypeEntity = new QFormTypeEntity("formType");

        HibernateQuery<RequirementEntity> hibernateQuery = new HibernateQueryFactory(getSession())
                .selectFrom(requirement)
                .innerJoin(requirement.formRequirementEntities, formRequirement)
                .innerJoin(formRequirement.form, form)
                .innerJoin(form.formType, formTypeEntity)
                .where(formRequirement.mainForm.eq(SimNao.SIM)
                        .and(requirement.rootRequirement.cod.eq(rootRequirement))
                        .and(formTypeEntity.abbreviation.eq(type)));
        hibernateQuery.getMetadata().setLimit(1L);

        return (T) hibernateQuery.fetchOne();
    }

}