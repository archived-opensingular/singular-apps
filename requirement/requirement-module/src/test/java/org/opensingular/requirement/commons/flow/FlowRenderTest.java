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

package org.opensingular.requirement.commons.flow;

import org.junit.Test;
import org.opensingular.flow.core.ITaskDefinition;
import org.opensingular.flow.core.SFlowUtil;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.test.AbstractFlowRenderTest;

public class FlowRenderTest extends AbstractFlowRenderTest implements Loggable {

    public FlowRenderTest() {
        setOpenGeneratedFiles(false);
    }

    @Test
    public void renderBasic() {
        renderImage(f -> {
            ITaskDefinition first = () -> "First";
            ITaskDefinition second = () -> "Second";
            ITaskDefinition third = () -> "Third";
            ITaskDefinition end = () -> "End";

            f.addHumanTask(first, SFlowUtil.dummyTaskAccessStrategy());
            f.addWaitTask(second);
            f.addJavaTask(third).call(SFlowUtil.dummyTaskJavaCall());
            f.addEndTask(end);

            f.setStartTask(first);
            f.from(first).go(second).setAsDefaultTransition();
            f.from(first).go("transition1", third);
            f.from(first).go("transition2", third);
            f.from(second).go(third).thenGo(end);
        });
    }

    @Test
    public void renderBasic2() {
        renderImage(f -> {
            ITaskDefinition dobarDef = () -> "The Great Gig";
            ITaskDefinition endbarDef = () -> "The Sky";

            f.addEndTask(endbarDef);
            f.addJavaTask(dobarDef).call(SFlowUtil.dummyTaskJavaCall()).go(endbarDef);

            f.setStartTask(dobarDef);
        });
    }
}
