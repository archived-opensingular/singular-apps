package org.opensingular.server.commons.box.decorator;

import org.opensingular.server.commons.box.chain.ItemBoxDataDecoratorChain;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.PetitionService;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Map;

public class PetitionActionAppenderItemBoxDataDecorator implements ItemBoxDataDecorator {

    private final PetitionService<?, ?> petitionService;

    @Inject
    public PetitionActionAppenderItemBoxDataDecorator(PetitionService<?, ?> petitionService) {
        this.petitionService = petitionService;
    }

    @Override
    public void decorate(Map<String, Serializable> line, QuickFilter filter, ItemBoxDataDecoratorChain chain) {
        petitionService.addLineActions(line);
        chain.decorate(line, filter);
    }
}
