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
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.module.BoxInfo;
import org.springframework.context.ApplicationContext;

import java.util.List;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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
        backup = ApplicationContextProvider.get();
        filter = new QuickFilter();
        ApplicationContext mock = Mockito.mock(ApplicationContext.class);
        Mockito.when(mock.getBean(PetitionService.class)).thenReturn(petitionService);
        new ApplicationContextProvider().setApplicationContext(mock);
    }

    @After
    public void restore() {
        new ApplicationContextProvider().setApplicationContext(backup);
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