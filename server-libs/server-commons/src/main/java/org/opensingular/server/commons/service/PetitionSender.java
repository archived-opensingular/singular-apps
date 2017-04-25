package org.opensingular.server.commons.service;

import org.opensingular.form.SInstance;
import org.opensingular.server.commons.service.dto.PetitionSendedFeedback;

public interface PetitionSender {

    PetitionSendedFeedback send(PetitionInstance petition, SInstance instance, String codResponsavel);

}