package org.opensingular.server.commons.box.decorator;

import org.opensingular.server.commons.box.chain.ItemBoxDataDecoratorChain;
import org.opensingular.server.commons.jackson.SingularObjectMapper;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.PetitionService;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Map;


public class TaskActionAppenderItemBoxDataDecorator implements ItemBoxDataDecorator {

    private final PetitionService<?, ?> petitionService;

    @Inject
    public TaskActionAppenderItemBoxDataDecorator(PetitionService<?, ?> petitionService) {
        this.petitionService = petitionService;
    }

    @Override
    public void decorate(Map<String, Serializable> line, QuickFilter filter, ItemBoxDataDecoratorChain chain) {
        petitionService.checkTaskActions(line, filter);
        chain.decorate(line, filter);
    }

}
