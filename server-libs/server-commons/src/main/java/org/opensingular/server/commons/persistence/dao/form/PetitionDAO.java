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
import org.opensingular.server.commons.persistence.context.PetitionSearchQuery;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.spring.security.PetitionAuthMetadataDTO;
import org.opensingular.server.commons.spring.security.SingularPermission;
import org.opensingular.server.commons.util.JPAQueryUtil;

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
        return countQuickSearch(new PetitionSearchQuery(filter).setCount(Boolean.TRUE).setEvaluatePermissions(Boolean.TRUE).addPermissions(permissions));
    }

    public Long countQuickSearch(QuickFilter filter) {
        return countQuickSearch(new PetitionSearchQuery(filter).setCount(Boolean.TRUE));
    }

    public Long countQuickSearch(PetitionSearchQuery query) {
        return (Long) createQuery(query).uniqueResult();
    }

    public List<Map<String, Serializable>> quickSearchMap(QuickFilter filter) {
        return quickSearchMap(new PetitionSearchQuery(filter).setCount(Boolean.FALSE));
    }

    public List<Map<String, Serializable>> quickSearchMap(QuickFilter filter, List<SingularPermission> permissions) {
        return quickSearchMap(new PetitionSearchQuery(filter).setCount(Boolean.FALSE).setEvaluatePermissions(Boolean.TRUE).addPermissions(permissions));
    }

    public List<Map<String, Serializable>> quickSearchMap(PetitionSearchQuery query) {
        return createQuery(query)
                .setFirstResult(query.getQuickFilter().getFirst())
                .setMaxResults(query.getQuickFilter().getCount())
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .list();
    }

    private void buildSelectClause(PetitionSearchQuery query) {
        if (Boolean.TRUE.equals(query.getCount())) {
            query.append("SELECT count(petition) ");
        } else {
            query.append(" SELECT");
            query.append("   petition.cod as codPeticao ");
            query.append(" , petition.description as description ");
            query.append(" , taskVersion.name as situation ");
            query.append(" , taskVersion.name as taskName ");
            query.append(" , taskVersion.type as taskType ");
            query.append(" , processDefinitionEntity.name as processName ");
            query.append(" , case when currentFormDraftVersionEntity is null then currentFormVersion.inclusionDate else currentFormDraftVersionEntity.inclusionDate end as creationDate ");
            query.append(" , case when formType.abbreviation is null then formDraftType.abbreviation else formType.abbreviation end as type ");
            query.append(" , processDefinitionEntity.key as processType ");
            query.append(" , task.beginDate as situationBeginDate ");
            query.append(" , task.cod as taskInstanceId ");
            query.append(" , processInstance.beginDate as processBeginDate ");
            query.append(" , currentDraftEntity.editionDate as editionDate ");
            query.append(" , processInstance.cod as processInstanceId ");
            query.append(" , petition.rootPetition.id as rootPetition ");
            query.append(" , petition.parentPetition.id as parentPetition ");
            query.append(" , taskDefinition.cod as taskId");
            query.append(" , task.versionStamp as versionStamp");
            query.append(" , allocatedUser.codUsuario as codUsuarioAlocado");
            query.append(" , allocatedUser.nome as nomeUsuarioAlocado");
            query.append(" , processGroup.cod as processGroupCod");
            query.append(" , processGroup.connectionURL as processGroupContext");
            appendCustomSelectClauses(query);
        }
    }

    /**
     * Append Custom Select Clauses
     */
    protected void appendCustomSelectClauses(PetitionSearchQuery query) {
    }

    private void buildFromClause(PetitionSearchQuery query) {
        query.append(" FROM ").append(tipo.getName()).append(" petition ");
        query.append(" LEFT JOIN petition.petitioner petitioner ");
        query.append(" LEFT JOIN petition.processInstanceEntity processInstance ");
        query.append(" LEFT JOIN petition.formPetitionEntities formPetitionEntity on formPetitionEntity.mainForm = :sim ");
        query.append(" LEFT JOIN formPetitionEntity.form formEntity ");
        query.append(" LEFT JOIN formPetitionEntity.currentDraftEntity currentDraftEntity ");
        query.append(" LEFT JOIN currentDraftEntity.form formDraftEntity");
        query.append(" LEFT JOIN formDraftEntity.currentFormVersionEntity currentFormDraftVersionEntity");
        query.append(" LEFT JOIN formEntity.currentFormVersionEntity currentFormVersion ");
        query.append(" LEFT JOIN petition.processDefinitionEntity processDefinitionEntity ");
        query.append(" LEFT JOIN processDefinitionEntity.processGroup processGroup ");
        query.append(" LEFT JOIN formEntity.formType formType  ");
        query.append(" LEFT JOIN formDraftEntity.formType formDraftType  ");
        query.append(" LEFT JOIN processInstance.tasks task ");
        query.append(" LEFT JOIN task.task taskVersion ");
        query.append(" LEFT JOIN taskVersion.taskDefinition taskDefinition ");
        query.append(" LEFT JOIN task.allocatedUser allocatedUser ");
        appendCustomFromClauses(query);
    }

    /**
     * Append Custom From Clauses
     */
    protected void appendCustomFromClauses(PetitionSearchQuery query) {
    }


    private void buildWhereClause(PetitionSearchQuery query) {

        QuickFilter quickFilter = query.getQuickFilter();

        query.append(" WHERE 1=1 ");

        if (quickFilter.getIdPessoa() != null) {
            query.append(" AND petitioner.idPessoa = :idPessoa ");
            query.addParam("idPessoa", quickFilter.getIdPessoa());
        }

        query.addParam("sim", SimNao.SIM);

        if (!quickFilter.isRascunho()
                && quickFilter.getProcessesAbbreviation() != null
                && !quickFilter.getProcessesAbbreviation().isEmpty()) {
            query.append(" AND ( processDefinitionEntity.key  in (:siglasProcesso) ");
            query.addParam("siglasProcesso", quickFilter.getProcessesAbbreviation());
            if (quickFilter.getTypesNames() != null && !quickFilter.getTypesNames().isEmpty()) {
                query.append(" OR formType.abbreviation in (:formNames)) ");
                query.addParam("formNames", quickFilter.getTypesNames());
            } else {
                query.append(" ) ");
            }
        }

        appendCustomQuickFilter(query);

        if (!CollectionUtils.isEmpty(quickFilter.getTasks())) {
            query.append(" AND taskVersion.name in (:tasks)");
            query.addParam("tasks", quickFilter.getTasks());
        }

        if (quickFilter.isRascunho()) {
            query.append(" AND petition.processInstanceEntity is null ");
        } else {
            query.append(" AND petition.processInstanceEntity is not null ");
            if (quickFilter.getEndedTasks() == null) {
                query.append(" and (taskVersion.type = :tipoEnd or (taskVersion.type <> :tipoEnd and task.endDate is null)) ");
                query.addParam("tipoEnd", TaskType.END);
            } else if (Boolean.TRUE.equals(quickFilter.getEndedTasks())) {
                query.append(" and taskVersion.type = :tipoEnd ");
                query.addParam("tipoEnd", TaskType.END);
            } else {
                query.append(" and task.endDate is null ");
            }
        }

        appendCustomWhereClauses(query);
        appendSort(query);
    }

    private void appendSort(PetitionSearchQuery query) {
        QuickFilter quickFilter = query.getQuickFilter();
        if (quickFilter.getSortProperty() != null) {
            query.append(mountSort(quickFilter.getSortProperty(), quickFilter.isAscending()));
        } else if (!Boolean.TRUE.equals(query.getCount())) {
            if (quickFilter.isRascunho()) {
                query.append(mountSort("creationDate", false));
            } else {
                query.append(mountSort("processBeginDate", false));
            }
        }
    }

    /**
     * Append Custom Where Clauses
     */
    protected void appendCustomWhereClauses(PetitionSearchQuery query) {
    }

    /**
     * Append Custom Quick Filter
     */
    protected void appendCustomQuickFilter(PetitionSearchQuery query) {
        QuickFilter quickFilter = query.getQuickFilter();
        if (quickFilter.hasFilter()) {
            query.append(" AND ( upper(petition.description) like upper(:filter) ");
            query.append(" OR upper(processDefinitionEntity.name) like upper(:filter) ");
            query.append(" OR upper(taskVersion.name) like upper(:filter) ");
            if (quickFilter.isRascunho()) {
                query.append(" OR ").append(JPAQueryUtil.formattDateTimeClause("currentFormVersion.inclusionDate", "filter"));
                query.append(" OR ").append(JPAQueryUtil.formattDateTimeClause("currentDraftEntity.editionDate", "filter"));
            } else {
                query.append(" OR ").append(JPAQueryUtil.formattDateTimeClause("task.beginDate", "filter"));
                query.append(" OR ").append(JPAQueryUtil.formattDateTimeClause("processInstance.beginDate", "filter"));
            }
            query.append(" OR petition.id like :filter ) ");
            query.addParam("filter", "%" + quickFilter.getFilter() + "%");
        }
    }

    private Query createQuery(PetitionSearchQuery query) {
        buildSelectClause(query);
        buildFromClause(query);
        buildWhereClause(query);
        return setParametersQuery(getSession().createQuery(query.getHqlQueryString()), query.getParams());
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