package org.opensingular.server.commons;

import org.opensingular.server.commons.box.ItemBoxDataList;
import org.opensingular.server.commons.flow.actions.ActionRequest;
import org.opensingular.server.commons.flow.actions.ActionResponse;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.dto.BoxConfigurationMetadata;

import java.util.List;

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
    ItemBoxDataList search(String boxId, QuickFilter filter);

    /**
     * Executes custom actions defined in the {@link ItemBoxDataList}
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


    List<BoxConfigurationMetadata> loadWorkspaceConfiguration(String context, String user);
}
