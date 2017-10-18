/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.server.commons.service;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.form.SInstance;
import org.opensingular.form.persistence.entity.FormEntity;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.service.dto.PetitionSendedFeedback;

@Transactional
public class DefaultPetitionSender implements PetitionSender {

    @Inject
    private PetitionService<PetitionEntity, PetitionInstance> petitionService;

    @Inject
    private FormPetitionService<PetitionEntity> formPetitionService;

    @Override
    public PetitionSendedFeedback send(PetitionInstance petition, SInstance instance, String codResponsavel) {
        final List<FormEntity> consolidatedDrafts = formPetitionService.consolidateDrafts(petition);
        final FlowDefinition<?> flowDefinition = PetitionUtil.getProcessDefinition(petition.getEntity());

        petitionService.onBeforeStartProcess(petition, instance, codResponsavel);
        FlowInstance flowInstance = petitionService.startNewProcess(petition, flowDefinition, codResponsavel);
        petitionService.onAfterStartProcess(petition, instance, codResponsavel, flowInstance);

        petitionService.savePetitionHistory(petition, consolidatedDrafts);

        return new PetitionSendedFeedback(petition);
    }

}
