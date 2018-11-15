/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.requirement.module.provider;

import org.opensingular.flow.core.ITaskDefinition;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.requirement.module.ActionProvider;
import org.opensingular.requirement.module.BoxItemDataProvider;
import org.opensingular.requirement.module.persistence.filter.BoxFilter;
import org.opensingular.requirement.module.persistence.query.RequirementSearchExtender;
import org.opensingular.requirement.module.service.RequirementService;
import org.opensingular.requirement.module.spring.security.PermissionResolverService;
import org.opensingular.requirement.module.spring.security.SingularPermission;
import org.opensingular.requirement.module.wicket.box.BoxItemDataFilter;
import org.opensingular.requirement.module.wicket.box.DateBoxItemDataFilter;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RequirementBoxItemDataProvider implements BoxItemDataProvider {
    private final Boolean evalPermissions;
    private final ActionProvider actionProvider;
    private final RequirementService requirementService;
    private final List<ITaskDefinition> tasks = new ArrayList<>();
    private final List<RequirementSearchExtender> extenders = new ArrayList<>();
    private final Set<BoxItemDataFilter> filters = new HashSet<>();

    RequirementBoxItemDataProvider(Boolean evalPermissions, ActionProvider actionProvider,
                                   RequirementService requirementService) {
        this.evalPermissions = evalPermissions;
        this.actionProvider = actionProvider;
        this.requirementService = requirementService;
    }

    @Override
    public List<Map<String, Serializable>> search(BoxFilter filter) {
        addEnabledTasksToFilter(filter);
        List<Map<String, Serializable>> requirements;
        if (Boolean.TRUE.equals(evalPermissions)) {
            requirements = requirementService.listTasks(filter, searchPermissions(filter), extenders);
        } else {
            requirements = requirementService.quickSearchMap(filter, extenders);
        }
        filters.forEach(f -> f.doFilter(requirements));
        return requirements;
    }

    @Override
    public Long count(BoxFilter filter) {
        addEnabledTasksToFilter(filter);
        if (Boolean.TRUE.equals(evalPermissions)) {
            return requirementService.countTasks(filter, searchPermissions(filter), extenders);
        } else {
            return requirementService.countQuickSearch(filter, extenders);
        }
    }

    @Nonnull
    @Override
    public ActionProvider getActionProvider() {
        return actionProvider;
    }

    protected void addEnabledTasksToFilter(BoxFilter filter) {
        filter.tasks(tasks);
    }

    protected List<SingularPermission> searchPermissions(BoxFilter filter) {
        return ApplicationContextProvider.get().getBean(PermissionResolverService.class).searchPermissions(filter.getIdUsuarioLogado());
    }

    public RequirementBoxItemDataProvider addExtender(@Nonnull RequirementSearchExtender RequirementSearchExtender) {
        extenders.add(RequirementSearchExtender);
        return this;
    }

    public RequirementBoxItemDataProvider addExtenders(@Nonnull List<RequirementSearchExtender> extenderFactories) {
        extenderFactories.forEach(this::addExtender);
        return this;
    }

    public RequirementBoxItemDataProvider addFilter(@Nonnull BoxItemDataFilter filter) {
        filters.add(filter);
        return this;
    }

    public RequirementBoxItemDataProvider addFilters(@Nonnull List<BoxItemDataFilter> filters) {
        filters.forEach(this::addFilter);
        return this;
    }

    public RequirementBoxItemDataProvider addTask(@Nonnull ITaskDefinition filter) {
        tasks.add(filter);
        return this;
    }

    public RequirementBoxItemDataProvider addTasks(@Nonnull List<ITaskDefinition> tasks) {
        tasks.forEach(this::addTask);
        return this;
    }

    /**
     * Method for include a dateFormatter in the filters.
     * This method should be used to change the formatter of the date, or include more than one.
     *
     * @param dateFormatter String with the date Formatter.
     * @param alias         The alias of the date columns that the formatter must changed.
     * @return <code>this</code>
     */
    public RequirementBoxItemDataProvider addDateFilters(String dateFormatter, String... alias) {
        filters.add(new DateBoxItemDataFilter(dateFormatter, alias));
        return this;
    }

}