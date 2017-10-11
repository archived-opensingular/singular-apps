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

import net.vidageek.mirror.dsl.Mirror;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.flow.core.variable.VarService;
import org.opensingular.form.context.ServiceRegistryLocator;
import org.opensingular.form.spring.SpringServiceRegistry;
import org.opensingular.internal.lib.commons.injection.SingularInjector;
import org.opensingular.lib.commons.context.SingularContext;
import org.opensingular.lib.commons.context.SingularContextSetup;
import org.opensingular.lib.commons.context.SingularSingletonStrategy;
import org.opensingular.lib.commons.test.AbstractTestTempFileSupport;
import org.opensingular.lib.commons.util.Loggable;
import org.springframework.context.ApplicationContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Support the implementation of tests that renders a image with a graph of the process represented by a flow
 * definition.
 *
 * @author Daniel C. Bordin
 * @since 2017-10-11
 */
public abstract class AbstractFlowRenderTest extends AbstractTestTempFileSupport implements Loggable {

    @Rule
    public MockFormInjectRule mockFormInjectRule = new MockFormInjectRule();

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

    public class MockFormInjectRule implements MethodRule, Loggable {
        @Override
        public Statement apply(Statement base, FrameworkMethod method, Object target) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    SingularContextSetup.reset();
                    ApplicationContextMock applicationContextMock = new ApplicationContextMock();
                    ServiceRegistryLocator.setup(new SpringServiceRegistry());
                    applicationContextMock.putBean((SingularInjector) object -> new Mirror().on(object.getClass())
                            .reflectAll().fields().matching(f -> f.isAnnotationPresent(Inject.class)).forEach(f -> {
                                f.setAccessible(true);
                                try {
                                    f.set(object, Mockito.mock(f.getType()));
                                } catch (Exception e) {
                                    getLogger().error(e.getMessage(), e);
                                }
                            }));
                    ((SingularSingletonStrategy) SingularContext.get()).singletonize(ApplicationContext.class,
                            () -> applicationContextMock);
                    base.evaluate();
                }
            };
        }
    }
}
