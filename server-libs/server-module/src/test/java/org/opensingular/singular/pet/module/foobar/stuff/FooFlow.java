package org.opensingular.singular.pet.module.foobar.stuff;

import org.opensingular.flow.core.DefinitionInfo;
import org.opensingular.flow.core.FlowMap;
import org.opensingular.flow.core.ITaskDefinition;
import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.flow.core.builder.FlowBuilder;
import org.opensingular.flow.core.builder.FlowBuilderImpl;
import org.opensingular.flow.core.defaults.NullTaskAccessStrategy;
import org.opensingular.server.commons.flow.SingularRequirementTaskPageStrategy;
import org.opensingular.server.commons.wicket.view.form.FormPage;

@DefinitionInfo("fooooooooFlow")
public class FooFlow extends FlowDefinition<FlowInstance> {


    public FooFlow() {
        super(FlowInstance.class);
    }

    @Override
    protected FlowMap createFlowMap() {
        FlowBuilder flow = new FlowBuilderImpl(this);

        ITaskDefinition dobarDef  = () -> "Do bar";
        ITaskDefinition endbarDef = () -> "No more bar";

        flow.addEndTask(endbarDef);
        flow.addHumanTask(dobarDef)
                .withExecutionPage(SingularRequirementTaskPageStrategy.of(FormPage.class))
                .addAccessStrategy(new NullTaskAccessStrategy())
                .go(endbarDef);

        flow.setStartTask(dobarDef);


        return flow.build();

    }

}
