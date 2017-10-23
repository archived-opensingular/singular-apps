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

import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.form.SInstance;
import org.opensingular.form.persistence.entity.FormEntity;
import org.opensingular.server.commons.persistence.entity.form.RequirementEntity;
import org.opensingular.server.commons.service.dto.RequirementSenderFeedback;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class DefaultRequirementSender implements RequirementSender {

    @Inject
    private RequirementService<RequirementEntity, RequirementInstance> requirementService;

    @Inject
    private FormRequirementService<RequirementEntity> formRequirementService;

    @Override
    public RequirementSenderFeedback send(RequirementInstance requirement, SInstance instance, String codResponsavel) {
        final List<FormEntity> consolidatedDrafts = formRequirementService.consolidateDrafts(requirement);
        final FlowDefinition<?> flowDefinition = RequirementUtil.getFlowDefinition(requirement.getEntity());

        requirementService.onBeforeStartFlow(requirement, instance, codResponsavel);
        FlowInstance flowInstance = requirementService.startNewFlow(requirement, flowDefinition, codResponsavel);
        requirementService.onAfterStartFlow(requirement, instance, codResponsavel, flowInstance);

        requirementService.saveRequirementHistory(requirement, consolidatedDrafts);

        return new RequirementSenderFeedback(requirement);
    }

}
