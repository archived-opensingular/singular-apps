package org.opensingular.server.module.connector;

import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.server.commons.ModuleConnector;
import org.opensingular.server.commons.WorkspaceConfigurationMetadata;
import org.opensingular.server.commons.box.BoxItemDataList;
import org.opensingular.server.commons.box.action.ActionRequest;
import org.opensingular.server.commons.box.action.ActionResponse;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.module.rest.ModuleBackstageService;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public class LocalModuleConnector implements ModuleConnector {

    @Inject
    private ModuleBackstageService moduleBackstageService;

    @Override
    public Long count(String boxId, QuickFilter filter) {
        return moduleBackstageService.count(boxId, filter);
    }

    @Override
    public BoxItemDataList search(String boxId, QuickFilter filter) {
        return moduleBackstageService.search(boxId, filter);
    }

    @Override
    public ActionResponse execute(Long id, ActionRequest actionRequest) {
        return moduleBackstageService.executar(id, actionRequest);
    }

    @Override
    public WorkspaceConfigurationMetadata loadWorkspaceConfiguration(String context, String user) {
        return new WorkspaceConfigurationMetadata(moduleBackstageService.listMenu(context, user));
    }

    @Override
    public List<Actor> listUsers(Map<String, Object> selectedTask) {
        return moduleBackstageService.listAllocableUsers(selectedTask);
    }

}