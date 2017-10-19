package org.opensingular.server.commons.service;

import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.form.SInstance;
import org.opensingular.form.persistence.entity.FormEntity;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.service.dto.PetitionSendedFeedback;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class DefaultPetitionSender implements PetitionSender {

    @Inject
    private PetitionService<PetitionEntity, PetitionInstance> petitionService;

    @Inject
    private FormPetitionService<PetitionEntity> formPetitionService;

    @Override
    public PetitionSendedFeedback send(PetitionInstance petition, SInstance instance, String codResponsavel) {
        final List<FormEntity> consolidatedDrafts = formPetitionService.consolidateDrafts(petition);
        final FlowDefinition<?> flowDefinition = PetitionUtil.getFlowDefinition(petition.getEntity());

        petitionService.onBeforeStartProcess(petition, instance, codResponsavel);
        FlowInstance flowInstance = petitionService.startNewProcess(petition, flowDefinition, codResponsavel);
        petitionService.onAfterStartProcess(petition, instance, codResponsavel, flowInstance);

        petitionService.savePetitionHistory(petition, consolidatedDrafts);

        return new PetitionSendedFeedback(petition);
    }

}
