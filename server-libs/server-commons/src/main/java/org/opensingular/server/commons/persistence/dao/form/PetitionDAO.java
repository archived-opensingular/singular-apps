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
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.spring.security.PetitionAuthMetadataDTO;
import org.opensingular.server.commons.util.JPAQueryUtil;

import java.io.Serializable;
import java.util.HashMap;
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

    public Long countQuickSearch(QuickFilter filtro) {
        return (Long) createQuery(filtro, true).uniqueResult();
    }

    public List<Map<String, Serializable>> quickSearchMap(QuickFilter filter) {
        return createQuery(filter, false)
                .setFirstResult(filter.getFirst())
                .setMaxResults(filter.getCount())
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .list();
    }

    private void buildSelectClause(StringBuilder hql, boolean count, QuickFilter filter) {
        if (count) {
            hql.append("SELECT count(petition) ");
        } else {
            hql.append(" SELECT");
            hql.append("   petition.cod as codPeticao ");
            hql.append(" , petition.description as description ");
            hql.append(" , taskVersion.name as situation ");
            hql.append(" , taskVersion.name as taskName ");
            hql.append(" , taskVersion.type as taskType ");
            hql.append(" , processDefinitionEntity.name as processName ");
            hql.append(" , case when currentFormDraftVersionEntity is null then currentFormVersion.inclusionDate else currentFormDraftVersionEntity.inclusionDate end as creationDate ");
            hql.append(" , case when formType.abbreviation is null then formDraftType.abbreviation else formType.abbreviation end as type ");
            hql.append(" , processDefinitionEntity.key as processType ");
            hql.append(" , task.beginDate as situationBeginDate ");
            hql.append(" , task.cod as taskInstanceId ");
            hql.append(" , processInstance.beginDate as processBeginDate ");
            hql.append(" , processInstance.beginDate as creationDate ");
            hql.append(" , currentDraftEntity.editionDate as editionDate ");
            hql.append(" , processInstance.cod as processInstanceId ");
            hql.append(" , petition.rootPetition.id as rootPetition ");
            hql.append(" , petition.parentPetition.id as parentPetition ");
            hql.append(" , taskDefinition.cod as taskId");
            hql.append(" , task.versionStamp as versionStamp");
            hql.append(" , allocatedUser.codUsuario as codUsuarioAlocado");
            hql.append(" , allocatedUser.nome as nomeUsuarioAlocado");
            hql.append(" , processGroup.cod as processGroupCod");
            hql.append(" , processGroup.connectionURL as processGroupContext");
            appendCustomSelectClauses(hql, filter);
        }
    }

    /**
     * Append Custom Select Clauses
     */
    protected void appendCustomSelectClauses(StringBuilder hql, QuickFilter filter) {
    }

    private void buildFromClause(StringBuilder hql, QuickFilter filtro) {
        hql.append(" FROM ").append(tipo.getName()).append(" petition ");
        hql.append(" LEFT JOIN petition.petitioner petitioner ");
        hql.append(" LEFT JOIN petition.processInstanceEntity processInstance ");
        hql.append(" LEFT JOIN petition.formPetitionEntities formPetitionEntity on formPetitionEntity.mainForm = :sim ");
        hql.append(" LEFT JOIN formPetitionEntity.form formEntity ");
        hql.append(" LEFT JOIN formPetitionEntity.currentDraftEntity currentDraftEntity ");
        hql.append(" LEFT JOIN currentDraftEntity.form formDraftEntity");
        hql.append(" LEFT JOIN formDraftEntity.currentFormVersionEntity currentFormDraftVersionEntity");
        hql.append(" LEFT JOIN formEntity.currentFormVersionEntity currentFormVersion ");
        hql.append(" LEFT JOIN petition.processDefinitionEntity processDefinitionEntity ");
        hql.append(" LEFT JOIN processDefinitionEntity.processGroup processGroup ");
        hql.append(" LEFT JOIN formEntity.formType formType  ");
        hql.append(" LEFT JOIN formDraftEntity.formType formDraftType  ");
        hql.append(" LEFT JOIN processInstance.tasks task ");
        hql.append(" LEFT JOIN task.task taskVersion ");
        hql.append(" LEFT JOIN taskVersion.taskDefinition taskDefinition ");
        hql.append(" LEFT JOIN task.allocatedUser allocatedUser ");
        appendCustomFromClauses(hql, filtro);
    }

    /**
     * Append Custom From Clauses
     */
    protected void appendCustomFromClauses(StringBuilder hql, QuickFilter filtro) {
    }


    private void buildWhereClause(StringBuilder hql,
                                  Map<String, Object> params,
                                  QuickFilter filtro,
                                  boolean count) {

        hql.append(" WHERE 1=1 ");

        if (filtro.getIdPessoa() != null) {
            hql.append(" AND petitioner.idPessoa = :idPessoa ");
            params.put("idPessoa", filtro.getIdPessoa());
        }

        params.put("sim", SimNao.SIM);

        if (!filtro.isRascunho() && filtro.getProcessesAbbreviation() != null && !filtro.getProcessesAbbreviation().isEmpty()) {
            hql.append(" AND ( processDefinitionEntity.key  in (:siglasProcesso) ");
            params.put("siglasProcesso", filtro.getProcessesAbbreviation());
            if (filtro.getTypesNames() != null && !filtro.getTypesNames().isEmpty()) {
                hql.append(" OR formType.abbreviation in (:formNames)) ");
                params.put("formNames", filtro.getTypesNames());
            } else {
                hql.append(" ) ");
            }
        }

        appendCustomQuickFilter(hql, params, filtro);

        if (!CollectionUtils.isEmpty(filtro.getTasks())) {
            hql.append(" AND taskVersion.name in (:tasks)");
            params.put("tasks", filtro.getTasks());
        }

        if (filtro.isRascunho()) {
            hql.append(" AND petition.processInstanceEntity is null ");
        } else {
            hql.append(" AND petition.processInstanceEntity is not null ");
            if (filtro.getEndedTasks() == null) {
                hql.append(" and (taskVersion.type = :tipoEnd or (taskVersion.type <> :tipoEnd and task.endDate is null)) ");
                params.put("tipoEnd", TaskType.END);
            } else if (Boolean.TRUE.equals(filtro.getEndedTasks())) {
                hql.append(" and taskVersion.type = :tipoEnd ");
                params.put("tipoEnd", TaskType.END);
            } else {
                hql.append(" and task.endDate is null ");
            }
        }

        appendCustomWhereClauses(hql, params, filtro);

        appendSort(hql, filtro, count);
    }

    private void appendSort(StringBuilder hql, QuickFilter filtro, boolean count) {
        if (filtro.getSortProperty() != null) {
            hql.append(mountSort(filtro.getSortProperty(), filtro.isAscending()));
        } else if (!count) {
            if (filtro.isRascunho()) {
                hql.append(mountSort("creationDate", false));
            } else {
                hql.append(mountSort("processBeginDate", false));
            }
        }
    }

    /**
     * Append Custom Where Clauses
     */
    protected void appendCustomWhereClauses(StringBuilder hql, Map<String, Object> params, QuickFilter filter) {
    }

    /**
     * Append Custom Quick Filter
     */
    protected void appendCustomQuickFilter(StringBuilder hql, Map<String, Object> params, QuickFilter filter) {
        if (filter.hasFilter()) {
            hql.append(" AND ( upper(petition.description) like upper(:filter) ");
            hql.append(" OR upper(processDefinitionEntity.name) like upper(:filter) ");
            hql.append(" OR upper(taskVersion.name) like upper(:filter) ");
            if (filter.isRascunho()) {
                hql.append(" OR ").append(JPAQueryUtil.formattDateTimeClause("currentFormVersion.inclusionDate", "filter"));
                hql.append(" OR ").append(JPAQueryUtil.formattDateTimeClause("currentDraftEntity.editionDate", "filter"));
            } else {
                hql.append(" OR ").append(JPAQueryUtil.formattDateTimeClause("task.beginDate", "filter"));
                hql.append(" OR ").append(JPAQueryUtil.formattDateTimeClause("processInstance.beginDate", "filter"));
            }
            hql.append(" OR petition.id like :filter ) ");
            params.put("filter", "%" + filter.getFilter() + "%");
        }
    }

    private Query createQuery(QuickFilter filtro, boolean count) {

        final StringBuilder       hql    = new StringBuilder();
        final Map<String, Object> params = new HashMap<>();

        buildSelectClause(hql, count, filtro);
        buildFromClause(hql, filtro);
        buildWhereClause(hql, params, filtro, count);

        final Query query = getSession().createQuery(hql.toString());
        setParametersQuery(query, params);

        return query;
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