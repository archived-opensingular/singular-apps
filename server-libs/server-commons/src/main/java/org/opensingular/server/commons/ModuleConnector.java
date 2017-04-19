package org.opensingular.server.commons;

import org.opensingular.server.commons.box.BoxItemDataList;
import org.opensingular.server.commons.flow.actions.ActionRequest;
import org.opensingular.server.commons.flow.actions.ActionResponse;
import org.opensingular.server.commons.persistence.filter.QuickFilter;

/**
 * Module connector interface for server communication
 */
public interface ModuleConnector {


    /**
     * Invoke count method for the box with the corresponding {@param boxId}
     * @param boxId
     * @param filter
     * @return
     */
    Long count(String boxId, QuickFilter filter);


    /**
     * Invoke search method for the box with the corresponding {@param boxId}
     * Return the results in the ItemBoxDataList format
     * @param boxId
     * @param filter
     * @return
     */
    BoxItemDataList search(String boxId, QuickFilter filter);

    /**
     * Executes custom actions defined in the {@link BoxItemDataList}
     * @param id
     * @param actionRequest
     * @return
     */
    ActionResponse execute(Long id, ActionRequest actionRequest);

    /**
     * remover essa ação e converter para o modelo de execute
     * @param id
     * @param actionRequest
     * @return
     */
    //TODO REFACTOR
    @Deprecated
    ActionResponse delete(Long id, ActionRequest actionRequest);


    WorkspaceConfigurationMetadata loadWorkspaceConfiguration(String context, String user);
}
