package org.opensingular.server.module.requirement.builder;

import org.opensingular.form.SType;
import org.opensingular.server.commons.wicket.view.form.AbstractFormPage;

public class SingularRequirementDefinitionForm {

    private SingularRequirementBuilderContext builderContext;

    SingularRequirementDefinitionForm(SingularRequirementBuilderContext builderContext) {
        this.builderContext = builderContext;
    }

    public SingularRequirementDefinitionForms mainForm(Class<? extends SType<?>> form) {
        return new SingularRequirementDefinitionForms(builderContext.setMainForm(form));
    }

    public SingularRequirementDefinitionForm initPage(Class<? extends AbstractFormPage<?, ?>> initPage) {
        builderContext.setInitPage(initPage);
        return this;
    }

}