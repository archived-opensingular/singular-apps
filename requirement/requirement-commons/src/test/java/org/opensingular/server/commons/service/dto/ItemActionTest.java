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

package org.opensingular.server.commons.service.dto;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.requirement.commons.service.dto.BoxItemAction;
import org.opensingular.requirement.commons.service.dto.ItemActionConfirmation;
import org.opensingular.requirement.commons.service.dto.ItemActionType;

public class ItemActionTest {

    @Test
    public void constructorTest(){
        BoxItemAction action  = new BoxItemAction();
        BoxItemAction action2 = new BoxItemAction();
        action2.setName("name");
        Assert.assertNotEquals(action, action2);
    }

    @Test
    public void allMethodsTest(){
        BoxItemAction action = new BoxItemAction();

        action.setName("name");
        action.setLabel("label");
        action.setType(ItemActionType.EXECUTE);
        action.setDefaultAction(false);
        action.setConfirmation(new ItemActionConfirmation());

        Assert.assertEquals("name", action.getName());
        Assert.assertEquals("label", action.getLabel());
        Assert.assertEquals(ItemActionType.EXECUTE, action.getType());
        Assert.assertFalse(action.isDefaultAction());
        Assert.assertNotNull(action.getConfirmation());
    }
}
