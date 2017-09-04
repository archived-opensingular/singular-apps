package org.opensingular.server.commons;

import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.service.dto.ItemBox;

import java.util.List;

public interface ModuleConnector {

    WorkspaceConfigurationMetadata loadWorkspaceConfiguration(ModuleEntity module, IServerContext serverContext);

    String count(ItemBox itemBoxDTO, List<String> siglas, String idUsuarioLogado, ModuleEntity module);

}
