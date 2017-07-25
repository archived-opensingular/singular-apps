package org.opensingular.server.module.requirement;

import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.form.SType;

import java.util.Optional;

/**
 * Singular requirement with  Single form and Single flow.
 */
public class FormFlowSingularRequirement extends DynamicFormFlowSingularRequirement {

    public FormFlowSingularRequirement(String name, Class<? extends SType<?>> form, Class<? extends FlowDefinition> flow) {
        super(name, form, new BoundedFlowResolver((c, i) -> Optional.of(flow), flow), null, null);
    }

}
