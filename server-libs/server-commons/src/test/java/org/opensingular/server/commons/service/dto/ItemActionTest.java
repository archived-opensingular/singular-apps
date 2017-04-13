package org.opensingular.server.commons.service.dto;

import org.junit.Assert;
import org.junit.Test;

public class ItemActionTest {

    @Test
    public void constructorTest(){
        ItemAction action = new ItemAction();
        ItemAction action2 = new ItemAction("name");
        Assert.assertNotEquals(action, action2);
    }

    @Test
    public void allMethodsTest(){
        ItemAction action = new ItemAction();

        action.setName("name");
        action.setLabel("label");
        action.setType(ItemActionType.ENDPOINT);
        action.setDefaultAction(false);
        action.setConfirmation(new ItemActionConfirmation());

        Assert.assertEquals("name", action.getName());
        Assert.assertEquals("label", action.getLabel());
        Assert.assertEquals(ItemActionType.ENDPOINT, action.getType());
        Assert.assertFalse(action.isDefaultAction());
        Assert.assertNotNull(action.getConfirmation());
    }
}
