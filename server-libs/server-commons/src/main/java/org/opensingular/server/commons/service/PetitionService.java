package org.opensingular.server.commons.service;

import org.opensingular.flow.core.MTransition;
import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.flow.core.ProcessInstance;
import org.opensingular.flow.core.TaskInstance;
import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.flow.persistence.entity.ProcessDefinitionEntity;
import org.opensingular.flow.persistence.entity.ProcessGroupEntity;
import org.opensingular.flow.persistence.entity.ProcessInstanceEntity;
import org.opensingular.flow.persistence.entity.TaskInstanceEntity;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.form.persistence.FormKey;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.persistence.dto.PetitionDTO;
import org.opensingular.server.commons.persistence.dto.PetitionHistoryDTO;
import org.opensingular.server.commons.persistence.dto.TaskInstanceDTO;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.persistence.entity.form.PetitionerEntity;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.spring.security.PetitionAuthMetadataDTO;
import org.opensingular.server.commons.spring.security.SingularPermission;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by Danilo on 14/03/2017.
 */
public interface PetitionService<PE extends PetitionEntity, PI extends PetitionInstance> extends Loggable {
    @Nonnull
    PI getPetitionInstance(@Nonnull ProcessInstance processInstance);

    @Nonnull
    PI getPetitionInstance(@Nonnull TaskInstance taskInstance);

    @Nonnull
    Optional<PI> findPetition(@Nonnull Long cod);

    @Nonnull
    @Deprecated
    PE getPetitionByCod(@Nonnull Long cod);

    @Nonnull
    PI getPetition(@Nonnull Long cod);

    @Nonnull
    @Deprecated
    PE getPetitionByProcessCod(@Nonnull Integer cod);

    @Nonnull
    PI getPetition(@Nonnull ProcessInstance processInstance);

    @Nonnull
    PI getPetition(@Nonnull TaskInstance taskInstance);

    void deletePetition(PetitionDTO peticao);

    void deletePetition(@Nonnull Long idPeticao);

    Long countQuickSearch(QuickFilter filter);

    Long countQuickSearch(QuickFilter filter, List<String> siglasProcesso, List<String> formNames);

    List<PetitionDTO> quickSearch(QuickFilter filter, List<String> siglasProcesso, List<String> formNames);

    List<Map<String, Serializable>> quickSearchMap(QuickFilter filter);

    @Nonnull
    FormKey saveOrUpdate(@Nonnull PI petition, @Nonnull SInstance instance, boolean mainForm);

    void send(PI petition, SInstance instance, String codResponsavel);

    @SuppressWarnings({"unchecked", "rawtypes"})
    void executeTransition(String tn, PI petition, BiConsumer<PI, String> onTransition, Map<String, String> params);

    List<TaskInstanceDTO> listTasks(QuickFilter filter, List<SingularPermission> permissions);

    Long countTasks(QuickFilter filter, List<SingularPermission> permissions);

    List<? extends TaskInstanceDTO> listTasks(int first, int count, String sortProperty, boolean ascending, String siglaFluxo, List<SingularPermission> permissions, String filtroRapido, boolean concluidas);

    Long countTasks(String siglaFluxo, List<SingularPermission> permissions, String filtroRapido, boolean concluidas);

    List<MTransition> listCurrentTaskTransitions(Long petitionId);

    @Nonnull
    Optional<TaskInstanceEntity> findCurrentTaskByPetitionId(@Nonnull Long petitionId);

    List<ProcessGroupEntity> listAllProcessGroups();

    ProcessGroupEntity findByProcessGroupName(String name);

    ProcessGroupEntity findByProcessGroupCod(String cod);

    @Nonnull
    PI createNewPetitionWithoutSave(@Nullable Class<? extends ProcessDefinition> classProcess, @Nullable PI parentPetition,
                                    @Nullable Consumer<PI> creationListener);

    ProcessDefinitionEntity findEntityProcessDefinitionByClass(Class<? extends ProcessDefinition> clazz);

    List<PetitionHistoryDTO> listPetitionContentHistoryByPetitionCod(long petitionCod, String menu, boolean filter);

    List<Actor> listAllocableUsers(Map<String, Object> selectedTask);

    PetitionerEntity findPetitionerByExternalId(String externalId);

    @Nonnull
    boolean isPreviousTransition(@Nonnull PI petition, @Nonnull String trasitionName);

    boolean isPreviousTransition(@Nonnull Long petitionCod, @Nonnull String trasitionName);

    PetitionAuthMetadataDTO findPetitionAuthMetadata(Long petitionId);

    List<FormVersionEntity> buscarDuasUltimasVersoesForm(@Nonnull Long codPetition);

    Optional<ProcessInstanceEntity> getFormProcessInstanceEntity(@Nonnull SInstance instance);

    boolean formHasProcessInstance(SInstance instance);

    @Nonnull
    SIComposite getMainFormAsInstance(@Nonnull PetitionEntity petition);

    @Nonnull
    <I extends SInstance, K extends SType<? extends I>> I getMainFormAsInstance(@Nonnull PetitionEntity petition,
                                                                                @Nonnull Class<K> expectedType);
}
