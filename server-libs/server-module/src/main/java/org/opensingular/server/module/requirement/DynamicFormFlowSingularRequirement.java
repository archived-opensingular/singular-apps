package org.opensingular.server.module.requirement;

import org.opensingular.form.SType;
import org.opensingular.server.commons.wicket.view.form.AbstractFormPage;

/**
 * Singular requirement implementation capable of dynamically resolve
 * the necessary flow.
 */
public class DynamicFormFlowSingularRequirement extends SingularRequirementAdapter {

    private Class<? extends SType<?>> form;
    private Class<? extends AbstractFormPage<?, ?>> initPage;

    public DynamicFormFlowSingularRequirement(String name, Class<? extends SType<?>> form,
                                              BoundedFlowResolver flowResolver, Class<? extends AbstractFormPage<?, ?>> initPage) {
        super(name, flowResolver);
        this.form = form;
        this.initPage = initPage;
    }

    @Override
    public final Class<? extends SType> getMainForm() {
        return form;
    }

    @Override
    public Class<? extends AbstractFormPage<?, ?>> getInitialPageClass() {
        if(initPage != null){
            return initPage;
        } else {
            return super.getInitialPageClass();
        }
    }

}