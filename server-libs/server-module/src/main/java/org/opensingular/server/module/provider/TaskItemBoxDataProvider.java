package org.opensingular.server.module.provider;

import org.jetbrains.annotations.NotNull;
import org.opensingular.server.commons.jackson.SingularObjectMapper;
import org.opensingular.server.commons.persistence.dto.TaskInstanceDTO;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.spring.security.PermissionResolverService;
import org.opensingular.server.commons.spring.security.SingularPermission;
import org.opensingular.server.module.ItemBoxData;
import org.opensingular.server.module.ItemBoxDataProvider;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

@Named
public class TaskItemBoxDataProvider implements ItemBoxDataProvider {

    @Inject
    private PetitionService<?, ?> petitionService;

    @Inject
    private PermissionResolverService permissionResolverService;

    @Override
    public List<Map<String, java.io.Serializable>> search(QuickFilter filter) {
        List<SingularPermission> permissions      = permissionResolverService.searchPermissions(filter.getIdUsuarioLogado());
        List<TaskInstanceDTO>    taskInstanceDTOS = petitionService.listTasks(filter, permissions);
        return getSingularObjectMapper().toMap(taskInstanceDTOS);
    }

    @NotNull
    private SingularObjectMapper getSingularObjectMapper() {
        return new SingularObjectMapper();
    }

    @Override
    public Long count(QuickFilter filter) {
        List<SingularPermission> permissions = permissionResolverService.searchPermissions(filter.getIdUsuarioLogado());
        return petitionService.countTasks(filter, permissions);
    }

    public void configureLineActions(ItemBoxData line) {

    }

}