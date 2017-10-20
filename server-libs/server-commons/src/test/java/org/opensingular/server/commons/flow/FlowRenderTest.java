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

package org.opensingular.server.commons.flow;

import org.junit.Test;
import org.opensingular.flow.core.FlowMap;
import org.opensingular.flow.core.ITaskDefinition;
import org.opensingular.flow.core.SFlowUtil;
import org.opensingular.flow.core.builder.FlowBuilder;
import org.opensingular.flow.core.builder.FlowBuilderImpl;
import org.opensingular.lib.commons.util.Loggable;

public class FlowRenderTest extends AbstractFlowRenderTest implements Loggable {

    public FlowRenderTest() {
        setOpenGeneratedFiles(false);
    }


    @Test
    public void renderBasic() {
        renderImage(new BaseFlowTestDefinition() {
            @Override
            protected FlowMap createFlowMap() {
                ITaskDefinition first = () -> "First";
                ITaskDefinition second = () -> "Second";
                ITaskDefinition third = () -> "Third";
                ITaskDefinition end = () -> "End";

                FlowBuilderImpl f = new FlowBuilderImpl(this);

                f.addHumanTask(first, SFlowUtil.dummyTaskAccessStrategy());
                f.addWaitTask(second);
                f.addJavaTask(third).call(SFlowUtil.dummyTaskJavaCall());
                f.addEndTask(end);

                f.setStartTask(first);
                f.from(first).go(second).setAsDefaultTransition();
                f.from(first).go("transition1", third);
                f.from(first).go("transition2", third);
                f.from(second).go(third).thenGo(end);


                return f.build();
            }
        });
    }

    @Test
    public void renderBasic2() {
        renderImage(new BaseFlowTestDefinition() {
            @Override
            protected FlowMap createFlowMap() {
                FlowBuilder flow = new FlowBuilderImpl(this);

                ITaskDefinition dobarDef  = () -> "The Great Gig";
                ITaskDefinition endbarDef = () -> "The Sky";

                flow.addEndTask(endbarDef);
                flow.addJavaTask(dobarDef).call(SFlowUtil.dummyTaskJavaCall()).go(endbarDef);

                flow.setStartTask(dobarDef);

                return flow.build();
            }
        });
    }
}
