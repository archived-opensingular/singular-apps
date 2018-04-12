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

import org.opensingular.lib.commons.lambda.IConsumer;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.requirement.commons.persistence.filter.QuickFilter;
import org.opensingular.requirement.commons.persistence.query.RequirementSearchExtender;
import org.opensingular.requirement.commons.service.RequirementService;
import org.opensingular.requirement.commons.spring.security.PermissionResolverService;
import org.opensingular.requirement.commons.spring.security.SingularPermission;
import org.opensingular.requirement.module.ActionProvider;
import org.opensingular.requirement.module.BoxInfo;
import org.opensingular.requirement.module.BoxItemDataProvider;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequirementBoxItemDataProvider implements BoxItemDataProvider {

    private final Boolean evalPermissions;

    private final ActionProvider actionProvider;

    private List<String> tasks;

    private List<RequirementSearchExtender> extenders;

    private List<IConsumer<List<Map<String, Serializable>>>> filters;

    public RequirementBoxItemDataProvider(@Nonnull Boolean evalPermissions, @Nonnull ActionProvider actionProvider) {
        this.evalPermissions = evalPermissions;
        this.actionProvider = actionProvider;
    }

    @Override
    public List<Map<String, Serializable>> search(QuickFilter filter, BoxInfo boxInfo) {
        addEnabledTasksToFilter(filter);
        List<Map<String, Serializable>> requirements;
        if (Boolean.TRUE.equals(evalPermissions)) {
            requirements = lookupRequirementService().listTasks(filter, searchPermissions(filter), extenders);
        } else {
            requirements = lookupRequirementService().quickSearchMap(filter, extenders);
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
            return lookupRequirementService().countTasks(filter, searchPermissions(filter), extenders);
        } else {
            return lookupRequirementService().countQuickSearch(filter, extenders);
        }
    }

    @Nonnull
    @Override
    public ActionProvider getActionProvider() {
        return actionProvider;
    }

    @Nonnull
    protected RequirementService<?, ?> lookupRequirementService() {
        return ApplicationContextProvider.get().getBean(RequirementService.class);
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