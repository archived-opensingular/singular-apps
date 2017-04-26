package org.opensingular.server.commons.service.dto;


import org.opensingular.server.commons.service.PetitionInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PetitionSendedFeedback implements Serializable {

    private PetitionInstance petition;
    private List<PetitionInstance> forkedPetitions;

    public PetitionSendedFeedback(PetitionInstance petition) {
        this.petition = petition;
    }

    public List<PetitionInstance> getForkedPetitions() {
        if (forkedPetitions == null) {
            forkedPetitions = new ArrayList<>();
        }
        return forkedPetitions;
    }

    public PetitionInstance getPetition() {
        return petition;
    }
}