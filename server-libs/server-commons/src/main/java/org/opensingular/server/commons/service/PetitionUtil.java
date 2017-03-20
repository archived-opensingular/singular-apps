/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
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

import org.opensingular.flow.core.Flow;
import org.opensingular.flow.core.MUser;
import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.flow.core.ProcessInstance;
import org.opensingular.flow.core.TaskInstance;
import org.opensingular.flow.persistence.entity.ProcessInstanceEntity;
import org.opensingular.flow.persistence.entity.TaskDefinitionEntity;
import org.opensingular.form.SFormUtil;
import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.form.persistence.entity.FormEntity;
import org.opensingular.form.persistence.entity.FormTypeEntity;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.server.commons.exception.PetitionWithoutDefinitionException;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.persistence.entity.form.DraftEntity;
import org.opensingular.server.commons.persistence.entity.form.FormPetitionEntity;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

/**
 * Métodos utilitários para manipulação de requerimentos.
 *
 * @author Daniel C. Bordin on 01/03/2017.
 */
public final class PetitionUtil {

    private PetitionUtil() {}

    /** Recupera a definição de processo associado a petição. */
    @Nonnull
    public static ProcessDefinition<?> getProcessDefinition(@Nonnull PetitionEntity petition) {
        return getProcessDefinitionOpt(petition).orElseThrow(() -> new PetitionWithoutDefinitionException());
    }

    /** Recupera a definição de processo associado a petição. */
    @Nonnull
    final static Optional<ProcessDefinition<?>> getProcessDefinitionOpt(@Nonnull PetitionEntity petition) {
        Objects.requireNonNull(petition);
        if (petition.getProcessDefinitionEntity() == null) {
            return Optional.empty();
        }
        return Optional.of(Flow.getProcessDefinition(petition.getProcessDefinitionEntity().getKey()));
    }

    /** Retorna a tarefa atual associada a petição ou dispara exception senão houver nenhuma. */
    @Nonnull
    public static TaskDefinitionEntity getCurrentTaskDefinition(@Nonnull PetitionEntity petition) {
        return getCurrentTaskDefinitionOpt(petition).orElseThrow(
                () -> SingularServerException.rethrow("Não há uma tarefa corrente associada à petição."));
    }

    /** Retorna a tarefa atual associada a petição se existir. */
    @Nonnull
    public static Optional<TaskDefinitionEntity> getCurrentTaskDefinitionOpt(@Nonnull PetitionEntity petition) {
        final ProcessInstanceEntity processInstanceEntity = petition.getProcessInstanceEntity();
        if (processInstanceEntity != null) {
            return Optional.of(processInstanceEntity.getCurrentTask().getTaskVersion().getTaskDefinition());
        }
        return Optional.empty();
    }

    /**
     * If instance have a Task Associated with it, returns it.
     *
     * @param x Instance where document contains task instance
     * @return Task if exists
     */
    @Nonnull
    public static Optional<TaskInstance> getCurrentTaskEntity(@Nonnull SInstance instance) {
        return Optional.of(instance.getDocument().lookupServiceOrException(ServerSIntanceProcessAwareService.class))
                .map(ServerSIntanceProcessAwareService::getProcessInstance).flatMap(ProcessInstance::getCurrentTask);
    }

    /** Recupera a instância de processo associada à petição informada. */
    @Nonnull
    public static ProcessInstance getProcessInstance(@Nonnull PetitionEntity petition) {
        Objects.requireNonNull(petition);
        return Flow.getProcessInstance(petition.getProcessInstanceEntity());
    }

    /** Resolve o id de usuário. */
    @Nonnull
    public static Optional<MUser> findUser(@Nonnull String idUsuario) {
        Objects.requireNonNull(idUsuario);
        return Flow.getConfigBean().getUserService().saveUserIfNeeded(idUsuario);
    }

    /** Resolve o id de usuário ou dispara exception senão encontrar o usuário. */
    @Nonnull
    public static MUser findUserOrException(@Nonnull String idUsuario) {
        return findUser(idUsuario).orElseThrow(
                () -> SingularServerException.rethrow("Não foi encontrado o usuário").add("idUsuario", idUsuario));
    }


    @Nonnull
    public static PetitionService<?,?> getPetitionServiceOrException(@Nonnull SInstance instance) {
        return instance.getDocument().lookupServiceOrException(PetitionService.class);
    }

    /** Retorna o nome do tipo associado a essa entidade. */
    @Nonnull
    public static String getTypeName(@Nonnull PetitionInstance petition) {
        return getTypeName(petition.getEntity().getMainForm());
    }

    /** Retorna o nome do tipo associado a essa entidade. */
    @Nonnull
    public static String getTypeName(@Nonnull PetitionEntity petition) {
        return getTypeName(petition.getMainForm());
    }

    /** Retorna o nome do tipo associado a essa entidade. */
    @Nonnull
    public static String getTypeName(@Nonnull DraftEntity draftEntity) {
        return getTypeName(draftEntity.getForm());
    }

    /** Retorna o nome do tipo associado a essa entidade. */
    @Nonnull
    public static String getTypeName(@Nonnull FormPetitionEntity formPetitionEntity) {
        return getTypeName(formPetitionEntity.getForm());
    }

    /** Retorna o nome do tipo associado a essa entidade. */
    @Nonnull
    public static String getTypeName(@Nonnull FormVersionEntity formVersionEntity) {
        return getTypeName(formVersionEntity.getFormEntity());
    }

    /** Retorna o nome do tipo associado a essa entidade. */
    @Nonnull
    public static String getTypeName(@Nonnull FormEntity form) {
        return getTypeName(form.getFormType());
    }

    /** Retorna o nome do tipo associado a essa entidade. */
    @Nonnull
    public static String getTypeName(@Nonnull FormTypeEntity ft) {
        return ft.getAbbreviation();
    }

    /** Retorna o nome completo do tipo definido pela classe. Veja {@link SFormUtil#getTypeName(Class)}. */
    @Nonnull
    public static String getTypeName(@Nonnull Class<? extends SType<?>> typeClass) {
        return SFormUtil.getTypeName(typeClass);
    }
}
