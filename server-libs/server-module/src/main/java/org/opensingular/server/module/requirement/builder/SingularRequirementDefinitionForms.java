package org.opensingular.server.module.requirement.builder;

import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.form.SType;

public class SingularRequirementDefinitionForms<T extends SType<?>> {

    private String                 name;
    private Class<? extends SType<?>> form;

    SingularRequirementDefinitionForms(String name, Class<T> form) {
        this.name = name;
        this.form = form;
    }

    public SingularRequirementDefinitionFlows allowedFlow(Class<? extends ProcessDefinition> flowClass) {
        return new SingularRequirementDefinitionFlows(name, form, flowClass);
    }


}
