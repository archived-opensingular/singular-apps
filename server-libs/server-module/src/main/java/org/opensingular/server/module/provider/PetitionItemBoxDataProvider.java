package org.opensingular.server.module.provider;

import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.module.ActionProvider;
import org.opensingular.server.module.BoxInfo;
import org.opensingular.server.module.DefaultActionProvider;
import org.opensingular.server.module.ItemBoxDataProvider;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
public class PetitionItemBoxDataProvider implements ItemBoxDataProvider {

    @Inject
    private PetitionService<?, ?> petitionService;

    @Override
    public List<Map<String, Serializable>> search(QuickFilter filter, BoxInfo boxInfo) {
        return petitionService.quickSearchMap(filter);
    }

    @Override
    public Long count(QuickFilter filter, BoxInfo boxInfo) {
        return petitionService.countQuickSearch(filter);
    }

    @Override
    public ActionProvider getActionProvider() {
        return new DefaultActionProvider();
    }


}