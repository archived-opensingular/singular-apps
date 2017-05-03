package org.opensingular.singular.pet.module.foobar.stuff;

import org.opensingular.server.module.RequirementConfiguration;
import org.opensingular.server.module.SingularModule;
import org.opensingular.server.module.WorkspaceConfiguration;
import org.opensingular.server.commons.requirement.SingularRequirement;
import org.opensingular.server.module.requirement.builder.SingularRequirementBuilder;
import org.opensingular.server.module.workspace.DefaultDonebox;
import org.opensingular.server.module.workspace.DefaultInbox;

public class FooSingularModule implements SingularModule {

    private FooRequirement fooRequirement = new FooRequirement();

    @Override
    public String category() {
        return "Foo";
    }

    @Override
    public String name() {
        return "Foozin";
    }

    @Override
    public void requirements(RequirementConfiguration config) {
        config
                .addRequirement(fooRequirement)
                .addRequirement(this::barRequirement);
    }

    @Override
    public void workspace(WorkspaceConfiguration config) {
        config
                .addBox(new DefaultInbox()).newFor(this::barRequirement)
                .addBox(new DefaultDonebox()).newFor(fooRequirement);
    }


    public SingularRequirement barRequirement(SingularRequirementBuilder builder) {
        return builder
                .name("Bar Requirement")
                .mainForm(STypeFoo.class)
                .allowedFlow(FooFlow.class)
                .build();
    }
}
