package org.opensingular.server.module.requirement;

import org.opensingular.form.SType;

import java.util.ArrayList;
import java.util.List;

public class PropertyBasedSingularRequirement extends MultiformDynamicFlowSingularRequirement {

    private List<STypeProperty> properties = new ArrayList<>();

    public PropertyBasedSingularRequirement(String name, Class<? extends SType> form, BoundedFlowResolver boundedFlowResolver) {
        super(name, form, boundedFlowResolver);
        this.properties.addAll(properties);
    }

}
