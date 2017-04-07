package org.opensingular.server.commons.flow;

import org.junit.Ignore;
import org.junit.Test;
import org.opensingular.flow.core.ProcessDefinition;

public class TheGreatGigInTheSkyFlowRenderTest extends FlowRenderTest {

    @Override
    protected ProcessDefinition<?> getInstanceToRender() {
        return new TheGreatGigInTheSkyFlow();
    }

    @Ignore
    @Test
    @Override
    public void render() {
        super.render();
    }
}
