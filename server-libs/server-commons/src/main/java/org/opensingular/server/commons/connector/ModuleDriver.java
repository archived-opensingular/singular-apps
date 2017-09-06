package org.opensingular.server.commons.connector;

import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.server.commons.WorkspaceConfigurationMetadata;
import org.opensingular.server.commons.box.BoxItemDataMap;
import org.opensingular.server.commons.box.action.ActionRequest;
import org.opensingular.server.commons.box.action.ActionResponse;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.service.dto.ItemActionConfirmation;
import org.opensingular.server.commons.service.dto.ItemBox;

import java.util.List;
import java.util.Map;

public interface ModuleDriver {

    /**
     * Retrieve the module workspace
     */
    WorkspaceConfigurationMetadata retrieveModuleWorkspace(ModuleEntity module, IServerContext serverContext);

    /**
     * Count all elements inside a box
     */
    String countAll(ModuleEntity module, ItemBox box, List<String> flowNames, String loggedUser);

    /**
     * Count elements inside a box, applying the filter
     */
    long countFiltered(ModuleEntity module, ItemBox box, QuickFilter filter);

    /**
     * Searchelements inside a box, applying the filter
     */
    List<BoxItemDataMap> searchFiltered(ModuleEntity module, ItemBox box, QuickFilter filter);

    /**
     * Find users that can execute the confirmAction
     */
    List<Actor> findEligibleUsers(ModuleEntity module, BoxItemDataMap rowItemData, ItemActionConfirmation confirmAction);

    /**
     * Execute a action
     */
    ActionResponse executeAction(ModuleEntity module, BoxItemAction rowAction, Map<String, String> params, ActionRequest actionRequest);

    /**
     * Build a static endpoint
     */
    String buildUrlToBeRedirected(BoxItemDataMap rowItemData, BoxItemAction rowAction, Map<String, String> params, String baseURI);

}