package org.opensingular.server.module.requirement;

import org.opensingular.form.SType;

public class DynamicFormFlowSingularRequirement extends SingularRequirementAdapter {

    private Class<? extends SType<?>> form;

    public DynamicFormFlowSingularRequirement(String name, Class<? extends SType<?>> form, BoundedFlowResolver flowResolver) {
        super(name, flowResolver);
        this.form = form;
    }

    @Override
    public final Class<? extends SType> getMainForm() {
        return form;
    }


}
