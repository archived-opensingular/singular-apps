package org.opensingular.server.commons.box.filter;

import org.opensingular.server.commons.box.ItemBoxData;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.PetitionService;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class PetitionActionAppenderItemBoxDataFilter implements ItemBoxDataFilter {

    private final PetitionService<?, ?> petitionService;

    @Inject
    public PetitionActionAppenderItemBoxDataFilter(PetitionService<?, ?> petitionService) {
        this.petitionService = petitionService;
    }

    @Override
    public void doFilter(ItemBoxData itemBoxData, QuickFilter filter) {
        petitionService.addLineActions(itemBoxData);
    }

}