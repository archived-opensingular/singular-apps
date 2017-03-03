package org.opensingular.server.module.requirement;

import org.opensingular.form.SType;

import java.util.Set;

public class MultiformDynamicFlowSingularRequirement extends SingularRequirementAdapter {

    private Class<? extends SType> form;

    public MultiformDynamicFlowSingularRequirement(String name, Class<? extends SType> form, BoundedFlowResolver flowResolver) {
        super(name, flowResolver);
        this.form = form;
    }

    @Override
    public Class<? extends SType> getMainForm() {
        return form;
    }
}
