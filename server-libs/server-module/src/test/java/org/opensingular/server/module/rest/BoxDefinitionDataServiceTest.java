package org.opensingular.server.module.rest;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.opensingular.lib.commons.context.SingularContext;
import org.opensingular.lib.commons.context.SingularContextSetup;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.spring.security.AuthorizationService;
import org.opensingular.server.module.BoxController;
import org.opensingular.server.module.BoxItemDataProvider;
import org.opensingular.server.module.DefaultActionProvider;
import org.opensingular.server.module.SingularModuleConfiguration;
import org.opensingular.server.module.SingularRequirementRef;
import org.opensingular.server.module.workspace.ItemBoxFactory;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class BoxDefinitionDataServiceTest {

    @Mock
    private ApplicationContext          context;
    @Mock
    private AuthorizationService        authorizationService;
    @Mock
    private SingularModuleConfiguration singularModuleConfiguration;
    @InjectMocks
    private RestBackstageService        restBackstageService;

    private QuickFilter quickFilter;

    private Long countSize = 1L;

    private String boxId = "123456";

    public void setUpApplicationContextMock() {

        ApplicationContextProvider applicationContextProvider = new ApplicationContextProvider();
        applicationContextProvider.setApplicationContext(context);
    }

    @Before
    public void setUp() {
        SingularContextSetup.reset();
        ItemBoxFactory                  itemBoxFactory      = mock(ItemBoxFactory.class);
        BoxItemDataProvider             boxItemDataProvider = mock(BoxItemDataProvider.class);
        List<Map<String, Serializable>> searchResult        = new ArrayList<>();
        Map<String, Serializable>       firstItemMap        = new HashMap<>();
        searchResult.add(firstItemMap);
        firstItemMap.put("id", "123456");

        quickFilter = new QuickFilter();

        BoxController boxController = new BoxController(itemBoxFactory);
        when(boxItemDataProvider.count(eq(quickFilter), eq(boxController))).thenReturn(countSize);
        when(boxItemDataProvider.search(eq(quickFilter), eq(boxController))).thenReturn(searchResult);
        when(boxItemDataProvider.getActionProvider()).thenReturn(new DefaultActionProvider());
        when(itemBoxFactory.getDataProvider()).thenReturn(boxItemDataProvider);


        when(singularModuleConfiguration.getBoxControllerByBoxId(eq(boxId))).thenReturn(Optional.of(boxController));

        when(singularModuleConfiguration.findRequirmentByFormType(any(String.class))).thenReturn(Optional.of(mock(SingularRequirementRef.class)));
        when(context.getBean(eq(SingularModuleConfiguration.class))).thenReturn(singularModuleConfiguration);

        when(context.getBean(eq(AuthorizationService.class))).thenReturn(authorizationService);


        setUpApplicationContextMock();
    }

    @Test
    public void testCount() throws Exception {
        assertThat(restBackstageService.count(boxId, quickFilter), Matchers.is(countSize));
    }

    @Test
    public void testSearch() throws Exception {
        assertThat(restBackstageService.search(boxId, quickFilter).getBoxItemDataList().size(), Matchers.is(countSize.intValue()));
    }

}