package org.opensingular.server.module.provider;

import org.opensingular.form.SType;
import org.opensingular.server.commons.box.factory.ItemBoxActionList;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.box.ItemBoxData;
import org.opensingular.server.module.ItemBoxDataProvider;
import org.opensingular.server.module.SingularModuleConfiguration;
import org.opensingular.server.module.SingularRequirementRef;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
public class PetitionItemBoxDataProvider implements ItemBoxDataProvider {

    @Inject
    private PetitionService<?, ?> petitionService;

    @Inject
    private SingularModuleConfiguration moduleConfiguration;


    @Override
    public List<Map<String, Serializable>> search(QuickFilter filter) {
        return petitionService.quickSearchMap(filter);
    }

    @Override
    public Long count(QuickFilter filter) {
        return petitionService.countQuickSearch(filter);
    }

    @Override
    public ItemBoxActionList getLineActions(ItemBoxData line, QuickFilter filter) {
        return petitionService.getLineActions(line);
    }

}