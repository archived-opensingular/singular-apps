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

package org.opensingular.requirement.module.persistence.dao.form;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.lib.support.persistence.BaseDAO;
import org.opensingular.requirement.module.persistence.entity.form.FormRequirementEntity;

public class FormRequirementDAO extends BaseDAO<FormRequirementEntity, Long> {

    public FormRequirementDAO() {
        super(FormRequirementEntity.class);
    }

    @Nonnull
    public Optional<FormRequirementEntity> findFormRequirementEntityByTypeName(@Nonnull Long requirementPK,
            @Nonnull String typeName) {
        return findUniqueResult(FormRequirementEntity.class,  getSession()
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
