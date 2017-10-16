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
