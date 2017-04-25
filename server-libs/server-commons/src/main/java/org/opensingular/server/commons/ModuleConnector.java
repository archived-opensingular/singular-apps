package org.opensingular.server.commons;

import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.server.commons.box.BoxItemDataList;
import org.opensingular.server.commons.box.action.ActionRequest;
import org.opensingular.server.commons.box.action.ActionResponse;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * Module connector interface for server communication
 */
public interface ModuleConnector {


    /**
     * Invoke count method for the box with the corresponding {@param boxId}
     *
     * @param boxId
     * @param filter
     * @return
     */
    Long count(String boxId, QuickFilter filter);


    /**
     * Invoke search method for the box with the corresponding {@param boxId}
     * Return the results in the ItemBoxDataList format
     *
     * @param boxId
     * @param filter
     * @return
     */
    BoxItemDataList search(String boxId, QuickFilter filter);

    /**
     * Executes custom actions defined in the {@link BoxItemDataList}
     *
     * @param id
     * @param actionRequest
     * @return
     */
    ActionResponse execute(Long id, ActionRequest actionRequest);

    WorkspaceConfigurationMetadata loadWorkspaceConfiguration(String context, String user);

    List<Actor> listUsers(Map<String, Object> selectedTask);
}
