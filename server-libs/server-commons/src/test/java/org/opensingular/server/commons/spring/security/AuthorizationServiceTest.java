package org.opensingular.server.commons.spring.security;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.server.commons.flow.actions.DefaultActions;
import org.opensingular.server.commons.service.dto.BoxConfigurationData;
import org.opensingular.server.commons.service.dto.BoxDefinitionData;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.service.dto.ProcessDTO;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.opensingular.server.commons.test.SingularServletContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.util.ReflectionTestUtils;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class AuthorizationServiceTest extends SingularCommonsBaseTest {

    @Inject
    private AuthorizationService authorizationService;

    @Mock
    private PermissionResolverService permissionResolverService;
    
    @Before
    public void init(){
      MockitoAnnotations.initMocks(this);
      permissionResolverService = Mockito.mock(PermissionResolverService.class);

      List<SingularPermission> list = new ArrayList<SingularPermission>();
      list.add(new SingularPermission("BOX1","1"));
      list.add(new SingularPermission("BOX2","2"));
      Mockito.when(permissionResolverService.searchPermissions("antonio")).thenReturn(list);
      ReflectionTestUtils.setField(authorizationService, "permissionResolverService", permissionResolverService);      
    }
    
    @Test
    @WithUserDetails("antonio")
    public void basicTest() {
        List<BoxConfigurationData> groupDTOs = new ArrayList<BoxConfigurationData>();
        
        BoxConfigurationData b = new BoxConfigurationData();
        b.setId("box1");
        b.setBoxesDefinition(new ArrayList<BoxDefinitionData>());
        b.setProcesses(new ArrayList<ProcessDTO>());
        groupDTOs.add(b);
        
        BoxConfigurationData b2 = new BoxConfigurationData();
        b2.setId("box2");
        b2.setBoxesDefinition(new ArrayList<BoxDefinitionData>());
        b2.setProcesses(new ArrayList<ProcessDTO>());
        groupDTOs.add(b2);
        
        String idUsuario = "antonio";
        authorizationService.filterBoxWithPermissions(groupDTOs, idUsuario);
        Assert.assertEquals(2, groupDTOs.size());
    }
    
    @Test
    @WithUserDetails("joao")
    public void withoutPermissionTest() {
        List<BoxConfigurationData> groupDTOs = new ArrayList<BoxConfigurationData>();
        
        BoxConfigurationData b = new BoxConfigurationData();
        b.setId("box1");
        b.setBoxesDefinition(new ArrayList<BoxDefinitionData>());
        b.setProcesses(new ArrayList<ProcessDTO>());
        groupDTOs.add(b);
        
        BoxConfigurationData b2 = new BoxConfigurationData();
        b2.setId("box2");
        b2.setBoxesDefinition(new ArrayList<BoxDefinitionData>());
        b2.setProcesses(new ArrayList<ProcessDTO>());
        groupDTOs.add(b2);
        
        String idUsuario = "joao";
        
        authorizationService.filterBoxWithPermissions(groupDTOs, idUsuario);
        Assert.assertEquals(0, groupDTOs.size());
    }
    
    @Test
    @WithUserDetails("joao")
    public void hasPermissionTest() {
        String idUsuario = "joao";
        Long petitionId = 1L;
        String action = DefaultActions.ACTION_ASSIGN.getName();
        
        boolean hasPermission = authorizationService.hasPermission(petitionId, null, idUsuario,
                action);
        Assert.assertFalse(hasPermission);
        
    }

    @Test
    @WithUserDetails("joao")
    public void filterActorsTest() {

        List<Actor> actors = new ArrayList<Actor>();
        actors.add(new Actor(1, "01", "torquato neto", "tn@gmail.com"));
        actors.add(new Actor(2, "02", "maria", "maria@gmail.com"));
        Long petitionId = 1L;
        String actionName = DefaultActions.ACTION_EDIT.getName();
        
        authorizationService.filterActors(actors, petitionId, actionName);
        Assert.assertEquals(0, actors.size());
    }
    

    @Test
    @WithUserDetails("antonio")
    public void filterActionsTest() {
        String idUsuario = "joao";
        String formType = null;
        Long petitionId = null;
        List<BoxItemAction> actions = new ArrayList<BoxItemAction>();
        actions.add(new BoxItemAction());
        actions.add(new BoxItemAction());
        
        authorizationService.filterActions(formType, petitionId, actions, idUsuario);
        Assert.assertEquals(0, actions.size());
    }
}
