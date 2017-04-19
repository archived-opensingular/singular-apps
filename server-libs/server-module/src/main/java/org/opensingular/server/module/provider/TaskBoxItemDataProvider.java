package org.opensingular.server.module.provider;

import org.jetbrains.annotations.NotNull;
import org.opensingular.server.commons.jackson.SingularObjectMapper;
import org.opensingular.server.commons.persistence.dto.TaskInstanceDTO;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.spring.security.PermissionResolverService;
import org.opensingular.server.commons.spring.security.SingularPermission;
import org.opensingular.server.module.ActionProvider;
import org.opensingular.server.module.BoxInfo;
import org.opensingular.server.module.DefaultActionProvider;
import org.opensingular.server.module.BoxItemDataProvider;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
public class TaskBoxItemDataProvider implements BoxItemDataProvider {

    @Inject
    private PetitionService<?, ?> petitionService;

    @Inject
    private PermissionResolverService permissionResolverService;

    @Override
    public List<Map<String, Serializable>> search(QuickFilter filter, BoxInfo boxInfo) {
        List<TaskInstanceDTO> taskInstanceDTOS = petitionService.listTasks(filter, searchPermissions(filter));
        return getSingularObjectMapper().toStringSerializableMap(taskInstanceDTOS);
    }

    @NotNull
    private SingularObjectMapper getSingularObjectMapper() {
        return new SingularObjectMapper();
    }

    @Override
    public Long count(QuickFilter filter, BoxInfo boxInfo) {
        return petitionService.countTasks(filter, searchPermissions(filter));
    }

    @Override
    public ActionProvider getActionProvider() {
        return new DefaultActionProvider();
    }

    private List<SingularPermission> searchPermissions(QuickFilter filter) {
        return permissionResolverService.searchPermissions(filter.getIdUsuarioLogado());
    }

}