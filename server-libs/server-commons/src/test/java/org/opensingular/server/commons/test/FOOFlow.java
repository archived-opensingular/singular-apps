package org.opensingular.server.commons.test;

import javax.annotation.Nonnull;

import org.opensingular.flow.core.DefinitionInfo;
import org.opensingular.flow.core.ITaskDefinition;
import org.opensingular.flow.core.ProcessInstance;
import org.opensingular.flow.core.defaults.NullTaskAccessStrategy;
import org.opensingular.server.commons.flow.SingularServerTaskPageStrategy;
import org.opensingular.server.commons.flow.builder.FlowBuilderPetition;
import org.opensingular.server.commons.wicket.view.form.FormPage;
import org.opensingular.server.p.commons.flow.definition.ServerProcessDefinition;

@DefinitionInfo("fooFlowCommons")
public class FOOFlow extends ServerProcessDefinition<ProcessInstance> {


    public FOOFlow() {
        super(ProcessInstance.class);
    }

    @Override
    protected void buildFlow(@Nonnull FlowBuilderPetition flow) {
        ITaskDefinition dobarDef  = () -> "Do bar";
        ITaskDefinition endbarDef = () -> "No more bar";

        flow.addEnd(endbarDef);
        worklist(flow.addPeopleTask(dobarDef)
                .withExecutionPage(SingularServerTaskPageStrategy.of(FormPage.class))
                .addAccessStrategy(new NullTaskAccessStrategy())
                .go(endbarDef));

        flow.setStart(dobarDef);

    }

}
