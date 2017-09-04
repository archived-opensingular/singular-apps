package org.opensingular.server.core.connector;

import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.server.commons.WorkspaceConfigurationMetadata;
import org.opensingular.server.commons.config.IServerContext;

public interface ModuleConnector {

    WorkspaceConfigurationMetadata loadWorkspaceConfiguration(ModuleEntity module, IServerContext serverContext);

}
