package org.opensingular.server.module.requirement;

public abstract class SingularRequirementAdapter implements SingularRequirement {

    private String              name;
    private BoundedFlowResolver flowResolver;

    public SingularRequirementAdapter(String name, BoundedFlowResolver flowResolver) {
        this.name = name;
        this.flowResolver = flowResolver;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BoundedFlowResolver getFlowResolver() {
        return flowResolver;
    }


}
