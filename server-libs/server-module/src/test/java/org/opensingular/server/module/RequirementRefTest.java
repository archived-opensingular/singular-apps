package org.opensingular.server.module;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.singular.pet.module.foobar.stuff.FooRequirement;
import org.opensingular.singular.pet.module.foobar.stuff.FooSingularModule;

public class RequirementRefTest {


    @Test
    public void testEquality() {
        FooSingularModule module = new FooSingularModule();

        SingularRequirementRef refBar1 = new SingularRequirementRef(module::barRequirement);
        SingularRequirementRef refBar2 = new SingularRequirementRef(module::barRequirement);

        Assert.assertEquals(refBar1, refBar2);

        SingularRequirementRef refFoo1 = new SingularRequirementRef(new FooRequirement());
        SingularRequirementRef refFoo2 = new SingularRequirementRef(new FooRequirement());

        Assert.assertEquals(refFoo1, refFoo2);
    }


    @Test
    public void testUnEquality() {
        FooSingularModule module = new FooSingularModule();

        SingularRequirementRef refBar1 = new SingularRequirementRef(module::barRequirement);
        SingularRequirementRef refFoo1 = new SingularRequirementRef(new FooRequirement());

        Assert.assertNotEquals(refBar1, refFoo1);

    }
}
