package org.opensingular.server.p.commons.flow.definition;

import org.opensingular.flow.core.FlowMap;
import org.opensingular.flow.core.ProcessInstance;
import org.opensingular.flow.core.builder.BuilderTransition;
import org.opensingular.server.commons.flow.builder.PetitionProcessDefinition;
import org.opensingular.server.commons.flow.controllers.DefaultAssignController;
import org.opensingular.server.commons.flow.metadata.ServerContextMetaData;
import org.opensingular.server.p.commons.config.PServerContext;

public abstract class ServerProcessDefinition<I extends ProcessInstance> extends PetitionProcessDefinition<I> {

    protected ServerProcessDefinition(Class<I> instanceClass) {
        super(instanceClass);
    }

    protected final BuilderTransition worklist(BuilderTransition t) {
        t.getTransition()
                .setMetaDataValue(ServerContextMetaData.KEY,
                        ServerContextMetaData
                                .enable()
                                .enableOn(PServerContext.WORKLIST));
        return t;
    }

    protected final BuilderTransition petition(BuilderTransition t) {
        t.getTransition()
                .setMetaDataValue(ServerContextMetaData.KEY,
                        ServerContextMetaData
                                .enable()
                                .enableOn(PServerContext.PETITION));
        return t;
    }
}
