package org.opensingular.server.commons.connector;

import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.server.commons.WorkspaceConfigurationMetadata;
import org.opensingular.server.commons.box.BoxItemDataMap;
import org.opensingular.server.commons.box.action.ActionRequest;
import org.opensingular.server.commons.box.action.ActionResponse;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.dto.ItemActionConfirmation;
import org.opensingular.server.commons.service.dto.ItemBox;

import java.util.List;

public interface ModuleConnector {

    WorkspaceConfigurationMetadata loadWorkspaceConfiguration(ModuleEntity module, IServerContext serverContext);

    String count(ItemBox itemBoxDTO, List<String> siglas, String idUsuarioLogado, ModuleEntity module);

    long countQuickSearch(ModuleEntity module, ItemBox itemBox, QuickFilter filter);

    List<BoxItemDataMap> quickSearch(ModuleEntity module, ItemBox itemBox, QuickFilter filter);

    List<Actor> buscarUsuarios(ModuleEntity module, BoxItemDataMap boxItemDataMap, ItemActionConfirmation confirmation);

    ActionResponse callModule(String actionURI, ActionRequest actionRequest);

}
