package org.opensingular.requirement.studio.init;

import org.opensingular.requirement.module.SingularModule;
import org.opensingular.requirement.module.config.DefaultContexts;
import org.opensingular.requirement.module.workspace.WorkspaceRegistry;
import org.opensingular.requirement.studio.context.StudioContext;

public interface StudioSingularModule extends SingularModule {
    @Override
    default void defaultWorkspace(WorkspaceRegistry workspaceRegistry) {
        workspaceRegistry.add(DefaultContexts.AdministrationContext.class);
        workspaceRegistry.add(StudioContext.class);
    }
}