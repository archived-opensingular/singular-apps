package org.opensingular.server.commons.spring.security;

import org.junit.Assert;
import org.junit.Test;

public class SingularPermissionTest {

    @Test
    public void testGetSetInternalId(){
        SingularPermission permission = new SingularPermission();
        permission.setSingularId("singularId");

        Assert.assertEquals("singularId", permission.getSingularId());
        Assert.assertNull(permission.getInternalId());
    }

    @Test
    public void equalsTest(){
        SingularPermission permission = new SingularPermission("singularId", "internalId");
        SingularPermission permission2 = new SingularPermission();

        Assert.assertFalse(permission.equals(null));
        Assert.assertFalse(permission.equals("errorObject"));
        Assert.assertFalse(permission.equals(permission2));

        permission2.setSingularId("singularId");
        permission2.setInternalId("internalId");
        Assert.assertTrue(permission.equals(permission2));
        Assert.assertTrue(permission.equals(permission));
    }
}
