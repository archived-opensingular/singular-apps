package org.opensingular.server.module.provider;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.opensingular.server.commons.persistence.dto.TaskInstanceDTO;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.spring.security.PermissionResolverService;
import org.opensingular.server.commons.spring.security.SingularPermission;
import org.opensingular.server.module.BoxInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class TaskBoxDefinitionDataProviderTest {

    @Mock
    private PetitionService petitionService;

    @Mock
    private PermissionResolverService permissionResolverService;

    @InjectMocks
    private TaskBoxItemDataProvider taskItemBoxDataProvider;

    private QuickFilter filter;

    @Before
    public void setUp() {
        filter = new QuickFilter();
    }

    @Test
    public void testSearch() throws Exception {
        Integer               taskId           = 10;
        List<TaskInstanceDTO> taskInstanceDTOS = listOfTaskInstanceDTOForIDsAndTodayDate(taskId);

        when(petitionService.listTasks(eq(filter), anyListOf(SingularPermission.class))).thenReturn(taskInstanceDTOS);

        List<Map<String, Serializable>> itemBoxes       = taskItemBoxDataProvider.search(filter, Mockito.mock(BoxInfo.class));
        Map<String, Serializable>       taskInstanceMap = itemBoxes.get(0);

        assertEquals(taskId, taskInstanceMap.get("taskId"));
        assertEquals(String.class, taskInstanceMap.get("creationDate").getClass());
    }

    @Test
    public void testCount() throws Exception {
        Long taskCount = 10L;
        when(petitionService.countTasks(eq(filter), anyListOf(SingularPermission.class))).thenReturn(taskCount);
        assertThat(taskItemBoxDataProvider.count(filter, Mockito.mock(BoxInfo.class)), Matchers.equalTo(taskCount));
    }

    private List<TaskInstanceDTO> listOfTaskInstanceDTOForIDsAndTodayDate(Integer... ids) {
        return Stream.of(ids).map(this::taskInstanceDTOForIDAndTodayDate).collect(Collectors.toList());
    }

    private TaskInstanceDTO taskInstanceDTOForIDAndTodayDate(Integer taskId) {
        TaskInstanceDTO taskInstanceDTO = new TaskInstanceDTO();
        taskInstanceDTO.setTaskId(taskId);
        taskInstanceDTO.setCreationDate(new Date());
        return taskInstanceDTO;
    }

}