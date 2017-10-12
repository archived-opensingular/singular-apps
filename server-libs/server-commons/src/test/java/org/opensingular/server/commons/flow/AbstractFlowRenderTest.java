/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
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

package org.opensingular.server.commons.flow;

import org.junit.Rule;
import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.flow.core.variable.VarService;
import org.opensingular.lib.commons.junit.AbstractTestTempFileSupport;
import org.opensingular.lib.commons.junit.MockInjectorRule;
import org.opensingular.lib.commons.util.Loggable;

import javax.annotation.Nonnull;

/**
 * Support the implementation of tests that renders a image with a graph of the process represented by a flow
 * definition.
 *
 * @author Daniel C. Bordin
 * @since 2017-10-11
 */
public abstract class AbstractFlowRenderTest extends AbstractTestTempFileSupport implements Loggable {

    @Rule
    public MockInjectorRule mockFormInjectRule = new MockInjectorRule();

    /**
     * Renders the image with a graph of the process. It may show on the developer console the result if {@link
     * #setOpenGeneratedFiles(boolean)} is set true.
     */
    protected final void renderImage(@Nonnull FlowDefinition<?> instanceToRender) {
        generateFileAndShowOnDesktopForUser("png", out -> {
            JGraphFlowRenderer.INSTANCE.generatePng(instanceToRender, out);
        });
    }

    protected static abstract class BaseFlowTestDefinition extends FlowDefinition<FlowInstance> {
        public BaseFlowTestDefinition() {
            super(FlowInstance.class, VarService.basic(), "XX");
        }
    }

}
