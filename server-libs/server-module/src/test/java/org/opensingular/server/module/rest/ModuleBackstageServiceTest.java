package org.opensingular.server.module.rest;

import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.opensingular.server.commons.box.BoxItemDataList;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.dto.BoxConfigurationData;
import org.opensingular.server.commons.spring.security.AuthorizationService;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.opensingular.server.commons.test.SingularServletContextTestExecutionListener;
import org.opensingular.server.p.commons.config.PServerContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class ModuleBackstageServiceTest extends SingularCommonsBaseTest {
    
    @Inject
    private ModuleBackstageService moduleBackstageService;

    @Inject
    private AuthorizationService authorizationService;

    @Before
    public void setUp() {
        reset(authorizationService);
    }

    @Test
    @WithUserDetails("vinicius.nunes")
    public void listMenu() {
        doNothing().when(authorizationService).filterBoxWithPermissions(any(), any());
        List<BoxConfigurationData> boxConfigurationData = moduleBackstageService.listMenu(PServerContext.REQUIREMENT.getName(), "vinicius.nunes");
        assertFalse(boxConfigurationData.isEmpty());
    }

    @Test
    public void count() {
        Long count = moduleBackstageService.count("", new QuickFilter());
        assertEquals(Long.valueOf(0), count);
    }

    @Test
    public void search() {
        BoxItemDataList search = moduleBackstageService.search("", new QuickFilter());
        assertTrue(search.getBoxItemDataList().isEmpty());
    }

}