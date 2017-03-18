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
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
public class TaskItemBoxDataProvider implements ItemBoxDataProvider {

    private final PetitionService<?, ?> petitionService;

    private final PermissionResolverService permissionResolverService;

    @Inject
    public TaskItemBoxDataProvider(PetitionService<?, ?> petitionService, PermissionResolverService permissionResolverService) {
        this.petitionService = petitionService;
        this.permissionResolverService = permissionResolverService;
    }

    @Override
    public List<Map<String, Serializable>> search(QuickFilter filter) {
        List<TaskInstanceDTO> taskInstanceDTOS = petitionService.listTasks(filter, searchPermissions(filter));
        return getSingularObjectMapper().toStringSerializableMap(taskInstanceDTOS);
    }

    @NotNull
    private SingularObjectMapper getSingularObjectMapper() {
        return new SingularObjectMapper();
    }

    @Override
    public Long count(QuickFilter filter) {
        return petitionService.countTasks(filter, searchPermissions(filter));
    }

    private List<SingularPermission> searchPermissions(QuickFilter filter) {
        return permissionResolverService.searchPermissions(filter.getIdUsuarioLogado());
    }

    public void configureLineActions(ItemBoxData line) {

    }

}