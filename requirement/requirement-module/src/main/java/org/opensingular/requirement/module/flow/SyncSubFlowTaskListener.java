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

package org.opensingular.requirement.module.flow;


import org.opensingular.flow.core.ExecutionContext;
import org.opensingular.flow.core.StartedTaskListener;
import org.opensingular.flow.core.TaskInstance;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.requirement.module.service.RequirementInstance;
import org.opensingular.requirement.module.service.RequirementService;

import java.util.List;

/**
 * Started Task Listener to check if all children flows ended and, if so, trigger a transition on the parent flow.
 */
public class SyncSubFlowTaskListener implements StartedTaskListener {

    private String parentTransitionName;

    public SyncSubFlowTaskListener(String parentTransitionName) {
        this.parentTransitionName = parentTransitionName;
    }

    @Override
    public void onTaskStart(TaskInstance taskInstance, ExecutionContext executionContext) {
        final RequirementService              requirementService = ApplicationContextProvider.get().getBean(RequirementService.class);
        final RequirementInstance             requirement        = requirementService.getRequirementInstance(executionContext.getFlowInstance());
        final RequirementInstance             parent             = requirement.getParentRequirementOrException();
        final List<RequirementInstance<?, ?>> children           = parent.getChildrenRequirements();

        final boolean allTasksEnded = children
                .stream()
                .allMatch(r -> r.getCurrentTaskOrException().isEnd());
        if (allTasksEnded) {
            parent.getFlowInstance().prepareTransition(parentTransitionName).go();
        }
    }
}
