package org.opensingular.server.module.requirement.builder;

import org.opensingular.form.SType;

public class SingularRequirementDefinitionForm {

    private String name;

    SingularRequirementDefinitionForm(String name) {
        this.name = name;
    }

    public <T extends SType> SingularRequirementDefinitionForms<T> mainForm(Class<T> form) {
        return new SingularRequirementDefinitionForms<>(name, form);
    }

}
