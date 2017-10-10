package org.opensingular.server.module.rest;

import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.server.commons.WorkspaceConfigurationMetadata;
import org.opensingular.server.commons.box.BoxItemDataList;
import org.opensingular.server.commons.box.action.ActionRequest;
import org.opensingular.server.commons.box.action.ActionResponse;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.module.connector.LocalModuleConnector;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.opensingular.server.commons.RESTPaths.*;

@RestController
@RequestMapping("/rest/flow")
public class RESTModuleConnector extends LocalModuleConnector {

    @Override
    @RequestMapping(value = "/count/{boxId}", method = RequestMethod.POST)
    public Long count(@PathVariable String boxId, @RequestBody QuickFilter filter) {
        return super.count(boxId, filter);

    }

    @Override
    @RequestMapping(value = "/search/{boxId}", method = RequestMethod.POST)
    public BoxItemDataList search(@PathVariable String boxId, @RequestBody QuickFilter filter) {
        return super.search(boxId, filter);

    }

    @Override
    @RequestMapping(value = PATH_BOX_ACTION + EXECUTE, method = RequestMethod.POST)
    public ActionResponse execute(@RequestParam Long id, @RequestBody ActionRequest actionRequest) {
        return super.execute(id, actionRequest);

    }

    @Override
    @RequestMapping(value = WORKSPACE_CONFIGURATION, method = RequestMethod.GET)
    public WorkspaceConfigurationMetadata loadWorkspaceConfiguration(@RequestParam(MENU_CONTEXT) String context, @RequestParam(USER) String user) {
        return super.loadWorkspaceConfiguration(context, user);

    }

    @Override
    @RequestMapping(value = PATH_BOX_SEARCH + USERS, method = {RequestMethod.POST, RequestMethod.GET})
    public List<Actor> listUsers(@RequestBody Map<String, Object> selectedTask) {
        return super.listUsers(selectedTask);
    }

}