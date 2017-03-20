package org.opensingular.server.module.rest;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.module.ItemBoxDataProvider;
import org.opensingular.server.module.SingularModuleConfiguration;
import org.opensingular.server.module.box.service.ItemBoxDataService;
import org.opensingular.server.module.box.service.ItemBoxDataServiceImpl;
import org.opensingular.server.module.workspace.ItemBoxFactory;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ItemBoxDataServiceTest {

    private ItemBoxDataService itemBoxDataService;

    private QuickFilter quickFilter;

    private Long countSize = 1L;

    private String boxId = "123456";

    @Before
    public void setUp() {

        ItemBoxFactory itemBoxFactory = mock(ItemBoxFactory.class);
        SingularModuleConfiguration singularModuleConfiguration = mock(SingularModuleConfiguration.class);
        ItemBoxDataProvider itemBoxDataProvider = mock(ItemBoxDataProvider.class);
        List searchResult = mock(List.class);

        itemBoxDataService = new ItemBoxDataServiceImpl(singularModuleConfiguration);
        quickFilter = new QuickFilter();

        when(singularModuleConfiguration.getItemBoxFactory(eq(boxId))).thenReturn(Optional.of(itemBoxFactory));
        when(itemBoxFactory.getDataProvider()).thenReturn(itemBoxDataProvider);
        when(itemBoxDataProvider.count(eq(quickFilter))).thenReturn(countSize);
        when(itemBoxDataProvider.search(eq(quickFilter))).thenReturn(searchResult);
        when(searchResult.size()).thenReturn(countSize.intValue());
    }

    @Test
    public void testCount() throws Exception {
        assertThat(itemBoxDataService.count(boxId, quickFilter), Matchers.is(countSize));
    }

    @Test
    public void testSearch() throws Exception {
        assertThat(itemBoxDataService.search(boxId, quickFilter).size(), Matchers.is(countSize.intValue()));
    }

}