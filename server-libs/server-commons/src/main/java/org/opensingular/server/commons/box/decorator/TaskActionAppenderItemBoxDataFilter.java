package org.opensingular.server.commons.box.decorator;

import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.PetitionService;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Map;

@Named
public class TaskActionAppenderItemBoxDataFilter implements ItemBoxDataFilter {

    private final PetitionService<?, ?> petitionService;

    @Inject
    public TaskActionAppenderItemBoxDataFilter(PetitionService<?, ?> petitionService) {
        this.petitionService = petitionService;
    }

    @Override
    public void doFilter(Map<String, Serializable> line, QuickFilter filter) {
        if (line.get("taskInstanceId") != null) {//TODO virar metodo isTask danilo
            petitionService.checkTaskActions(line, filter);
        }
    }

}