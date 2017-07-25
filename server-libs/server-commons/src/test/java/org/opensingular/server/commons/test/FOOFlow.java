package org.opensingular.server.commons.test;

import javax.annotation.Nonnull;

import org.opensingular.flow.core.DefinitionInfo;
import org.opensingular.flow.core.ITaskDefinition;
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.flow.core.defaults.PermissiveTaskAccessStrategy;
import org.opensingular.server.commons.flow.SingularRequirementTaskPageStrategy;
import org.opensingular.server.commons.flow.builder.RequirementFlowBuilder;
import org.opensingular.server.commons.flow.builder.RequirementFlowDefinition;
import org.opensingular.server.commons.wicket.view.form.FormPage;


@DefinitionInfo("fooFlowCommons")
public class FOOFlow extends RequirementFlowDefinition<FlowInstance> {


    public FOOFlow() {
        super(FlowInstance.class);
    }

    @Override
    protected void buildFlow(@Nonnull RequirementFlowBuilder flow) {
        ITaskDefinition dobarDef  = () -> "Do bar";
        ITaskDefinition endbarDef = () -> "No more bar";

        flow.addEndTask(endbarDef);
        flow.addHumanTask(dobarDef)
                .withExecutionPage(SingularRequirementTaskPageStrategy.of(FormPage.class))
                .uiAccess(new PermissiveTaskAccessStrategy())
                .go(endbarDef);

        flow.setStartTask(dobarDef);

    }

}
