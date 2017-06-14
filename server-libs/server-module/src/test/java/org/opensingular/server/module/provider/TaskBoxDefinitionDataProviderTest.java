package org.opensingular.server.module.provider;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.spring.security.PermissionResolverService;
import org.opensingular.server.module.BoxInfo;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TaskBoxDefinitionDataProviderTest {

    @Mock
    private PetitionService petitionService;

    @Mock
    private PermissionResolverService permissionResolverService;

    @InjectMocks
    private TaskBoxItemDataProvider taskItemBoxDataProvider = new TaskBoxItemDataProvider();

    private QuickFilter        filter;
    private ApplicationContext backup;

    @Before
    public void setUp() {
        if (ApplicationContextProvider.isConfigured()) {
            backup = ApplicationContextProvider.get();
        }
        filter = new QuickFilter();
        ApplicationContext mock = Mockito.mock(ApplicationContext.class);
        Mockito.when(mock.getBean(PetitionService.class)).thenReturn(petitionService);
        Mockito.when(mock.getBean(PermissionResolverService.class)).thenReturn(permissionResolverService);
        new ApplicationContextProvider().setApplicationContext(mock);
    }


    @After
    public void restore() {
        new ApplicationContextProvider().setApplicationContext(backup);
    }


    @Test
    public void testSearch() throws Exception {
        Integer                         taskId           = 10;
        List<Map<String, Serializable>> taskInstanceDTOS = listOfTaskInstanceDTOForIDsAndTodayDate(taskId);

        when(petitionService.listTasks(eq(filter), anyList(), anyList())).thenReturn(taskInstanceDTOS);

        List<Map<String, Serializable>> itemBoxes       = taskItemBoxDataProvider.search(filter, Mockito.mock(BoxInfo.class));
        Map<String, Serializable>       taskInstanceMap = itemBoxes.get(0);

        assertEquals(taskId, taskInstanceMap.get("taskId"));
        assertEquals(Date.class, taskInstanceMap.get("creationDate").getClass());
    }

    @Test
    public void testCount() throws Exception {
        Long taskCount = 10L;
        when(petitionService.countTasks(eq(filter), anyList(), anyList())).thenReturn(taskCount);
        assertThat(taskItemBoxDataProvider.count(filter, Mockito.mock(BoxInfo.class)), Matchers.equalTo(taskCount));
    }

    private List<Map<String, Serializable>> listOfTaskInstanceDTOForIDsAndTodayDate(Integer... ids) {
        return Stream.of(ids).map(this::taskInstanceDTOForIDAndTodayDate).collect(Collectors.toList());
    }

    private Map<String, Serializable> taskInstanceDTOForIDAndTodayDate(Integer taskId) {
        Map<String, Serializable> taskInstanceDTO = new LinkedHashMap<>();
        taskInstanceDTO.put("taskId", taskId);
        taskInstanceDTO.put("creationDate", new Date());
        return taskInstanceDTO;
    }

}