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

import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.persistence.entity.form.RequirementDefinitionEntity;

import javax.annotation.Nonnull;

/**
 * Implementação padrão de {@link PetitionService}. Não acrescenta nenhuma funcionaldiade ao serviço.
 *
 * @author Daniel C. Bordin on 08/03/2017.
 */
public class DefaultPetitionService extends PetitionService<PetitionEntity, PetitionInstance> {

    @Override
    @Nonnull
    protected PetitionInstance newPetitionInstance(@Nonnull PetitionEntity petitionEntity) {
        return new PetitionInstance(petitionEntity);
    }

    @Override
    @Nonnull
    protected PetitionEntity newPetitionEntityFor(RequirementDefinitionEntity requirementDefinitionEntity) {
        PetitionEntity petitionEntity = new PetitionEntity();
        petitionEntity.setRequirementDefinitionEntity(requirementDefinitionEntity);
        return petitionEntity;
    }
}
