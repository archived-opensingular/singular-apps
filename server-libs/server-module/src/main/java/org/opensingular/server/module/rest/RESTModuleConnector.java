package org.opensingular.server.module.rest;

import org.opensingular.server.commons.ModuleConnector;
import org.opensingular.server.commons.WorkspaceConfigurationMetadata;
import org.opensingular.server.commons.box.BoxItemDataList;
import org.opensingular.server.commons.box.action.ActionRequest;
import org.opensingular.server.commons.box.action.ActionResponse;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

import static org.opensingular.server.commons.RESTPaths.DELETE;
import static org.opensingular.server.commons.RESTPaths.EXECUTE;
import static org.opensingular.server.commons.RESTPaths.MENU_CONTEXT;
import static org.opensingular.server.commons.RESTPaths.PATH_BOX_ACTION;
import static org.opensingular.server.commons.RESTPaths.WORKSPACE_CONFIGURATION;
import static org.opensingular.server.commons.RESTPaths.USER;

@RestController
@RequestMapping("/rest/flow")
public class RESTModuleConnector implements ModuleConnector {

    @Inject
    private RestBackstageService restBackstageService;

    @Override
    @RequestMapping(value = "/count/{boxId}", method = RequestMethod.POST)
    public Long count(@PathVariable String boxId, @RequestBody QuickFilter filter) {
        return restBackstageService.count(boxId, filter);
    }

    @Override
    @RequestMapping(value = "/search/{boxId}", method = RequestMethod.POST)
    public BoxItemDataList search(@PathVariable String boxId, @RequestBody QuickFilter filter) {
        return restBackstageService.search(boxId, filter);
    }

    @Override
    @RequestMapping(value = PATH_BOX_ACTION + EXECUTE, method = RequestMethod.POST)
    public ActionResponse execute(@RequestParam Long id, @RequestBody ActionRequest actionRequest) {
        return restBackstageService.executar(id, actionRequest);
    }

    @Override
    @RequestMapping(value = PATH_BOX_ACTION + DELETE, method = RequestMethod.POST)
    public ActionResponse delete(@RequestParam Long id, @RequestBody ActionRequest actionRequest) {
        return restBackstageService.excluir(id, actionRequest);
    }

    @Override
    @RequestMapping(value = WORKSPACE_CONFIGURATION, method = RequestMethod.GET)
    public WorkspaceConfigurationMetadata loadWorkspaceConfiguration(@RequestParam(MENU_CONTEXT) String context, @RequestParam(USER) String user) {
        return new WorkspaceConfigurationMetadata(restBackstageService.listMenu(context, user));
    }

}