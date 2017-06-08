package org.opensingular.server.module.provider;

import org.opensingular.flow.core.ITaskDefinition;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.persistence.requirement.RequirementSearchExtender;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.module.ActionProvider;
import org.opensingular.server.module.BoxInfo;
import org.opensingular.server.module.BoxItemDataProvider;
import org.opensingular.server.module.DefaultActionProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PetitionBoxItemDataProvider implements BoxItemDataProvider {

    private ActionProvider        actionProvider = new DefaultActionProvider();
    private List<ITaskDefinition> tasksFilter    = new ArrayList<>();

    public PetitionBoxItemDataProvider(ActionProvider actionProvider) {
        this.actionProvider = actionProvider;
    }

    public PetitionBoxItemDataProvider(ActionProvider actionProvider, ITaskDefinition... taskDefinitions) {
        this.actionProvider = actionProvider;
        if (taskDefinitions != null) {
            tasksFilter.addAll(Arrays.asList(taskDefinitions));
        }
    }

    public PetitionBoxItemDataProvider() {
    }

    @Override
    public List<Map<String, Serializable>> search(QuickFilter filter, BoxInfo boxInfo) {
        filter.forTasks(tasksFilter.stream().map(ITaskDefinition::getName).collect(Collectors.toList()).toArray(new String[0]));
        return ApplicationContextProvider.get()
                .getBean(PetitionService.class).quickSearchMap(filter, getExtenders(filter));
    }

    @Override
    public Long count(QuickFilter filter, BoxInfo boxInfo) {
        filter.forTasks(tasksFilter.stream().map(ITaskDefinition::getName).collect(Collectors.toList()).toArray(new String[0]));
        return ApplicationContextProvider.get()
                .getBean(PetitionService.class).countQuickSearch(filter, getExtenders(filter));
    }

    @Override
    public ActionProvider getActionProvider() {
        return actionProvider;
    }

    protected List<RequirementSearchExtender> getExtenders(QuickFilter filter) {
        return Collections.emptyList();
    }

}