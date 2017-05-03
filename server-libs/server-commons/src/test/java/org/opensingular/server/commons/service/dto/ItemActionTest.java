package org.opensingular.server.commons.service.dto;

import org.junit.Assert;
import org.junit.Test;

public class ItemActionTest {

    @Test
    public void constructorTest(){
        BoxItemAction action = new BoxItemAction();
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
