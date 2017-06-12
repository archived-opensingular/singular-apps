package org.opensingular.server.module.provider;

import org.opensingular.lib.commons.lambda.IConsumer;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.persistence.requirement.RequirementSearchExtender;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.spring.security.PermissionResolverService;
import org.opensingular.server.commons.spring.security.SingularPermission;
import org.opensingular.server.module.ActionProvider;
import org.opensingular.server.module.BoxInfo;
import org.opensingular.server.module.BoxItemDataProvider;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequirementBoxItemDataProvider implements BoxItemDataProvider {

    @Nonnull
    private final ActionProvider actionProvider;

    @Nonnull
    private final Boolean evalPermissions;

    private List<String> tasks;

    private List<RequirementSearchExtender> extenders;

    private List<IConsumer<List<Map<String, Serializable>>>> filters;

    public RequirementBoxItemDataProvider(@Nonnull ActionProvider actionProvider, @Nonnull Boolean evalPermissions) {
        this.actionProvider = actionProvider;
        this.evalPermissions = evalPermissions;
    }

    @Override
    public List<Map<String, Serializable>> search(QuickFilter filter, BoxInfo boxInfo) {
        addEnabledTasksToFilter(filter);
        List<Map<String, Serializable>> requirements;
        if (Boolean.TRUE.equals(evalPermissions)) {
            requirements = lookupPetitionService().listTasks(filter, searchPermissions(filter), extenders);
        } else {
            requirements = lookupPetitionService().quickSearchMap(filter, extenders);
        }
        if (filters != null) {
            filters.forEach(x -> x.accept(requirements));
        }
        return requirements;
    }

    @Override
    public Long count(QuickFilter filter, BoxInfo boxInfo) {
        addEnabledTasksToFilter(filter);
        if (Boolean.TRUE.equals(evalPermissions)) {
            return lookupPetitionService().countTasks(filter, searchPermissions(filter), extenders);
        } else {
            return lookupPetitionService().countQuickSearch(filter, extenders);
        }
    }

    @Nonnull
    @Override
    public ActionProvider getActionProvider() {
        return actionProvider;
    }

    protected PetitionService<?, ?> lookupPetitionService() {
        return ApplicationContextProvider.get().getBean(PetitionService.class);
    }

    protected void addEnabledTasksToFilter(QuickFilter filter) {
        if (tasks != null) {
            filter.forTasks(tasks.toArray(new String[0]));
        }
    }

    protected List<SingularPermission> searchPermissions(QuickFilter filter) {
        return ApplicationContextProvider.get().getBean(PermissionResolverService.class).searchPermissions(filter.getIdUsuarioLogado());
    }

    public RequirementBoxItemDataProvider addExtender(@Nonnull RequirementSearchExtender extender) {
        if (extenders == null) {
            extenders = new ArrayList<>();
        }
        extenders.add(extender);
        return this;
    }

    public RequirementBoxItemDataProvider addExtenders(@Nonnull List<RequirementSearchExtender> extenders) {
        extenders.forEach(this::addExtender);
        return this;
    }

    public RequirementBoxItemDataProvider addFilter(@Nonnull IConsumer<List<Map<String, Serializable>>> filter) {
        if (filters == null) {
            filters = new ArrayList<>();
        }
        filters.add(filter);
        return this;
    }

    public RequirementBoxItemDataProvider addFilters(@Nonnull List<IConsumer<List<Map<String, Serializable>>>> filters) {
        filters.forEach(this::addFilter);
        return this;
    }

    public RequirementBoxItemDataProvider addTask(@Nonnull String filter) {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        tasks.add(filter);
        return this;
    }

    public RequirementBoxItemDataProvider addTasks(@Nonnull List<String> tasks) {
        tasks.forEach(this::addTask);
        return this;
    }

}