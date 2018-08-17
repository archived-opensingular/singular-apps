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

package org.opensingular.requirement.module.persistence.dao.form;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.form.persistence.entity.QFormEntity;
import org.opensingular.form.persistence.entity.QFormTypeEntity;
import org.opensingular.form.persistence.entity.QFormVersionEntity;
import org.opensingular.lib.support.persistence.BaseDAO;
import org.opensingular.requirement.module.persistence.entity.form.FormRequirementEntity;
import org.opensingular.requirement.module.persistence.entity.form.QDraftEntity;
import org.opensingular.requirement.module.persistence.entity.form.QFormRequirementEntity;
import org.opensingular.requirement.module.persistence.entity.form.QRequirementEntity;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class FormRequirementDAO extends BaseDAO<FormRequirementEntity, Long> {

    public FormRequirementDAO() {
        super(FormRequirementEntity.class);
    }

    @Nonnull
    public Optional<FormRequirementEntity> findFormRequirementEntityByTypeName(@Nonnull Long requirementPK,
                                                                               @Nonnull String typeName) {
        return findUniqueResult(FormRequirementEntity.class, getSession()
                .createCriteria(FormRequirementEntity.class)
                .createAlias("form", "formEntity")
                .createAlias("formEntity.formType", "formType")
                .add(Restrictions.eq("requirement.cod", requirementPK))
                .add(Restrictions.eq("formType.abbreviation", typeName)));
    }

    public Optional<FormRequirementEntity> findFormRequirementEntityByTypeNameAndTask(@Nonnull Long requirementPK,
                                                                                      @Nonnull String typeName, @Nonnull Integer taskDefinitionEntityPK) {
        return findUniqueResult(FormRequirementEntity.class, getSession()
                .createCriteria(FormRequirementEntity.class)
                .createAlias("form", "formEntity")
                .createAlias("formEntity.formType", "formType")
                .add(Restrictions.eq("requirement.cod", requirementPK))
                .add(Restrictions.eq("formType.abbreviation", typeName))
                .add(Restrictions.eq("taskDefinitionEntity.cod", taskDefinitionEntityPK)));
    }

    public Optional<FormRequirementEntity> findLastFormRequirementEntityByTypeName(@Nonnull Long requirementPK,
                                                                                   @Nonnull String typeName) {
        return findUniqueResult(FormRequirementEntity.class, getSession()
                .createCriteria(FormRequirementEntity.class)
                .createAlias("form", "formEntity")
                .createAlias("formEntity.formType", "formType")
                .createAlias("formEntity.currentFormVersionEntity", "currentFormVersion")
                .add(Restrictions.eq("requirement.cod", requirementPK))
                .add(Restrictions.eq("formType.abbreviation", typeName))
                .addOrder(Order.desc("currentFormVersion.inclusionDate")));
    }


    public Optional<FormVersionEntity> findLastDraftByTypeName(@Nonnull Long requirementPK, @Nonnull String typeName) {
        QFormRequirementEntity formRequirementEntity = QFormRequirementEntity.formRequirementEntity;
        QRequirementEntity     requirementEntity     = QRequirementEntity.requirementEntity;
        QDraftEntity           draftEntity           = QDraftEntity.draftEntity;
        QFormEntity            formEntity            = QFormEntity.formEntity;
        QFormTypeEntity        formTypeEntity        = QFormTypeEntity.formTypeEntity;
        QFormVersionEntity     formVersionEntity     = QFormVersionEntity.formVersionEntity;

        return Optional.ofNullable(new HibernateQueryFactory(getSession())
                .from(formRequirementEntity)
                .join(formRequirementEntity.requirement, requirementEntity)
                .join(formRequirementEntity.currentDraftEntity, draftEntity)
                .join(draftEntity.form, formEntity)
                .join(formEntity.formType, formTypeEntity)
                .join(formEntity.currentFormVersionEntity, formVersionEntity)
                .where(
                        requirementEntity.cod.eq(requirementPK)
                                .and(formTypeEntity.abbreviation.eq(typeName))
                )
                .select(formVersionEntity)
                .orderBy(formVersionEntity.inclusionDate.desc())
                .fetchFirst());
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public List<FormVersionEntity> findTwoLastFormVersions(@Nonnull Long codForm) {
        return getSession()
                .createCriteria(FormVersionEntity.class)
                .createAlias("formEntity", "formEntity")
                .add(Restrictions.eq("formEntity.cod", codForm))
                .addOrder(Order.desc("inclusionDate"))
                .setMaxResults(2)
                .list();
    }

    public FormVersionEntity findPreviousVersion(@Nonnull Long formVersionCod) {
        QFormVersionEntity qAllFormVersion     = QFormVersionEntity.formVersionEntity;
        QFormVersionEntity qCurrentFormVersion = QFormVersionEntity.formVersionEntity;
        return new HibernateQueryFactory(getSession())
                .from(qAllFormVersion)
                .where(qAllFormVersion.formEntity
                        .eq(JPAExpressions
                                .select(qCurrentFormVersion.formEntity)
                                .from(qCurrentFormVersion)
                                .where(qCurrentFormVersion.cod.eq(formVersionCod)))
                        .and(qAllFormVersion.cod.ne(formVersionCod))
                        .and(qAllFormVersion.inclusionDate.before(JPAExpressions
                                .select(qCurrentFormVersion.inclusionDate)
                                .from(qCurrentFormVersion)
                                .where(qCurrentFormVersion.cod.eq(formVersionCod))))
                )
                .select(qAllFormVersion)
                .orderBy(new OrderSpecifier<>(com.querydsl.core.types.Order.DESC, qAllFormVersion.inclusionDate))
                .fetchFirst();
    }

    @Nonnull
    public Long countVersions(@Nonnull Long codForm) {
        return (Long) getSession()
                .createCriteria(FormVersionEntity.class)
                .createAlias("formEntity", "formEntity")
                .add(Restrictions.eq("formEntity.cod", codForm))
                .setProjection(Projections.countDistinct("cod"))
                .setMaxResults(1)
                .uniqueResult();
    }
}
