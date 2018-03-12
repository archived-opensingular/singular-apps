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
import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.flow.core.SUser;
import org.opensingular.flow.core.TaskInstance;
import org.opensingular.flow.core.entity.IEntityTaskDefinition;
import org.opensingular.flow.core.entity.IEntityTaskInstance;
import org.opensingular.flow.core.entity.IEntityTaskVersion;
import org.opensingular.flow.persistence.entity.FlowInstanceEntity;
import org.opensingular.flow.persistence.entity.TaskDefinitionEntity;
import org.opensingular.form.SFormUtil;
import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.form.persistence.entity.FormEntity;
import org.opensingular.form.persistence.entity.FormTypeEntity;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.exception.RequirementWithoutDefinitionException;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.persistence.entity.form.DraftEntity;
import org.opensingular.server.commons.persistence.entity.form.FormRequirementEntity;
import org.opensingular.server.commons.persistence.entity.form.RequirementEntity;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

/**
 * Métodos utilitários para manipulação de requerimentos.
 *
 * @author Daniel C. Bordin on 01/03/2017.
 */
public final class RequirementUtil {

    private RequirementUtil() {}

    /** Recupera a definição de fluxo associado a petição. */
    @Nonnull
    public static FlowDefinition<?> getFlowDefinition(@Nonnull RequirementEntity requirement) {
        return getFlowDefinitionOpt(requirement).orElseThrow(
                () -> new RequirementWithoutDefinitionException().add(requirement));
    }

    /** Recupera a definição de fluxo associado a petição. */
    @Nonnull
    final static Optional<FlowDefinition<?>> getFlowDefinitionOpt(@Nonnull RequirementEntity requirement) {
        Objects.requireNonNull(requirement);
        if (requirement.getFlowDefinitionEntity() == null) {
            return Optional.empty();
        }
        return Optional.of(Flow.getFlowDefinition(requirement.getFlowDefinitionEntity().getKey()));
    }

    /** Retorna a tarefa atual associada a petição ou dispara exception senão houver nenhuma. */
    @Nonnull
    public static IEntityTaskDefinition getCurrentTaskDefinition(@Nonnull RequirementEntity requirement) {
        return getCurrentTaskDefinitionOpt(requirement).orElseThrow(
                () -> SingularServerException.rethrow("Não há uma tarefa corrente associada à petição."));
    }

    /** Retorna a tarefa atual associada a petição se existir. */
    @Nonnull
    public static Optional<IEntityTaskDefinition> getCurrentTaskDefinitionOpt(@Nonnull RequirementEntity requirement) {
        final FlowInstanceEntity flowInstanceEntity = requirement.getFlowInstanceEntity();
        if (flowInstanceEntity != null) {
            return flowInstanceEntity.getCurrentTask().map(IEntityTaskInstance::getTaskVersion).map(IEntityTaskVersion::getTaskDefinition);
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
        return Optional.of(instance.getDocument().lookupLocalServiceOrException(ServerSInstanceFlowAwareService.class))
                .map(ServerSInstanceFlowAwareService::getFlowInstance).flatMap(FlowInstance::getCurrentTask);
    }

    /** Recupera a instância de fluxo associada à petição informada. */
    @Nonnull
    public static FlowInstance getFlowInstance(@Nonnull RequirementEntity requirement) {
        Objects.requireNonNull(requirement);
        return Flow.getFlowInstance(requirement.getFlowInstanceEntity());
    }

    /** Resolve o id de usuário. */
    @Nonnull
    public static Optional<SUser> findUser(@Nonnull String idUsuario) {
        Objects.requireNonNull(idUsuario);
        return Flow.getConfigBean().getUserService().saveUserIfNeeded(idUsuario);
    }

    /** Resolve o id de usuário ou dispara exception senão encontrar o usuário. */
    @Nonnull
    public static SUser findUserOrException(@Nonnull String idUsuario) {
        return findUser(idUsuario).orElseThrow(
                () -> SingularServerException.rethrow("Não foi encontrado o usuário").add("idUsuario", idUsuario));
    }


    @Nonnull
    public static RequirementService<?,?> getRequirementService() {
        return ApplicationContextProvider.get().getBean(RequirementService.class);
    }

    /** Retorna o nome do tipo associado a essa entidade. */
    @Nonnull
    public static String getTypeName(@Nonnull RequirementInstance requirement) {
        return getTypeName(requirement.getEntity().getMainForm());
    }

    /** Retorna o nome do tipo associado a essa entidade. */
    @Nonnull
    public static String getTypeName(@Nonnull RequirementEntity requirement) {
        return getTypeName(requirement.getMainForm());
    }

    /** Retorna o nome do tipo associado a essa entidade. */
    @Nonnull
    public static String getTypeName(@Nonnull DraftEntity draftEntity) {
        return getTypeName(draftEntity.getForm());
    }

    /** Retorna o nome do tipo associado a essa entidade. */
    @Nonnull
    public static String getTypeName(@Nonnull FormRequirementEntity formRequirementEntity) {
        return getTypeName(formRequirementEntity.getForm());
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
