package org.opensingular.singular.pet.module.foobar.stuff;

import org.opensingular.flow.core.FlowMap;
import org.opensingular.flow.core.ITaskDefinition;
import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.flow.core.ProcessInstance;
import org.opensingular.flow.core.builder.BuilderEnd;
import org.opensingular.flow.core.builder.BuilderPeople;
import org.opensingular.flow.core.builder.FlowBuilder;
import org.opensingular.flow.core.builder.FlowBuilderImpl;

public class FooFlow extends ProcessDefinition<ProcessInstance> {


    protected FooFlow(Class<ProcessInstance> instanceClass) {
        super(instanceClass);
    }

    @Override
    protected FlowMap createFlowMap() {
        FlowBuilder flow = new FlowBuilderImpl(this);

        ITaskDefinition dobarDef  = () -> "Do bar";
        ITaskDefinition endbarDef = () -> "No more bar";

        BuilderPeople dobar  = flow.addPeopleTask(dobarDef);
        BuilderEnd    endbar = flow.addEnd(endbarDef);

        flow.setStart(dobarDef);
        dobar.go(endbarDef);

        return flow.build();

    }

}
