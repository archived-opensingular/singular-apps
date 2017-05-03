package org.opensingular.server.module.requirement;

import org.opensingular.server.commons.requirement.SingularRequirement;

/**
 * Basic singular requirement adapter.
 */
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
