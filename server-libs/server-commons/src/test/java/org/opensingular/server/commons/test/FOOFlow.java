package org.opensingular.server.commons.test;

import org.opensingular.flow.core.DefinitionInfo;
import org.opensingular.flow.core.FlowMap;
import org.opensingular.flow.core.ITaskDefinition;
import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.flow.core.ProcessInstance;
import org.opensingular.flow.core.builder.FlowBuilder;
import org.opensingular.flow.core.builder.FlowBuilderImpl;
import org.opensingular.flow.core.defaults.NullTaskAccessStrategy;
import org.opensingular.server.commons.flow.SingularServerTaskPageStrategy;
import org.opensingular.server.commons.wicket.view.form.FormPage;

@DefinitionInfo("fooooooooFlow")
public class FOOFlow extends ProcessDefinition<ProcessInstance> {


    public FOOFlow() {
        super(ProcessInstance.class);
    }

    @Override
    protected FlowMap createFlowMap() {
        FlowBuilder flow = new FlowBuilderImpl(this);

        ITaskDefinition dobarDef  = () -> "Do bar";
        ITaskDefinition endbarDef = () -> "No more bar";

        flow.addEnd(endbarDef);
        flow.addPeopleTask(dobarDef)
                .withExecutionPage(SingularServerTaskPageStrategy.of(FormPage.class))
                .addAccessStrategy(new NullTaskAccessStrategy())
                .go(endbarDef);

        flow.setStart(dobarDef);


        return flow.build();

    }

}
