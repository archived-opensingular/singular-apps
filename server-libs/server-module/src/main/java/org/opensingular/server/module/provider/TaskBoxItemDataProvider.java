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

package org.opensingular.server.module.provider;

import org.opensingular.flow.core.ITaskDefinition;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.jackson.SingularObjectMapper;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.persistence.requirement.RequirementSearchExtender;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.spring.security.PermissionResolverService;
import org.opensingular.server.commons.spring.security.SingularPermission;
import org.opensingular.server.module.ActionProvider;
import org.opensingular.server.module.BoxInfo;
import org.opensingular.server.module.BoxItemDataProvider;
import org.opensingular.server.module.DefaultActionProvider;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskBoxItemDataProvider implements BoxItemDataProvider {

    private ActionProvider        actionProvider = new DefaultActionProvider();
    private List<ITaskDefinition> tasksFilter    = new ArrayList<>();

    public TaskBoxItemDataProvider(ActionProvider actionProvider) {
        this.actionProvider = actionProvider;
    }

    public TaskBoxItemDataProvider() {
    }

    public TaskBoxItemDataProvider(ActionProvider actionProvider, ITaskDefinition... taskDefinitions) {
        this.actionProvider = actionProvider;
        tasksFilter.addAll(Arrays.asList(taskDefinitions));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Serializable>> search(QuickFilter filter, BoxInfo boxInfo) {
        filter.forTasks(tasksFilter.stream().map(ITaskDefinition::getName).collect(Collectors.toList()).toArray(new String[0]));
        return ApplicationContextProvider.get()
                .getBean(PetitionService.class).listTasks(filter, searchPermissions(filter), getExtenders(filter));
    }

    @Nonnull
    private SingularObjectMapper getSingularObjectMapper() {
        return new SingularObjectMapper();
    }

    @Override
    public Long count(QuickFilter filter, BoxInfo boxInfo) {
        filter.forTasks(tasksFilter.stream().map(ITaskDefinition::getName).collect(Collectors.toList()).toArray(new String[0]));
        return ApplicationContextProvider.get()
                .getBean(PetitionService.class).countTasks(filter, searchPermissions(filter), getExtenders(filter));
    }

    @Override
    public ActionProvider getActionProvider() {
        return actionProvider;
    }

    private List<SingularPermission> searchPermissions(QuickFilter filter) {
        return ApplicationContextProvider.get().getBean(PermissionResolverService.class).searchPermissions(filter.getIdUsuarioLogado());
    }

    protected List<RequirementSearchExtender> getExtenders(QuickFilter filter){
        return Collections.emptyList();
    }

}