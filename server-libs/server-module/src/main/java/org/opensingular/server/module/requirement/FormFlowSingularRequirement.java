package org.opensingular.server.module.requirement;

import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.form.SType;
import org.opensingular.server.commons.service.PetitionSender;
import org.opensingular.server.commons.wicket.view.form.AbstractFormPage;

import java.util.Optional;

/**
 * Singular requirement with  Single form and Single flow.
 */
public class FormFlowSingularRequirement extends DynamicFormFlowSingularRequirement {

    public FormFlowSingularRequirement(String name, Class<? extends SType<?>> form, Class<? extends FlowDefinition> flow) {
        this(name, form, flow, null, null);
    }

    public FormFlowSingularRequirement(String name, Class<? extends SType<?>> form, Class<? extends FlowDefinition> flow,
                                              Class<? extends AbstractFormPage<?, ?>> initPage) {
        this(name, form, flow, initPage, null);
    }

    public FormFlowSingularRequirement(String name, Class<? extends SType<?>> form, Class<? extends FlowDefinition> flow,
                                              Class<? extends AbstractFormPage<?, ?>> initPage,
                                       Class<? extends PetitionSender> petitionSenderBeanClass) {
        super(name, form, new BoundedFlowResolver((c, i) -> Optional.of(flow), flow), initPage, petitionSenderBeanClass);
    }

}
