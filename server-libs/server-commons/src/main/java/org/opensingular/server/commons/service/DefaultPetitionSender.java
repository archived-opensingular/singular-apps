package org.opensingular.server.commons.service;

import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.flow.core.ProcessInstance;
import org.opensingular.form.SInstance;
import org.opensingular.form.persistence.entity.FormEntity;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;

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
    public void send(PetitionInstance petition, SInstance instance, String codResponsavel) {
        final List<FormEntity> consolidatedDrafts = formPetitionService.consolidateDrafts(petition);
        final ProcessDefinition<?> processDefinition = PetitionUtil.getProcessDefinition(petition.getEntity());

        petitionService.onBeforeStartProcess(petition, instance, codResponsavel);
        ProcessInstance processInstance = petitionService.startNewProcess(petition, processDefinition);
        petitionService.onAfterStartProcess(petition, instance, codResponsavel, processInstance);

        petitionService.savePetitionHistory(petition, consolidatedDrafts);
    }

}
