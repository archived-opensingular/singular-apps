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

package org.opensingular.server.commons.flow.renderer.remote;

import org.opensingular.flow.core.EventType;
import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.flow.core.ITaskPredicate;
import org.opensingular.flow.core.STask;
import org.opensingular.flow.core.STransition;
import org.opensingular.flow.core.renderer.ExecutionHistoryForRendering;
import org.opensingular.flow.core.renderer.IFlowRenderer;
import org.opensingular.server.commons.flow.renderer.remote.dto.FlowDefinitionRenderData;
import org.opensingular.server.commons.flow.renderer.remote.dto.Task;
import org.opensingular.server.commons.flow.renderer.remote.dto.Transition;
import org.opensingular.server.commons.flow.renderer.remote.dto.TransitionTask;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * https://www.yworks.com/en/products_yfiles_about.html
 */
public class YFilesFlowRemoteRenderer implements IFlowRenderer {

    private String url = "http://singular02/yfiles/graph";

    public YFilesFlowRemoteRenderer(String url) {
        if (url != null) {
            this.url = url;
        }
    }

    @Override
    @Nonnull
    public byte[] generatePng(@Nonnull FlowDefinition<?> definition, @Nullable ExecutionHistoryForRendering history) {
        FlowDefinitionRenderData pd = new FlowDefinitionRenderData();
        pd.setTasks(new ArrayList<>());
        for (STask<?> task : definition.getFlowMap().getAllTasks()) {
            pd.getTasks().add(from(task, definition.getFlowMap().getStart().getTask()));
        }
        return new RestTemplate().postForObject(url, pd, byte[].class);
    }

    @Override
    public void generatePng(@Nonnull FlowDefinition<?> definition, @Nullable ExecutionHistoryForRendering history,
            @Nonnull OutputStream out) throws IOException {
        out.write(generatePng(definition, history));
    }

    private Task from(STask<?> task, STask<?> startTask) {
        Task t = new Task(task.isWait(), task.isJava(), task.isPeople(), task.isEnd(), task.getName(), task.getAbbreviation(), task.equals(startTask), new ArrayList<>(0));
        for (STransition mt : task.getTransitions()) {
            t.getTransitions().add(from(mt));
        }
        return t;
    }

    private Transition from(STransition mt) {
        Transition t = new Transition(from(mt.getOrigin()), from(mt.getDestination()), mt.getName(), from(mt.getPredicate()));
        return t;
    }

    private EventType from(ITaskPredicate predicate) {
        if (predicate != null) {
            return predicate.getDisplayEventType();
        }
        return null;
    }


    private TransitionTask from(STask<?> origin) {
        return new TransitionTask(origin.getAbbreviation(), origin.getName());
    }
}
