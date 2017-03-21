package org.opensingular.server.module.requirement.builder;

import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.form.SType;
import org.opensingular.server.commons.flow.FlowResolver;
import org.opensingular.server.module.requirement.BoundedFlowResolver;
import org.opensingular.server.module.requirement.DynamicFormFlowSingularRequirement;

import java.util.HashSet;
import java.util.Set;

public class SingularRequirementDefinitionFlowResolver {

    private String                    name;
    private Class<? extends SType<?>> form;
    private Set<Class<? extends ProcessDefinition>> flows = new HashSet<>();

    public SingularRequirementDefinitionFlowResolver(String name, Class<? extends SType<?>> form, Set<Class<? extends ProcessDefinition>> flows) {
        this.name = name;
        this.form = form;
        this.flows.addAll(flows);
    }

    public SingularRequirementDefinitionFlowResolver allowedFlow(Class<? extends ProcessDefinition> flowClass) {
        this.flows.add(flowClass);
        return this;
    }

    public SingularRequirementDefinitionFlow flowResolver(FlowResolver resolver) {
        return new SingularRequirementDefinitionFlow(new DynamicFormFlowSingularRequirement(name, form, new BoundedFlowResolver(resolver, flows)));
    }
}
