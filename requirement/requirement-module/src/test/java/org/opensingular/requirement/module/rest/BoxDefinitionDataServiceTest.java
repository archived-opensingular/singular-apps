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

package org.opensingular.requirement.module.rest;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.opensingular.lib.commons.context.SingularContextSetup;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.requirement.commons.persistence.filter.QuickFilter;
import org.opensingular.requirement.commons.spring.security.AuthorizationServiceImpl;
import org.opensingular.requirement.module.BoxController;
import org.opensingular.requirement.module.BoxItemDataProvider;
import org.opensingular.requirement.module.DefaultActionProvider;
import org.opensingular.requirement.module.SingularModuleConfiguration;
import org.opensingular.requirement.module.workspace.BoxDefinition;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class BoxDefinitionDataServiceTest {

    @Mock
    private ApplicationContext          context;
    @Mock
    private AuthorizationServiceImpl        authorizationService;
    @Mock
    private SingularModuleConfiguration singularModuleConfiguration;
    @InjectMocks
    private ModuleBackstageService      moduleBackstageService;

    private QuickFilter quickFilter;

    private Long countSize = 1L;

    private String boxId = "123456";

    public void setUpApplicationContextMock() {

        ApplicationContextProvider applicationContextProvider = new ApplicationContextProvider();
        applicationContextProvider.setApplicationContext(context);
        when(context.getBean(AuthorizationServiceImpl.class)).thenReturn(authorizationService);

    }

    @Before
    public void setUp() {
        SingularContextSetup.reset();
        BoxDefinition boxDefinition = mock(BoxDefinition.class);
        BoxItemDataProvider             boxItemDataProvider = mock(BoxItemDataProvider.class);
        List<Map<String, Serializable>> searchResult        = new ArrayList<>();
        Map<String, Serializable>       firstItemMap        = new HashMap<>();
        searchResult.add(firstItemMap);
        firstItemMap.put("id", "123456");

        quickFilter = new QuickFilter();

        BoxController boxController = new BoxController(boxDefinition);
        when(boxItemDataProvider.count(eq(quickFilter), eq(boxController))).thenReturn(countSize);
        when(boxItemDataProvider.search(eq(quickFilter), eq(boxController))).thenReturn(searchResult);
        when(boxItemDataProvider.getActionProvider()).thenReturn(new DefaultActionProvider());
        when(boxDefinition.getDataProvider()).thenReturn(boxItemDataProvider);


        when(singularModuleConfiguration.getBoxControllerByBoxId(eq(boxId))).thenReturn(Optional.of(boxController));


        setUpApplicationContextMock();
    }

    @Test
    public void testCount() throws Exception {
        assertThat(moduleBackstageService.count(boxId, quickFilter), Matchers.is(countSize));
    }

    @Test
    public void testSearch() throws Exception {
        assertThat(moduleBackstageService.search(boxId, quickFilter).getBoxItemDataList().size(), Matchers.is(countSize.intValue()));
    }

}