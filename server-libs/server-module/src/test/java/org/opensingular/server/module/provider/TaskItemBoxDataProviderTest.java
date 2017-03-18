package org.opensingular.server.module.provider;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.opensingular.server.commons.persistence.dto.TaskInstanceDTO;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.spring.security.PermissionResolverService;
import org.opensingular.server.commons.spring.security.SingularPermission;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TaskItemBoxDataProviderTest {

    @Mock
    private PetitionService<?, ?> petitionService;

    @Mock
    private PermissionResolverService permissionResolverService;

    @Mock
    private List<SingularPermission> permissions;

    @InjectMocks
    private TaskItemBoxDataProvider taskItemBoxDataProvider;

    @Test
    public void search() throws Exception {

        String idUsuarioLogado = "1";
        QuickFilter filter = new QuickFilter().withIdUsuarioLogado(idUsuarioLogado);
        TaskInstanceDTO taskInstanceDTO = new TaskInstanceDTO();
        Integer taskId = 10;

        taskInstanceDTO.setTaskId(taskId);
        taskInstanceDTO.setCreationDate(new Date());

        when(permissionResolverService.searchPermissions(eq(idUsuarioLogado))).thenReturn(permissions);
        when(petitionService.listTasks(eq(filter), eq(permissions))).thenReturn(Collections.singletonList(taskInstanceDTO));

        List<Map<String, Serializable>> itemBoxes = taskItemBoxDataProvider.search(filter);

        assertNotNull(itemBoxes);
        assertThat(itemBoxes, Matchers.iterableWithSize(1));
        assertEquals(taskId, itemBoxes.get(0).get("taskId"));
        assertEquals(itemBoxes.get(0).get("creationDate").getClass(), String.class);
    }

    @Test
    public void count() throws Exception {

        String idUsuarioLogado = "1";
        QuickFilter filter = new QuickFilter().withIdUsuarioLogado(idUsuarioLogado);
        Long tasks = 10L;

        when(permissionResolverService.searchPermissions(eq(idUsuarioLogado))).thenReturn(permissions);
        when(petitionService.countTasks(eq(filter), eq(permissions))).thenReturn(tasks);

        Long count = taskItemBoxDataProvider.count(filter);

        assertThat(count, Matchers.equalTo(tasks));

    }

}