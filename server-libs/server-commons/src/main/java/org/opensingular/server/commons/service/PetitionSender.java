package org.opensingular.server.commons.service;

import org.opensingular.form.SInstance;

public interface PetitionSender {

    void send(PetitionInstance petition, SInstance instance, String codResponsavel);

}