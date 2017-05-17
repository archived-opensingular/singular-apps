package org.opensingular.server.p.commons.flow.definition;

import org.opensingular.flow.core.ProcessInstance;
import org.opensingular.server.commons.flow.builder.PetitionProcessDefinition;

public abstract class ServerProcessDefinition<I extends ProcessInstance> extends PetitionProcessDefinition<I> {

    protected ServerProcessDefinition(Class<I> instanceClass) {
        super(instanceClass);
    }

}
