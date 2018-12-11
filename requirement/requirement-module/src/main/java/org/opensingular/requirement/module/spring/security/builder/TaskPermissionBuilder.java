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

package org.opensingular.requirement.module.spring.security.builder;

import com.google.common.collect.Lists;
import org.opensingular.flow.core.ITaskDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TaskPermissionBuilder {

    private final List<String> actions;
    private final List<String> forms;
    private final List<String> flows;
    private final List<String> tasks = new ArrayList<>();

    TaskPermissionBuilder(List<String> actions, List<String> forms, List<String> flows) {
        this.actions = actions;
        this.forms = forms;
        this.flows = flows;
    }

    public PermissionBuilderEnd anyTask() {
        this.tasks.add(null);
        return new PermissionBuilderEnd(actions, forms, flows, tasks);
    }

    public PermissionBuilderEnd tasks(ITaskDefinition... taskDefinitions) {
        return tasks(Lists.newArrayList(taskDefinitions));
    }

    public PermissionBuilderEnd tasks(Collection<ITaskDefinition> taskDefinitions) {
        this.tasks.addAll(taskDefinitions.stream().map(ITaskDefinition::getKey).collect(Collectors.toList()));
        return new PermissionBuilderEnd(actions, forms, flows, tasks);
    }

}
