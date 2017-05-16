package org.opensingular.singular.pet.module.foobar.stuff;


import org.opensingular.server.module.requirement.FormFlowSingularRequirement;

public class FooRequirement extends FormFlowSingularRequirement {


    public FooRequirement() {
        super("Foo requirement", STypeFoo.class, FooFlow.class);
    }
}
