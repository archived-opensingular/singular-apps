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

package org.opensingular.requirement.commons.form;

import org.opensingular.flow.core.ITaskDefinition;
import org.opensingular.flow.core.TaskInstance;
import org.opensingular.form.SInstance;
import org.opensingular.requirement.commons.service.RequirementUtil;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Used to match Current Task, retrieved from ServerSInstanceProcessAwareService, and those
 * informed.
 * Helper methods in and notIn are here to help.
 */
public class CurrentTaskPredicate implements Predicate<SInstance>{
    private final ITaskDefinition[] referenceTasks;
    private final boolean negate;
    private TaskInstance currentTask;

    public static CurrentTaskPredicate  in(ITaskDefinition ... referenceTask){
        return new CurrentTaskPredicate(false, referenceTask);
    }

    public static CurrentTaskPredicate notIn(ITaskDefinition ... referenceTask){
        return new CurrentTaskPredicate(true , referenceTask);
    }

    public static CurrentTaskPredicate hasNoCurrentTask(){
        return new NoCurrentTaskPredicate();
    }

    public static CurrentTaskPredicate hasCurrentTask(){
        return new ExistsCurrentTaskPredicate();
    }

    public CurrentTaskPredicate(boolean negate, ITaskDefinition ... referenceTasks) {
        this.negate = negate;
        this.referenceTasks = referenceTasks;

    }

    @Override
    public boolean test(SInstance x) {
        updateCurrentTask(x);
        Boolean result = getCurrentTask()
                .map(this::matchesReferenceTask).orElse(Boolean.FALSE);
        if (negate) {
            return !result;
        }
        return result ;
    }

    protected Optional<TaskInstance> getCurrentTask() {
        return Optional.ofNullable(currentTask);
    }

    private boolean matchesReferenceTask(TaskInstance t) {
        for(ITaskDefinition ref : referenceTasks){
            if (ref.getName().equalsIgnoreCase(t.getName())) {
                return true;
            }
        }
        return false;
    }

    protected void updateCurrentTask(SInstance instance) {
        currentTask = RequirementUtil.getCurrentTaskEntity(instance).orElse(null);
    }
}

class NoCurrentTaskPredicate extends CurrentTaskPredicate {

    public NoCurrentTaskPredicate() {
        super(false);
    }

    @Override
    public boolean test(SInstance x) {
        updateCurrentTask(x);
        return !getCurrentTask().isPresent();
    }
}

class ExistsCurrentTaskPredicate extends CurrentTaskPredicate {

    public ExistsCurrentTaskPredicate() {
        super(false);
    }

    @Override
    public boolean test(SInstance x) {
        updateCurrentTask(x);
        return getCurrentTask().isPresent();
    }
}