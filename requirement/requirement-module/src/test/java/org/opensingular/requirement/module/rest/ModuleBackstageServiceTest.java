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
import org.opensingular.internal.lib.support.spring.injection.SingularSpringInjector;
import org.opensingular.requirement.commons.SingularCommonsBaseTest;
import org.opensingular.requirement.module.box.BoxItemDataMap;
import org.opensingular.requirement.module.connector.ModuleService;
import org.opensingular.requirement.module.persistence.filter.BoxFilter;
import org.opensingular.requirement.module.spring.security.AuthorizationService;
import org.opensingular.requirement.module.test.SingularServletContextTestExecutionListener;
import org.opensingular.requirement.module.workspace.DefaultDraftbox;
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
        DefaultDraftbox defaultDraftbox = new DefaultDraftbox();
        SingularSpringInjector.get().inject(defaultDraftbox);
        Long count = moduleService.countFiltered(defaultDraftbox, new BoxFilter());
        assertEquals(Long.valueOf(0), count);
    }

    @Test
    public void search() {
        DefaultDraftbox defaultDraftbox = new DefaultDraftbox();
        SingularSpringInjector.get().inject(defaultDraftbox);
        List<BoxItemDataMap> result = moduleService.searchFiltered(defaultDraftbox, new BoxFilter());
        assertTrue(result.isEmpty());
    }

}