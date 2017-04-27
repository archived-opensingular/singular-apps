package org.opensingular.server.module.provider;

import org.jetbrains.annotations.NotNull;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.jackson.SingularObjectMapper;
import org.opensingular.server.commons.persistence.dto.TaskInstanceDTO;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.spring.security.PermissionResolverService;
import org.opensingular.server.commons.spring.security.SingularPermission;
import org.opensingular.server.module.ActionProvider;
import org.opensingular.server.module.BoxInfo;
import org.opensingular.server.module.BoxItemDataProvider;
import org.opensingular.server.module.DefaultActionProvider;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TaskBoxItemDataProvider implements BoxItemDataProvider {


    private ActionProvider actionProvider = new DefaultActionProvider();

    public TaskBoxItemDataProvider(ActionProvider actionProvider) {
        this.actionProvider = actionProvider;
    }

    public TaskBoxItemDataProvider() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Serializable>> search(QuickFilter filter, BoxInfo boxInfo) {
        List<TaskInstanceDTO> taskInstanceDTOS = ApplicationContextProvider.get().getBean(PetitionService.class).listTasks(filter, searchPermissions(filter));
        return getSingularObjectMapper().toStringSerializableMap(taskInstanceDTOS);
    }

    @NotNull
    private SingularObjectMapper getSingularObjectMapper() {
        return new SingularObjectMapper();
    }

    @Override
    public Long count(QuickFilter filter, BoxInfo boxInfo) {
        return ApplicationContextProvider.get().getBean(PetitionService.class).countTasks(filter, searchPermissions(filter));
    }

    @Override
    public ActionProvider getActionProvider() {
        return actionProvider;
    }

    private List<SingularPermission> searchPermissions(QuickFilter filter) {
        return ApplicationContextProvider.get().getBean(PermissionResolverService.class).searchPermissions(filter.getIdUsuarioLogado());
    }

}