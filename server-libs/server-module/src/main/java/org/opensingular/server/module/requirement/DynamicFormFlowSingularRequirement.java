package org.opensingular.server.module.requirement;

import org.opensingular.form.SType;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.service.PetitionSender;
import org.opensingular.server.commons.wicket.view.form.AbstractFormPage;

/**
 * Singular requirement implementation capable of dynamically resolve
 * the necessary flow.
 */
public class DynamicFormFlowSingularRequirement extends SingularRequirementAdapter {

    private Class<? extends SType<?>> form;
    private Class<? extends AbstractFormPage<?, ?>> initPage;
    private Class<? extends PetitionSender> petitionSenderBeanClass;

    public DynamicFormFlowSingularRequirement(String name,
                                              Class<? extends SType<?>> form,
                                              BoundedFlowResolver flowResolver,
                                              Class<? extends AbstractFormPage<?, ?>> initPage,
                                              Class<? extends PetitionSender> petitionSenderBeanClass) {
        super(name, flowResolver);
        this.form = form;
        this.initPage = initPage;
        this.petitionSenderBeanClass = petitionSenderBeanClass;
    }

    @Override
    public final Class<? extends SType> getMainForm() {
        return form;
    }

    @Override
    public Class<? extends AbstractFormPage<?, ?>> getDefaultExecutionPage() {
        if (initPage != null) {
            return initPage;
        } else {
            return super.getDefaultExecutionPage();
        }
    }

    @Override
    public Class<? extends PetitionSender> getPetitionSenderBeanClass() {
        if(petitionSenderBeanClass != null) {
            return petitionSenderBeanClass;
        } else {
            return super.getPetitionSenderBeanClass();
        }
    }

}