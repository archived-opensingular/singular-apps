package org.opensingular.server.module.provider;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.module.BoxInfo;

import java.util.List;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PetitionBoxDefinitionDataProviderTest {

    @Mock
    private PetitionService<?, ?> petitionService;

    @InjectMocks
    private PetitionBoxItemDataProvider petitionItemBoxDataProvider;

    private QuickFilter filter;

    private Long count = 10L;

    @Before
    public void setUp() {
        filter = new QuickFilter();
    }

    @Test
    public void testSearch() throws Exception {
        List resultList = mock(List.class);
        when(resultList.size()).thenReturn(count.intValue());
        when(petitionService.quickSearchMap(eq(filter))).thenReturn(resultList);
        assertThat(petitionItemBoxDataProvider.search(filter, Mockito.mock(BoxInfo.class)).size(), Matchers.is(count.intValue()));

    }

    @Test
    public void testCount() throws Exception {
        when(petitionService.countQuickSearch(eq(filter))).thenReturn(count);
        assertThat(petitionItemBoxDataProvider.count(filter, Mockito.mock(BoxInfo.class)), Matchers.is(count));
    }

}