package org.opensingular.server.module.requirement.builder;

import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.form.SType;
import org.opensingular.server.module.requirement.BoundedFlowResolver;
import org.opensingular.server.module.requirement.DynamicFormFlowSingularRequirement;
import org.opensingular.server.module.requirement.SingularRequirement;

import java.util.HashSet;
import java.util.Set;

public class SingularRequirementDefinitionFlows {

    private String                 name;
    private Class<? extends SType<?>> form;
    private Set<Class<? extends ProcessDefinition>> flows = new HashSet<>();


    public SingularRequirementDefinitionFlows(String name, Class<? extends SType<?>> form, Class<? extends ProcessDefinition> flowClass) {
        this.name = name;
        this.form = form;
        this.flows.add(flowClass);
    }

    public SingularRequirementDefinitionFlowResolver allowedFlow(Class<? extends ProcessDefinition> flowClass) {
        this.flows.add(flowClass);
        return new SingularRequirementDefinitionFlowResolver(name, form, flows);
    }


    public SingularRequirement build() {
        return new DynamicFormFlowSingularRequirement(name, form, new BoundedFlowResolver((s, c) -> flows.stream().findFirst(), flows));
    }

}
