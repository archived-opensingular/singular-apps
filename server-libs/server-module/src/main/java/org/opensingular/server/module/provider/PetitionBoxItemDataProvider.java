package org.opensingular.server.module.provider;

import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.module.ActionProvider;
import org.opensingular.server.module.BoxInfo;
import org.opensingular.server.module.BoxItemDataProvider;
import org.opensingular.server.module.DefaultActionProvider;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PetitionBoxItemDataProvider implements BoxItemDataProvider {

    private ActionProvider actionProvider = new DefaultActionProvider();

    public PetitionBoxItemDataProvider(ActionProvider actionProvider) {
        this.actionProvider = actionProvider;
    }

    public PetitionBoxItemDataProvider() {
    }

    @Override
    public List<Map<String, Serializable>> search(QuickFilter filter, BoxInfo boxInfo) {
        return ApplicationContextProvider.get().getBean(PetitionService.class).quickSearchMap(filter);
    }

    @Override
    public Long count(QuickFilter filter, BoxInfo boxInfo) {
        return ApplicationContextProvider.get().getBean(PetitionService.class).countQuickSearch(filter);
    }

    @Override
    public ActionProvider getActionProvider() {
        return actionProvider;
    }


}