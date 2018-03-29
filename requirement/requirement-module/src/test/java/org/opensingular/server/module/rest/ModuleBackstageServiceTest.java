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

package org.opensingular.server.module.rest;

import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.opensingular.requirement.commons.box.BoxItemDataList;
import org.opensingular.requirement.commons.persistence.filter.QuickFilter;
import org.opensingular.requirement.commons.service.dto.BoxConfigurationData;
import org.opensingular.requirement.commons.spring.security.AuthorizationService;
import org.opensingular.requirement.module.rest.ModuleBackstageService;
import org.opensingular.requirement.commons.test.SingularServletContextTestExecutionListener;
import org.opensingular.requirement.commons.config.PServerContext;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class ModuleBackstageServiceTest extends SingularCommonsBaseTest {
    
    @Inject
    private ModuleBackstageService moduleBackstageService;

    @Inject
    private AuthorizationService authorizationService;

    @Before
    public void setUp() {
        reset(authorizationService);
    }

    @Test
    @WithUserDetails("vinicius.nunes")
    public void listMenu() {
        doNothing().when(authorizationService).filterBoxWithPermissions(any(), any());
        List<BoxConfigurationData> boxConfigurationData = moduleBackstageService.listMenu(PServerContext.REQUIREMENT.getName(), "vinicius.nunes");
        assertFalse(boxConfigurationData.isEmpty());
    }

    @Test
    public void count() {
        Long count = moduleBackstageService.count("", new QuickFilter());
        assertEquals(Long.valueOf(0), count);
    }

    @Test
    public void search() {
        BoxItemDataList search = moduleBackstageService.search("", new QuickFilter());
        assertTrue(search.getBoxItemDataList().isEmpty());
    }

}