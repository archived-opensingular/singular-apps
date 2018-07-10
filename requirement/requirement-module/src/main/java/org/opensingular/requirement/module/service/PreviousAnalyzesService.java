package org.opensingular.requirement.module.service;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.SessionFactory;
import org.opensingular.requirement.module.persistence.entity.form.RequirementContentHistoryEntity;
import org.opensingular.requirement.module.service.dto.AnaliseAnteriorDTO;

@Named
public class PreviousAnalyzesService {

    @Inject
    private SessionFactory sessionFactory;

    public List<AnaliseAnteriorDTO> buscarAnalisesAnteriores(Long requirementEntityPK) {
        String hql = "";
        hql += " select new " + AnaliseAnteriorDTO.class.getName() + " (p) from ";
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
