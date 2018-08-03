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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.opensingular.requirement.commons.SingularCommonsBaseTest;
import org.opensingular.requirement.module.box.BoxItemDataMap;
import org.opensingular.requirement.module.connector.ModuleService;
import org.opensingular.requirement.module.persistence.filter.BoxFilter;
import org.opensingular.requirement.module.service.dto.ItemBox;
import org.opensingular.requirement.module.spring.security.AuthorizationService;
import org.opensingular.requirement.module.test.SingularServletContextTestExecutionListener;
import org.opensingular.requirement.module.workspace.BoxDefinition;
import org.springframework.test.context.TestExecutionListeners;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.reset;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class ModuleBackstageServiceTest extends SingularCommonsBaseTest {

    @Inject
    private ModuleService moduleService;

    @Inject
    private AuthorizationService authorizationService;

    @Before
    public void setUp() {
        reset(authorizationService);
    }

    @Test
    public void count() {
        BoxDefinition boxDefinition = Mockito.mock(BoxDefinition.class);
        Long count = moduleService.countFiltered(boxDefinition, new BoxFilter());
        assertEquals(Long.valueOf(0), count);
    }

    @Test
    public void search() {
        BoxDefinition boxDefinition = Mockito.mock(BoxDefinition.class);
        List<BoxItemDataMap> result = moduleService.searchFiltered(boxDefinition, new BoxFilter());
        assertTrue(result.isEmpty());
    }

}