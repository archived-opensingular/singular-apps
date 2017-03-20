package org.opensingular.server.module.rest;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.module.ItemBoxDataProvider;
import org.opensingular.server.module.SingularModuleConfiguration;
import org.opensingular.server.module.box.filter.ItemBoxDataFilterCollector;
import org.opensingular.server.module.box.service.ItemBoxDataServiceImpl;
import org.opensingular.server.module.workspace.ItemBoxFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ItemBoxDataServiceTest {

    @Mock
    private SingularModuleConfiguration singularModuleConfiguration;

    @Mock
    private ItemBoxDataFilterCollector filtersFactory;

    @InjectMocks
    private ItemBoxDataServiceImpl itemBoxDataService;

    private QuickFilter quickFilter;

    private Long countSize = 1L;

    private String boxId = "123456";

    @Before
    public void setUp() {

        ItemBoxFactory itemBoxFactory = mock(ItemBoxFactory.class);
        ItemBoxDataProvider itemBoxDataProvider = mock(ItemBoxDataProvider.class);
        List searchResult = spy(new ArrayList());

        quickFilter = new QuickFilter();

        when(singularModuleConfiguration.getItemBoxFactory(eq(boxId))).thenReturn(Optional.of(itemBoxFactory));
        when(itemBoxFactory.getDataProvider()).thenReturn(itemBoxDataProvider);
        when(itemBoxDataProvider.count(eq(quickFilter))).thenReturn(countSize);
        when(itemBoxDataProvider.search(eq(quickFilter))).thenReturn(searchResult);
        when(searchResult.size()).thenReturn(countSize.intValue());
        when(filtersFactory.getFilterList(any())).thenReturn(Collections.emptyList());
    }

    @Test
    public void testCount() throws Exception {
        assertThat(itemBoxDataService.count(boxId, quickFilter), Matchers.is(countSize));
    }

    @Test
    public void testSearch() throws Exception {
        assertThat(itemBoxDataService.search(boxId, quickFilter).getItemBoxDataList().size(), Matchers.is(countSize.intValue()));
    }

}