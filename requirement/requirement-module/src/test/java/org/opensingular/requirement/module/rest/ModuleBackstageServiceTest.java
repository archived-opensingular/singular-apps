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

import java.util.List;
import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.opensingular.requirement.commons.SingularCommonsBaseTest;
import org.opensingular.requirement.module.box.BoxItemDataList;
import org.opensingular.requirement.module.config.ServerContext;
import org.opensingular.requirement.module.persistence.filter.QuickFilter;
import org.opensingular.requirement.module.service.dto.BoxConfigurationData;
import org.opensingular.requirement.module.spring.security.AuthorizationService;
import org.opensingular.requirement.module.test.SingularServletContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;

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
        List<BoxConfigurationData> boxConfigurationData = moduleBackstageService.listMenu(ServerContext.REQUIREMENT.getName(), "vinicius.nunes");
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