package org.opensingular.server.commons.flow;

import org.opensingular.flow.core.DefinitionInfo;
import org.opensingular.flow.core.ExecutionContext;
import org.opensingular.flow.core.FlowMap;
import org.opensingular.flow.core.ITaskDefinition;
import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.flow.core.ProcessInstance;
import org.opensingular.flow.core.builder.FlowBuilder;
import org.opensingular.flow.core.builder.FlowBuilderImpl;
import org.opensingular.flow.core.defaults.NullTaskAccessStrategy;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.wicket.view.form.FormPage;

@DefinitionInfo("AhaaaaaaaaaaaAaaahhAhhhhAaaah")
public class TheGreatGigInTheSkyFlow extends ProcessDefinition<ProcessInstance> implements Loggable {


    public TheGreatGigInTheSkyFlow() {
        super(ProcessInstance.class);
    }

    @Override
    protected FlowMap createFlowMap() {
        FlowBuilder flow = new FlowBuilderImpl(this);

        ITaskDefinition dobarDef  = () -> "The Great Gig";
        ITaskDefinition endbarDef = () -> "The Sky";

        flow.addEnd(endbarDef);
        flow.addJavaTask(dobarDef)
                .call(this::cryingSolo)
                .go(endbarDef);

        flow.setStart(dobarDef);


        return flow.build();

    }

    public Object cryingSolo(ExecutionContext executionContext){
        getLogger().info("WOOOAHAHAAAHAHAHA HAAAAAAAAYAYAYAAAAAAA WOAHAHAHAYAYAYAA AAAH AAAH AYYYAHHH AHHHHHHH AHHHHHH OHHHHHH OOOOOOOOOO ooooooooo ooooooooooooh WOOOAHAHAAAHAHAHA HAAAAAAAAYAYAYAAAAAAA WOAHAHAHAYAYAYAA AAAH AAAH AYYYAHHH AHHHHHHH AHHHHHH OHHHHHH OOOOOOOOOO ooooooooo ooooooooooooh ");
        return null;
    }

}
