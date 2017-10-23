/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.server.module.provider;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.opensingular.lib.commons.context.SingularContextSetup;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.RequirementService;
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

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class TaskBoxDefinitionDataProviderTest {

    @Mock
    private RequirementService requirementService;

    @Mock
    private PermissionResolverService permissionResolverService;

    @InjectMocks
    private TaskBoxItemDataProvider taskItemBoxDataProvider = new TaskBoxItemDataProvider();

    private QuickFilter        filter;

    @Before
    public void setUp() {
        filter = new QuickFilter();
        ApplicationContext mock = Mockito.mock(ApplicationContext.class);
        Mockito.when(mock.getBean(RequirementService.class)).thenReturn(requirementService);
        Mockito.when(mock.getBean(PermissionResolverService.class)).thenReturn(permissionResolverService);
        SingularContextSetup.reset();
        new ApplicationContextProvider().setApplicationContext(mock);
    }

    @Test
    public void testSearch() throws Exception {
        Integer                         taskId           = 10;
        List<Map<String, Serializable>> taskInstanceDTOS = listOfTaskInstanceDTOForIDsAndTodayDate(taskId);

        when(requirementService.listTasks(eq(filter), anyList(), anyList())).thenReturn(taskInstanceDTOS);

        List<Map<String, Serializable>> itemBoxes       = taskItemBoxDataProvider.search(filter, Mockito.mock(BoxInfo.class));
        Map<String, Serializable>       taskInstanceMap = itemBoxes.get(0);

        assertEquals(taskId, taskInstanceMap.get("taskId"));
        assertEquals(Date.class, taskInstanceMap.get("creationDate").getClass());
    }

    @Test
    public void testCount() throws Exception {
        Long taskCount = 10L;
        when(requirementService.countTasks(eq(filter), anyList(), anyList())).thenReturn(taskCount);
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