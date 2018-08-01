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

package org.opensingular.requirement.commons.spring.security;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.opensingular.requirement.commons.SingularCommonsBaseTest;
import org.opensingular.requirement.module.box.BoxItemDataImpl;
import org.opensingular.requirement.module.box.action.BoxItemActionList;
import org.opensingular.requirement.module.box.action.defaults.AssignAction;
import org.opensingular.requirement.module.service.dto.BoxConfigurationData;
import org.opensingular.requirement.module.service.dto.BoxItemAction;
import org.opensingular.requirement.module.spring.security.AuthorizationService;
import org.opensingular.requirement.module.test.SingularServletContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;

import javax.inject.Inject;
import java.util.ArrayList;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class AuthorizationServiceImplTest extends SingularCommonsBaseTest {

    @Inject
    private AuthorizationService authorizationService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    @WithUserDetails("toim")
    public void basicTest() {
        String idUsuario = "toim";
        Assert.assertFalse(authorizationService.hasPermission(idUsuario, ""));
    }

    @Test
    @WithUserDetails("joao")
    public void withoutPermissionTest() {
        BoxConfigurationData b = new BoxConfigurationData();
        b.setBoxesDefinition(new ArrayList<>());
        String idUsuario = "joao";
        Assert.assertFalse(authorizationService.hasPermission(idUsuario, ""));
    }

    @Test
    @WithUserDetails("joao")
    public void hasPermissionTest() {
        String idUsuario = "joao";
        Long requirementId = 1L;
        String action = new AssignAction(new BoxItemDataImpl()).getName();

        boolean hasPermission = authorizationService.hasPermission(requirementId, null, idUsuario,
                action);
        Assert.assertFalse(hasPermission);

    }

    @Test
    @WithUserDetails("joao")
    public void filterActionsTest() {
        String idUsuario = "joao";
        String formType = null;
        Long requirementId = null;
        BoxItemActionList actions = new BoxItemActionList();
        actions.add(new BoxItemAction());
        actions.add(new BoxItemAction());

        authorizationService.filterActions(formType, requirementId, actions, idUsuario);
        Assert.assertEquals(0, actions.size());
    }
}
