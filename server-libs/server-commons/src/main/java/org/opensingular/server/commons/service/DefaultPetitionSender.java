package org.opensingular.server.commons.service;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.flow.core.ProcessInstance;
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
        final ProcessDefinition<?> processDefinition = PetitionUtil.getProcessDefinition(petition.getEntity());

        petitionService.savePetitionHistory(petition, consolidatedDrafts);
        petitionService.onBeforeStartProcess(petition, instance, codResponsavel);
        ProcessInstance processInstance = petitionService.startNewProcess(petition, processDefinition);
        petitionService.onAfterStartProcess(petition, instance, codResponsavel, processInstance);


        return new PetitionSendedFeedback(petition);
    }

}
