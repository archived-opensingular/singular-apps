package org.opensingular.server.module.rest;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.module.SingularModuleConfiguration;

import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class BoxDefinitionDataServiceTest {

    @Mock
    private SingularModuleConfiguration singularModuleConfiguration;


    private QuickFilter quickFilter;

    private Long countSize = 1L;

    private String boxId = "123456";

//    @Before
//    public void setUp() {
//
//        ItemBoxFactory itemBoxFactory = mock(ItemBoxFactory.class);
//        ItemBoxDataProvider itemBoxDataProvider = mock(ItemBoxDataProvider.class);
//        List<Map<String, Serializable>> searchResult = new ArrayList<>();
//        Map<String, Serializable> firstItemMap = new HashMap<>();
//        searchResult.add(firstItemMap);
//        firstItemMap.put("id", "123456");
//
//        quickFilter = new QuickFilter();
//
//        when(singularModuleConfiguration.getItemBoxFactory(eq(boxId))).thenReturn(Optional.of(itemBoxFactory));
//        when(itemBoxFactory.getDataProvider()).thenReturn(itemBoxDataProvider);
//        when(itemBoxDataProvider.count(eq(quickFilter), this)).thenReturn(countSize);
//        when(itemBoxDataProvider.search(eq(quickFilter), this)).thenReturn(searchResult);
//        when(filtersFactory.getFilterList(any())).thenReturn(Collections.emptyList());
//    }
//
//    @Test
//    public void testCount() throws Exception {
//        assertThat(itemBoxDataService.count(boxId, quickFilter), Matchers.is(countSize));
//    }
//
//    @Test
//    public void testSearch() throws Exception {
//        assertThat(itemBoxDataService.search(boxId, quickFilter).getItemBoxDataList().size(), Matchers.is(countSize.intValue()));
//    }

}