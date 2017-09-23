package org.opensingular.server.commons.flow;

import org.junit.Test;
import org.opensingular.flow.core.ExecutionContext;
import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.flow.core.FlowMap;
import org.opensingular.flow.core.ITaskDefinition;
import org.opensingular.flow.core.STask;
import org.opensingular.flow.core.SUser;
import org.opensingular.flow.core.TaskAccessStrategy;
import org.opensingular.flow.core.builder.FlowBuilder;
import org.opensingular.flow.core.builder.FlowBuilderImpl;
import org.opensingular.flow.core.variable.VarService;
import org.opensingular.lib.commons.test.AbstractTestTempFileSupport;
import org.opensingular.lib.commons.util.Loggable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public class FlowRenderTest extends AbstractTestTempFileSupport implements Loggable {

    public FlowRenderTest() {
        setOpenGeneratedFiles(false);
    }

    protected void renderImage(FlowDefinition<?> instanceToRender) {
        generateFileAndShowOnDesktopForUser("png", out -> {
            JGraphFlowRenderer.INSTANCE.generatePng(instanceToRender, out);
        });
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

                f.addHumanTask(first, new DummyTaskAccessStrategy());
                f.addWaitTask(second);
                f.addJavaTask(third).call(FlowRenderTest::dummyCall);
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
                flow.addJavaTask(dobarDef).call(FlowRenderTest::dummyCall).go(endbarDef);

                flow.setStartTask(dobarDef);

                return flow.build();
            }
        });
    }

    public static Object dummyCall(ExecutionContext executionContext) {
        return null;
    }

    private static abstract class BaseFlowTestDefinition extends FlowDefinition<FlowInstance> {
        public BaseFlowTestDefinition() {
            super(FlowInstance.class, VarService.basic(), "XX");
        }
    }

    public static class DummyTaskAccessStrategy extends TaskAccessStrategy {

        @Override
        public boolean canExecute(FlowInstance instance, SUser user) {
            return false;
        }

        @Override
        public Set<Integer> getFirstLevelUsersCodWithAccess(FlowInstance instancia) {
            return null;
        }

        @Override
        public List<? extends SUser> listAllocableUsers(FlowInstance instancia) {
            return null;
        }

        @Nonnull
        @Override
        public List<String> getExecuteRoleNames(FlowDefinition definicao, STask task) {
            return null;
        }
    }
}
