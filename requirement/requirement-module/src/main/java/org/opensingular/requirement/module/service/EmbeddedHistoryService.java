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

package org.opensingular.requirement.module.service;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.SessionFactory;
import org.opensingular.requirement.module.persistence.entity.form.RequirementContentHistoryEntity;
import org.opensingular.requirement.module.service.dto.EmbeddedHistoryDTO;

@Named
public class EmbeddedHistoryService {

    @Inject
    private SessionFactory sessionFactory;

    public List<EmbeddedHistoryDTO> buscarAnalisesAnteriores(Long requirementEntityPK) {
        String hql = "";
        hql += " select new " + EmbeddedHistoryDTO.class.getName() + " (p) from ";
        hql += RequirementContentHistoryEntity.class.getName() + " p ";
        hql += " where p.requirementEntity.cod = :requirementEntityPK  ";
        hql += " and p.taskInstanceEntity is not null  ";
        hql += " order by p.historyDate ASC  ";
        return sessionFactory.getCurrentSession()
                .createQuery(hql)
                .setParameter("requirementEntityPK", requirementEntityPK)
                .list();
    }
}
