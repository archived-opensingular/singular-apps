package org.opensingular.server.module.rest;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.opensingular.lib.commons.context.SingularContextSetup;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.spring.security.AuthorizationService;
import org.opensingular.server.module.BoxController;
import org.opensingular.server.module.BoxItemDataProvider;
import org.opensingular.server.module.DefaultActionProvider;
import org.opensingular.server.module.SingularModuleConfiguration;
import org.opensingular.server.module.workspace.BoxDefinition;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

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
        when(context.getBean(AuthorizationService.class)).thenReturn(authorizationService);

    }

    @Before
    public void setUp() {
        SingularContextSetup.reset();
        BoxDefinition boxDefinition = mock(BoxDefinition.class);
        BoxItemDataProvider             boxItemDataProvider = mock(BoxItemDataProvider.class);
        List<Map<String, Serializable>> searchResult        = new ArrayList<>();
        Map<String, Serializable>       firstItemMap        = new HashMap<>();
        searchResult.add(firstItemMap);
        firstItemMap.put("id", "123456");

        quickFilter = new QuickFilter();

        BoxController boxController = new BoxController(boxDefinition);
        when(boxItemDataProvider.count(eq(quickFilter), eq(boxController))).thenReturn(countSize);
        when(boxItemDataProvider.search(eq(quickFilter), eq(boxController))).thenReturn(searchResult);
        when(boxItemDataProvider.getActionProvider()).thenReturn(new DefaultActionProvider());
        when(boxDefinition.getDataProvider()).thenReturn(boxItemDataProvider);


        when(singularModuleConfiguration.getBoxControllerByBoxId(eq(boxId))).thenReturn(Optional.of(boxController));


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