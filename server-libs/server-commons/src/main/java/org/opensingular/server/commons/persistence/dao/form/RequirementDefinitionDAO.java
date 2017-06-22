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

package org.opensingular.server.commons.persistence.dao.form;

import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.form.persistence.entity.FormTypeEntity;
import org.opensingular.lib.support.persistence.BaseDAO;
import org.opensingular.server.commons.persistence.entity.form.RequirementDefinitionEntity;

public class RequirementDefinitionDAO<T extends RequirementDefinitionEntity> extends BaseDAO<T, Long> {

    public RequirementDefinitionDAO() {
        super((Class<T>) RequirementDefinitionEntity.class);
    }

    public RequirementDefinitionDAO(Class<T> tipo) {
        super(tipo);
    }

    public RequirementDefinitionEntity findByModuleAndName(ModuleEntity moduleEntity, FormTypeEntity formType) {
        StringBuilder hql = new StringBuilder();

        hql.append(" FROM " + RequirementDefinitionEntity.class.getName() + " as req ");
        hql.append(" WHERE req.module = :module ");
        hql.append("    AND req.formType = :formType ");

        return (RequirementDefinitionEntity) getSession().createQuery(hql.toString())
            .setParameter("module", moduleEntity)
            .setParameter("formType", formType)
            .uniqueResult();
    }
}