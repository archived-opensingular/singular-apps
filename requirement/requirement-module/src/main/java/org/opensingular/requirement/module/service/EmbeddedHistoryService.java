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

import org.hibernate.SessionFactory;
import org.opensingular.flow.persistence.entity.TaskInstanceEntity;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.lib.support.persistence.enums.SimNao;
import org.opensingular.requirement.module.persistence.entity.form.RequirementContentHistoryEntity;
import org.opensingular.requirement.module.persistence.entity.form.RequirementEntity;
import org.opensingular.requirement.module.service.dto.EmbeddedHistoryDTO;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
public class EmbeddedHistoryService {

    @Inject
    private SessionFactory sessionFactory;


    public EmbeddedHistoryDTO findMainFormFirstVersion(Long requirementEntityPK) {
        String hql = "";
        hql += " select new " + EmbeddedHistoryDTO.class.getName() + " (r,fve) from ";
        hql += RequirementEntity.class.getName() + " r ";
        hql += "  inner join r.formRequirementEntities fre ";
        hql += "  inner join fre.form f, ";
        hql += FormVersionEntity.class.getName() + " fve ";
        hql += " where r.cod = :requirementEntityPK  ";
        hql += " and fre.mainForm = :sim  ";
        hql += " and fve.formEntity = f  ";
        hql += " order by fve.inclusionDate ASC  ";
        return (EmbeddedHistoryDTO) sessionFactory.getCurrentSession()
                .createQuery(hql)
                .setParameter("sim", SimNao.SIM)
                .setParameter("requirementEntityPK", requirementEntityPK)
                .setMaxResults(1)
                .uniqueResult();
    }

    public List<EmbeddedHistoryDTO> buscarAnalisesAnteriores(Long requirementEntityPK) {
        String hql = "";
        hql += " select new " + EmbeddedHistoryDTO.class.getName() + " (t, p) from ";
        hql += TaskInstanceEntity.class.getName() + " t ";
        hql += " INNER JOIN t.flowInstance  process ";
        hql += " INNER JOIN " + RequirementEntity.class.getName() + " req ";
        hql += " ON req.flowInstanceEntity = process";
        hql += " LEFT JOIN " + RequirementContentHistoryEntity.class.getName() + " p ";
        hql += " ON p.taskInstanceEntity = t";
        hql += " where req.cod = :requirementEntityPK  ";
        hql += " AND t.endDate is not null  ";
        hql += " order by t.endDate ASC  ";
        return sessionFactory.getCurrentSession()
                .createQuery(hql)
                .setParameter("requirementEntityPK", requirementEntityPK)
                .list();
    }

}
