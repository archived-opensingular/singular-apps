/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.requirement.module.provider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

import org.opensingular.lib.commons.lambda.IConsumer;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.requirement.module.ActionProvider;
import org.opensingular.requirement.module.BoxItemDataProvider;
import org.opensingular.requirement.module.ActionProvider;
import org.opensingular.requirement.module.BoxInfo;
import org.opensingular.requirement.module.BoxItemDataProvider;
import org.opensingular.requirement.module.persistence.filter.QuickFilter;
import org.opensingular.requirement.module.persistence.query.RequirementSearchExtender;
import org.opensingular.requirement.module.service.RequirementService;
import org.opensingular.requirement.module.spring.security.PermissionResolverService;
import org.opensingular.requirement.module.spring.security.SingularPermission;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequirementBoxItemDataProvider implements BoxItemDataProvider {
    private final Boolean evalPermissions;
    private final ActionProvider actionProvider;
    private final RequirementService requirementService;
    private final List<String> tasks = new ArrayList<>();
    private final List<RequirementSearchExtender> extenders = new ArrayList<>();
    private final List<IConsumer<List<Map<String, Serializable>>>> filters = new ArrayList<>();

    RequirementBoxItemDataProvider(Boolean evalPermissions, ActionProvider actionProvider,
                                   RequirementService requirementService) {
        this.evalPermissions = evalPermissions;
        this.actionProvider = actionProvider;
        this.requirementService = requirementService;
    }

    @Override
    public List<Map<String, Serializable>> search(QuickFilter filter) {
        addEnabledTasksToFilter(filter);
        List<Map<String, Serializable>> requirements;
        if (Boolean.TRUE.equals(evalPermissions)) {
            requirements = requirementService.listTasks(filter, searchPermissions(filter), extenders);
        } else {
            requirements = requirementService.quickSearchMap(filter, extenders);
        }
        filters.forEach(x -> x.accept(requirements));
        return requirements;
    }

    @Override
    public Long count(QuickFilter filter) {
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

    protected void addEnabledTasksToFilter(QuickFilter filter) {
        filter.forTasks(tasks.toArray(new String[0]));
    }

    protected List<SingularPermission> searchPermissions(QuickFilter filter) {
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

    public RequirementBoxItemDataProvider addFilter(@Nonnull IConsumer<List<Map<String, Serializable>>> filter) {
        filters.add(filter);
        return this;
    }

    public RequirementBoxItemDataProvider addFilters(@Nonnull List<IConsumer<List<Map<String, Serializable>>>> filters) {
        filters.forEach(this::addFilter);
        return this;
    }

    public RequirementBoxItemDataProvider addTask(@Nonnull String filter) {
        tasks.add(filter);
        return this;
    }

    public RequirementBoxItemDataProvider addTasks(@Nonnull List<String> tasks) {
        tasks.forEach(this::addTask);
        return this;
    }
}