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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.opensingular.lib.commons.context.SingularContextSetup;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.config.ServerContext;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.module.BoxInfo;
import org.springframework.context.ApplicationContext;

import java.util.List;

import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class PetitionBoxDefinitionDataProviderTest {

    @Mock
    private PetitionService<?, ?> petitionService;

    @InjectMocks
    private PetitionBoxItemDataProvider petitionItemBoxDataProvider = new PetitionBoxItemDataProvider();

    private QuickFilter filter;

    private Long count = 10L;
    private ApplicationContext backup;

    @Before
    public void setUp() {
        filter = new QuickFilter();
        ApplicationContext mock = Mockito.mock(ApplicationContext.class);
        Mockito.when(mock.getBean(PetitionService.class)).thenReturn(petitionService);
        SingularContextSetup.reset();
        new ApplicationContextProvider().setApplicationContext(mock);
    }

    @Test
    public void testSearch() throws Exception {
        List resultList = mock(List.class);
        when(resultList.size()).thenReturn(count.intValue());
        when(petitionService.quickSearchMap(eq(filter), anyList())).thenReturn(resultList);
        assertThat(petitionItemBoxDataProvider.search(filter, Mockito.mock(BoxInfo.class)).size(), Matchers.is(count.intValue()));

    }

    @Test
    public void testCount() throws Exception {
        when(petitionService.countQuickSearch(eq(filter), anyList())).thenReturn(count);
        assertThat(petitionItemBoxDataProvider.count(filter, Mockito.mock(BoxInfo.class)), Matchers.is(count));
    }

}