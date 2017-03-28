package org.opensingular.server.module.requirement.builder;

import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.form.SType;
import org.opensingular.server.commons.wicket.view.form.AbstractFormPage;

import java.util.LinkedHashSet;
import java.util.Set;

public class SingularRequirementBuilderContext {

    private String name;
    private Class<? extends SType<?>> mainForm;
    private Set<Class<? extends ProcessDefinition>> flowClasses = new LinkedHashSet<>();
    private Class<? extends AbstractFormPage<?, ?>> defaultExecutionPage;

    public String getName() {
        return name;
    }

    public SingularRequirementBuilderContext setName(String name) {
        this.name = name;
        return this;
    }

    public SingularRequirementBuilderContext setMainForm(Class<? extends SType<?>> mainForm) {
        this.mainForm = mainForm;
        return this;
    }

    public Class<? extends SType<?>> getMainForm() {
        return mainForm;
    }

    public Set<Class<? extends ProcessDefinition>> getFlowClasses() {
        return flowClasses;
    }

    public SingularRequirementBuilderContext addFlowClass(Class<? extends ProcessDefinition> flowClass) {
        this.flowClasses.add(flowClass);
        return this;
    }

    public Class<? extends AbstractFormPage<?, ?>> getDefaultExecutionPage() {
        return defaultExecutionPage;
    }

    public SingularRequirementBuilderContext defaultExecutionPage(Class<? extends AbstractFormPage<?, ?>> defaultExecutionPage) {
        this.defaultExecutionPage = defaultExecutionPage;
        return this;
    }

}