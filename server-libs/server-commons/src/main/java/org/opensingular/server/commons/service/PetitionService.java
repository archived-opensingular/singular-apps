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

package org.opensingular.server.commons.service;

import org.apache.commons.collections.CollectionUtils;
import org.opensingular.flow.core.*;
import org.opensingular.flow.core.variable.type.VarTypeString;
import org.opensingular.flow.persistence.entity.*;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.form.persistence.FormKey;
import org.opensingular.form.persistence.entity.FormAnnotationEntity;
import org.opensingular.form.persistence.entity.FormEntity;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.lib.commons.base.SingularException;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.exception.PetitionConcurrentModificationException;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.flow.actions.ActionConfig;
import org.opensingular.server.commons.form.FormActions;
import org.opensingular.server.commons.persistence.dao.flow.ActorDAO;
import org.opensingular.server.commons.persistence.dao.flow.GrupoProcessoDAO;
import org.opensingular.server.commons.persistence.dao.flow.TaskInstanceDAO;
import org.opensingular.server.commons.persistence.dao.form.PetitionContentHistoryDAO;
import org.opensingular.server.commons.persistence.dao.form.PetitionDAO;
import org.opensingular.server.commons.persistence.dao.form.PetitionerDAO;
import org.opensingular.server.commons.persistence.dto.PetitionDTO;
import org.opensingular.server.commons.persistence.dto.PetitionHistoryDTO;
import org.opensingular.server.commons.persistence.dto.TaskInstanceDTO;
import org.opensingular.server.commons.persistence.entity.form.FormPetitionEntity;
import org.opensingular.server.commons.persistence.entity.form.PetitionContentHistoryEntity;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.persistence.entity.form.PetitionerEntity;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.rest.DefaultServerREST;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.spring.security.AuthorizationService;
import org.opensingular.server.commons.spring.security.PetitionAuthMetadataDTO;
import org.opensingular.server.commons.spring.security.SingularPermission;
import org.opensingular.server.commons.wicket.view.form.FormPageConfig;
import org.opensingular.server.commons.wicket.view.util.DispatcherPageUtil;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static org.opensingular.server.commons.flow.actions.DefaultActions.*;
import static org.opensingular.server.commons.flow.actions.DefaultActions.ACTION_ASSIGN;
import static org.opensingular.server.commons.flow.actions.DefaultActions.ACTION_DELETE;
import static org.opensingular.server.commons.flow.actions.DefaultActions.ACTION_EDIT;
import static org.opensingular.server.commons.flow.actions.DefaultActions.ACTION_RELOCATE;
import static org.opensingular.server.commons.flow.actions.DefaultActions.ACTION_VIEW;
import static org.opensingular.server.commons.flow.actions.DefaultActions.DELETE;
import static org.opensingular.server.commons.util.DispatcherPageParameters.FORM_NAME;

@Transactional
public class PetitionService<P extends PetitionEntity> implements Loggable {

    @Inject
    protected PetitionDAO<P> petitionDAO;

    @Inject
    protected GrupoProcessoDAO grupoProcessoDAO;

    @Inject
    protected TaskInstanceDAO taskInstanceDAO;

    @Inject
    protected PetitionerDAO petitionerDAO;

    @Inject
    protected PetitionContentHistoryDAO petitionContentHistoryDAO;

    @Inject
    protected AuthorizationService authorizationService;

    @Inject
    private FormPetitionService<P> formPetitionService;

    @Inject
    protected ActorDAO actorDAO;

    /** Retorna o serviço de formulários da petição. */
    @Nonnull
    protected  final FormPetitionService<P> getFormPetitionService() {
        return Objects.requireNonNull(formPetitionService);
    }

    /** Procura a petição com o código informado. */
    @Nonnull
    public Optional<P> findPetitionByCod(@Nonnull Long cod) {
        Objects.requireNonNull(cod);
        return petitionDAO.find(cod);
    }

    /** Recupera a petição com o código informado ou dispara Exception senão encontrar. */
    @Nonnull
    public P getPetitionByCod(@Nonnull Long cod) {
        return findPetitionByCod(cod).orElseThrow(
                () -> SingularServerException.rethrow("Não foi encontrada a petição de cod=" + cod));
    }

    /** Recupera a petição associado a código de fluxo informado ou dispara exception senão encontrar. */
    @Nonnull
    public P getPetitionByProcessCod(@Nonnull Integer cod) {
        Objects.requireNonNull(cod);
        return petitionDAO.findByProcessCod(cod);
    }

    /** Recupera a petição associado ao fluxo informado. */
    @Nonnull
    public P getPetitionByProcess(@Nonnull ProcessInstance processInstance) {
        Objects.requireNonNull(processInstance);
        return getPetitionByProcessCod(processInstance.getEntityCod());
    }

    /** Recupera a petição associada a tarefa informada. */
    @Nonnull
    public P getPetitionByTask(@Nonnull TaskInstance taskInstance) {
        Objects.requireNonNull(taskInstance);
        return getPetitionByProcess(taskInstance.getProcessInstance());
    }

    public void deletePetition(PetitionDTO peticao) {
        deletePetition(peticao.getCodPeticao());
    }

    public void deletePetition(@Nonnull Long idPeticao) {
        Optional<P> pp = petitionDAO.find(idPeticao);
        if (pp.isPresent()) {
            petitionDAO.delete(pp.get());
        }
    }

    public Long countQuickSearch(QuickFilter filter) {
        return countQuickSearch(filter, filter.getProcessesAbbreviation(), filter.getTypesNames());
    }

    public Long countQuickSearch(QuickFilter filter, List<String> siglasProcesso, List<String> formNames) {
        return petitionDAO.countQuickSearch(filter, siglasProcesso, formNames);
    }

    public List<PetitionDTO> quickSearch(QuickFilter filter, List<String> siglasProcesso, List<String> formNames) {
        return petitionDAO.quickSearch(filter, siglasProcesso, formNames);
    }

    public List<Map<String, Object>> quickSearchMap(QuickFilter filter) {
        final List<Map<String, Object>> list = petitionDAO.quickSearchMap(filter, filter.getProcessesAbbreviation(), filter.getTypesNames());
        parseResultsPetition(list);
        list.forEach(this::addLineActions);
        for (Map<String, Object> map : list) {
            authorizationService.filterActions((String) map.get("type"), (Long) map.get("codPeticao"), (List<BoxItemAction>) map.get("actions"), filter.getIdUsuarioLogado());
        }
        return list;
    }

    protected void parseResultsPetition(List<Map<String, Object>> results) {

    }

    private void addLineActions(Map<String, Object> line) {
        List<BoxItemAction> actions = new ArrayList<>();
        actions.add(createPopupBoxItemAction(line, FormActions.FORM_FILL, ACTION_EDIT.getName()));
        actions.add(createPopupBoxItemAction(line, FormActions.FORM_VIEW, ACTION_VIEW.getName()));
        actions.add(createDeleteAction(line));
        actions.add(BoxItemAction.newExecuteInstante(line.get("codPeticao"), ACTION_ASSIGN.getName()));

        appendLineActions(line, actions);

        String processKey = (String) line.get("processType");


        ActionConfig tryConfig = null;
        try {
            ProcessDefinition<?> processDefinition = Flow.getProcessDefinition(processKey);
            tryConfig = processDefinition.getMetaDataValue(ActionConfig.KEY);
        } catch (SingularException e) {

            getLogger().error(e.getMessage(), e);
        }

        final ActionConfig actionConfig = tryConfig;
        if (actionConfig != null) {
            actions = actions.stream()
                    .filter(itemAction -> actionConfig.containsAction(itemAction.getName()))
                    .collect(Collectors.toList());
        }

        line.put("actions", actions);
    }

    /**
     * Dado um linha de dados (line), permite ao serviço adicionar quais as ações possiveis associadas a essa linha em
     * particular. Esse método deve ser sobrescrito pelos serviços derivados.
     */
    protected void appendLineActions(@Nonnull Map<String, Object> line, @Nonnull List<BoxItemAction> lineActions) {
    }

    private BoxItemAction createDeleteAction(Map<String, Object> line) {
        String endpointUrl = DefaultServerREST.PATH_BOX_ACTION + DELETE + "?id=" + line.get("codPeticao");

        final BoxItemAction boxItemAction = new BoxItemAction();
        boxItemAction.setName(ACTION_DELETE.getName());
        boxItemAction.setEndpoint(endpointUrl);
        return boxItemAction;
    }

    protected final static BoxItemAction createPopupBoxItemAction(Map<String, Object> line, FormActions formAction, String actionName) {
        Object cod  = line.get("codPeticao");
        Object type = line.get("type");
        return createPopupBoxItemAction(cod, type, formAction, actionName);
    }

    private static BoxItemAction createPopupBoxItemAction(Object cod, Object type, FormActions formAction, String actionName) {
        String endpoint = DispatcherPageUtil
                .baseURL("")
                .formAction(formAction.getId())
                .petitionId(cod)
                .param(FORM_NAME, type)
                .build();

        final BoxItemAction boxItemAction = new BoxItemAction();
        boxItemAction.setName(actionName);
        boxItemAction.setEndpoint(endpoint);
        boxItemAction.setFormAction(formAction);
        return boxItemAction;
    }

    @Nonnull
    public FormKey saveOrUpdate(@Nonnull P petition, @Nonnull SInstance instance, boolean mainForm) {
        Objects.requireNonNull(petition);
        Objects.requireNonNull(instance);

        petitionDAO.saveOrUpdate(petition);

        if (petition.getPetitioner() != null) {
            petitionerDAO.saveOrUpdate(petition.getPetitioner());
        }
        return formPetitionService.saveFormPetition(petition, instance, mainForm);
    }

    public void send(P peticao, SInstance instance, String codResponsavel) {

        final List<FormEntity>      consolidatedDrafts = formPetitionService.consolidateDrafts(peticao);
        final ProcessDefinition<?>  processDefinition  = PetitionUtil.getProcessDefinition(peticao);
        final ProcessInstance       processInstance    = processDefinition.newInstance();
        final ProcessInstanceEntity processEntity      = processInstance.saveEntity();

        processInstance.setDescription(peticao.getDescription());
        peticao.setProcessInstanceEntity(processEntity);

        processInstance.start();

        onSend(peticao, instance, processEntity, codResponsavel);

        savePetitionHistory(peticao, consolidatedDrafts);
    }

    protected void onSend(P peticao, SInstance instance, ProcessInstanceEntity processEntity, String codResponsavel) {
    }

    private void savePetitionHistory(PetitionEntity petition, List<FormEntity> newEntities) {

        Optional<TaskInstanceEntity> taskInstance = findCurrentTaskByPetitionId(petition.getCod());
        FormEntity         formEntity   = petition.getMainForm();

        getLogger().info("Atualizando histórico da petição.");

        final PetitionContentHistoryEntity contentHistoryEntity = new PetitionContentHistoryEntity();

        contentHistoryEntity.setPetitionEntity(petition);

        if (taskInstance.isPresent()) {
            contentHistoryEntity.setActor(taskInstance.get().getAllocatedUser());
            contentHistoryEntity.setTaskInstanceEntity(taskInstance.get());
        }

        if (CollectionUtils.isNotEmpty(formEntity.getCurrentFormVersionEntity().getFormAnnotations())) {
            contentHistoryEntity.setFormAnnotationsVersions(formEntity.getCurrentFormVersionEntity().getFormAnnotations().stream().map(FormAnnotationEntity::getAnnotationCurrentVersion).collect(Collectors.toList()));
        }

        contentHistoryEntity.setPetitionerEntity(petition.getPetitioner());
        contentHistoryEntity.setHistoryDate(new Date());

        petitionContentHistoryDAO.saveOrUpdate(contentHistoryEntity);

        contentHistoryEntity.setFormVersionHistoryEntities(
                petition
                        .getFormPetitionEntities()
                        .stream()
                        .filter(fpe -> newEntities.contains(fpe.getForm()))
                        .map(f -> formPetitionService.createFormVersionHistory(contentHistoryEntity, f))
                        .collect(Collectors.toList())
        );
    }


    /**
     * Executa a transição informada, consolidando todos os rascunhos, este metodo não salva a petição
     *
     * @param tn           nome da transicao
     * @param petition     peticao
     * @param cfg          formConfig
     * @param onTransition listener
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void executeTransition(String tn, P petition, BiConsumer<P, String> onTransition, Map<String, String> params) {
        try {
            if (onTransition != null) {
                onTransition.accept(petition, tn);
            }

            final List<FormEntity> consolidatedDrafts = formPetitionService.consolidateDrafts(petition);

            savePetitionHistory(petition, consolidatedDrafts);

            ProcessInstance pi = PetitionUtil.getProcessInstance(petition);

            checkTaskIsEqual(petition.getProcessInstanceEntity(), pi);

            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    pi.getVariaveis().addValor(entry.getKey(), new VarTypeString(), entry.getValue());
                }
            }

            pi.executeTransition(tn);

        } catch (SingularException e) {
            throw e;
        } catch (Exception e) {
            throw SingularServerException.rethrow(e.getMessage(), e);
        }
    }

    private void checkTaskIsEqual(ProcessInstanceEntity processInstanceEntity, ProcessInstance piAtual) {
        if (!processInstanceEntity.getCurrentTask().getTask().getAbbreviation().equalsIgnoreCase(piAtual.getCurrentTaskOrException().getAbbreviation())) {
            throw new PetitionConcurrentModificationException("A instância está em uma tarefa diferente da esperada.");
        }
    }

    public List<TaskInstanceDTO> listTasks(QuickFilter filter, List<SingularPermission> permissions) {
        List<TaskInstanceDTO> tasks = taskInstanceDAO.findTasks(filter, authorizationService.filterListTaskPermissions(permissions));
        parseResultsTask(tasks);
        for (TaskInstanceDTO task : tasks) {
            checkTaskActions(task, filter);
            authorizationService.filterActions(task.getType(), task.getCodPeticao(), task.getActions(), filter.getIdUsuarioLogado(), permissions);
        }
        return tasks;
    }

    protected void parseResultsTask(List<TaskInstanceDTO> tasks) {

    }

    protected void checkTaskActions(TaskInstanceDTO task, QuickFilter filter) {
        List<BoxItemAction> actions = new ArrayList<>();
        if (task.getCodUsuarioAlocado() == null
                && task.getTaskType() == TaskType.PEOPLE) {
            actions.add(BoxItemAction.newExecuteInstante(task.getCodPeticao(), ACTION_ASSIGN.getName()));
        }

        if (task.getTaskType() == TaskType.PEOPLE) {
            actions.add(BoxItemAction.newExecuteInstante(task.getCodPeticao(), ACTION_RELOCATE.getName()));
        }

        if (filter.getIdUsuarioLogado().equalsIgnoreCase(task.getCodUsuarioAlocado())) {
            actions.add(createPopupBoxItemAction(task.getCodPeticao(), task.getType(), FormActions.FORM_ANALYSIS, ACTION_ANALYSE.getName()));
        }

        actions.add(createPopupBoxItemAction(task.getCodPeticao(), task.getType(), FormActions.FORM_VIEW, ACTION_VIEW.getName()));

        appendTaskActions(task, actions);

        String                     processKey        = task.getProcessType();
        final ProcessDefinition<?> processDefinition = Flow.getProcessDefinition(processKey);
        final ActionConfig         actionConfig      = processDefinition.getMetaDataValue(ActionConfig.KEY);
        if (actionConfig != null) {
            actions = actions.stream()
                    .filter(itemAction -> actionConfig.containsAction(itemAction.getName()))
                    .collect(Collectors.toList());
        }

        task.setActions(actions);
    }

    protected void appendTaskActions(TaskInstanceDTO task, List<BoxItemAction> actions) {

    }

    public Long countTasks(QuickFilter filter, List<SingularPermission> permissions) {
        return taskInstanceDAO.countTasks(filter.getProcessesAbbreviation(), authorizationService.filterListTaskPermissions(permissions), filter.getFilter(), filter.getEndedTasks());
    }

    public List<? extends TaskInstanceDTO> listTasks(int first, int count, String sortProperty, boolean ascending, String siglaFluxo, List<SingularPermission> permissions, String filtroRapido, boolean concluidas) {

        return taskInstanceDAO.findTasks(first, count, sortProperty, ascending, siglaFluxo, authorizationService.filterListTaskPermissions(permissions), filtroRapido, concluidas);
    }


    public Long countTasks(String siglaFluxo, List<SingularPermission> permissions, String filtroRapido, boolean concluidas) {
        return taskInstanceDAO.countTasks(Collections.singletonList(siglaFluxo), authorizationService.filterListTaskPermissions(permissions), filtroRapido, concluidas);
    }

    public List<MTransition> listCurrentTaskTransitions(Long petitionId) {
        return findCurrentTaskByPetitionId(petitionId)
                .map(Flow::getTaskInstance)
                .flatMap(TaskInstance::getFlowTask)
                .map(MTask::getTransitions)
                .orElse(Collections.emptyList());
    }

    @Nonnull
    public Optional<TaskInstanceEntity> findCurrentTaskByPetitionId(@Nonnull Long petitionId) {
        //TODO (Daniel) Por que usar essa entidade em vez de TaskIntnstace ?
        Objects.requireNonNull(petitionId);
        List<TaskInstanceEntity> taskInstances = taskInstanceDAO.findCurrentTasksByPetitionId(petitionId);
        if (taskInstances.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(taskInstances.get(0));
    }

    public List<ProcessGroupEntity> listAllProcessGroups() {
        return grupoProcessoDAO.listarTodosGruposProcesso();
    }

    public ProcessGroupEntity findByProcessGroupName(String name) {
        return grupoProcessoDAO.findByName(name);
    }

    public ProcessGroupEntity findByProcessGroupCod(String cod) {
        return grupoProcessoDAO.get(cod).orElse(null);
    }

    public P createNewPetitionWithoutSave(Class<P> petitionClass, FormPageConfig config, BiConsumer<P, FormPageConfig> creationListener) {

        final P petition;

        try {
            petition = petitionClass.newInstance();
        } catch (Exception e) {
            throw SingularServerException.rethrow("Error creating new petition instance", e);
        }

        if (config.containsProcessDefinition()) {
            petition.setProcessDefinitionEntity((ProcessDefinitionEntity) Flow.getProcessDefinition(config.getProcessDefinition()).getEntityProcessDefinition());
        }

        if (creationListener != null) {
            creationListener.accept(petition, config);
        }

        return petition;
    }

    public ProcessDefinitionEntity findEntityProcessDefinitionByClass(Class<? extends ProcessDefinition> clazz) {
        return (ProcessDefinitionEntity) Flow.getProcessDefinition(clazz).getEntityProcessDefinition();
    }

    public List<PetitionHistoryDTO> listPetitionContentHistoryByPetitionCod(long petitionCod, String menu, boolean filter) {
        P petition = petitionDAO.findOrException(petitionCod);
        return petitionContentHistoryDAO.listPetitionContentHistoryByPetitionCod(petition, menu, filter);
    }

    public List<Actor> listAllocableUsers(Map<String, Object> selectedTask) {
        Integer taskInstanceId = (Integer) selectedTask.get("taskInstanceId");
        return actorDAO.listAllocableUsers(taskInstanceId);
    }

    public PetitionerEntity findPetitionerByExternalId(String externalId) {
        return petitionerDAO.findPetitionerByExternalId(externalId);
    }

    @Nonnull
    public boolean isPreviousTransition(@Nonnull  P petition, @Nonnull String trasitionName) {
        return isPreviousTransition(petition.getCod(), trasitionName);
    }

    public boolean isPreviousTransition(@Nonnull Long petitionCod, @Nonnull String trasitionName) {
        Optional<TaskInstanceEntity> currentTask = findCurrentTaskByPetitionId(petitionCod);
        if (currentTask.isPresent()) {
            List<TaskInstanceEntity> tasks = currentTask.get().getProcessInstance().getTasks();
            String name = tasks.get(tasks.indexOf(currentTask) - 1).getExecutedTransition().getName();
            return Objects.equals(name, trasitionName);
        }
        return false;
    }

    public PetitionAuthMetadataDTO findPetitionAuthMetadata(Long petitionId) {
        return petitionDAO.findPetitionAuthMetadata(petitionId);
    }

    public List<FormVersionEntity> buscarDuasUltimasVersoesForm(@Nonnull Long codPetition) {
        PetitionEntity petitionEntity = petitionDAO.findOrException(codPetition);
        FormEntity     mainForm       = petitionEntity.getMainForm();
        return formPetitionService.findTwoLastFormVersions(mainForm.getCod());
    }

    /** Procura a instância de processo (fluxo) associado ao formulário se o mesmo existir. */
    public Optional<ProcessInstanceEntity> getFormProcessInstanceEntity(@Nonnull SInstance instance) {
        return getFormPetitionService().findFormEntity(instance)
                .map(formEntity -> petitionDAO.findByFormEntity(formEntity))
                .map(PetitionEntity::getProcessInstanceEntity);
    }

    /** Verifica se o formulário já foi persistido e possui um processo (fluxo) instanciado e associado. */
    public boolean formHasProcessInstance(SInstance instance) {
        return getFormProcessInstanceEntity(instance).isPresent();
    }

    /** Recupera o formulário {@link SInstance} de abertura do requerimento. */
    @Nonnull
    public SIComposite getMainFormAsInstance(@Nonnull PetitionEntity petition) {
        Objects.requireNonNull(petition);
        return (SIComposite) getFormPetitionService().getSInstance(petition.getMainForm());
    }

    /** Recupera o formulário {@link SInstance} de abertura do requerimento e garante que é do tipo inforado. */
    @Nonnull
    public <I extends SInstance, K extends SType<? extends I>> I getMainFormAsInstance(@Nonnull PetitionEntity petition,
            @Nonnull Class<K> expectedType) {
        Objects.requireNonNull(petition);
        return getFormPetitionService().getSInstance(petition.getMainForm(), expectedType);
    }

    /** Procura na petição a versão mais recente do formulário do tipo informado. */
    @Nonnull
    protected final Optional<FormPetitionEntity> findLastFormPetitionEntityByType(@Nonnull PetitionEntity petition,
            @Nonnull Class<? extends SType<?>> typeClass) {
        return getFormPetitionService().findLastFormPetitionEntityByType(petition.getCod(), typeClass);
    }

    /** Procura na petição a versão mais recente do formulário do tipo informado. */
    @Nonnull
    protected final Optional<SInstance> findLastFormPetitionInstanceByType(@Nonnull PetitionEntity petition,
            @Nonnull Class<? extends SType<?>> typeClass) {
        return getFormPetitionService().findLastFormPetitionInstanceByType(petition.getCod(), typeClass);
    }
}