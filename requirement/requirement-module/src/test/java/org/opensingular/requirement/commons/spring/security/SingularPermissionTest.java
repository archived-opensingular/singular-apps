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
import org.junit.Test;
import org.opensingular.requirement.module.spring.security.SingularPermission;

import static org.opensingular.requirement.module.spring.security.SingularPermission.SEPARATOR;
import static org.opensingular.requirement.module.spring.security.SingularPermission.WILDCARD;

public class SingularPermissionTest {

    @Test
    public void testGetSetInternalId() {
        SingularPermission permission = new SingularPermission();
        permission.setSingularId("singularId");

        Assert.assertEquals("singularId", permission.getSingularId());
        Assert.assertNull(permission.getInternalId());
    }

    @Test
    public void equalsTest() {
        SingularPermission permission  = new SingularPermission("singularId", "internalId");
        SingularPermission permission2 = new SingularPermission();

        Assert.assertFalse(permission.equals(null));
        Assert.assertFalse(permission.equals("errorObject"));
        Assert.assertFalse(permission.equals(permission2));

        permission2.setSingularId("singularId");
        permission2.setInternalId("internalId");
        Assert.assertTrue(permission.equals(permission2));
        Assert.assertTrue(permission.equals(permission));
    }


    @Test
    public void parseTest() {
        SingularPermission singularPermission = new SingularPermission("action" + SingularPermission.SEPARATOR + "form" + SingularPermission.SEPARATOR + "flow" + SingularPermission.SEPARATOR + "task", null);
        System.out.println(singularPermission.toString());

        SingularPermission boxActionPermission = new SingularPermission("box_action", null);
        System.out.println(boxActionPermission.toString());

        SingularPermission nullPermission = new SingularPermission(SEPARATOR + SEPARATOR + SEPARATOR + SEPARATOR + SEPARATOR, null);

        SingularPermission whatever = new SingularPermission(WILDCARD + SEPARATOR + WILDCARD + SEPARATOR + WILDCARD + SEPARATOR + WILDCARD, null);
        System.out.println(whatever.toString());
        SingularPermission onlyAction = new SingularPermission("action" + SEPARATOR + SEPARATOR + SEPARATOR + WILDCARD, null);
        System.out.println(onlyAction.toString());
    }

    @Test
    public void matchTest() {
        SingularPermission singularPermission = new SingularPermission("action" + SingularPermission.SEPARATOR + "form" + SingularPermission.SEPARATOR + "flow" + SingularPermission.SEPARATOR + "task", null);
        SingularPermission nullPermission     = new SingularPermission(SEPARATOR + SEPARATOR + SEPARATOR + SEPARATOR + SEPARATOR, null);
        SingularPermission whatever           = new SingularPermission(WILDCARD + SEPARATOR + WILDCARD + SEPARATOR + WILDCARD + SEPARATOR + WILDCARD, null);

        Assert.assertTrue(whatever.matchesPermission(singularPermission));
        Assert.assertTrue(singularPermission.matchesPermission(singularPermission));
        Assert.assertTrue(singularPermission.matchesPermission(whatever));
        Assert.assertTrue(whatever.matchesPermission(nullPermission));
        Assert.assertTrue(nullPermission.matchesPermission(nullPermission));
        Assert.assertTrue(whatever.matchesPermission(whatever));

        Assert.assertTrue(whatever.matchesPermission(nullPermission));
        Assert.assertFalse(singularPermission.matchesPermission(nullPermission));
        Assert.assertFalse(nullPermission.matchesPermission(singularPermission));
    }
}
