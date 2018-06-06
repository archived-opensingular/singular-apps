/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.requirement.module;

import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.requirement.commons.SingularRequirement;
import org.opensingular.requirement.commons.exception.SingularRequirementException;
import org.opensingular.requirement.commons.flow.FlowResolver;
import org.opensingular.requirement.commons.service.RequirementInstance;
import org.opensingular.requirement.commons.service.RequirementSender;
import org.opensingular.requirement.commons.service.dto.RequirementSenderFeedback;
import org.opensingular.requirement.commons.wicket.view.form.FormPageExecutionContext;
import org.opensingular.requirement.module.wicket.view.RequirementResolverPage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

/**
 * Defines an 'virtual' requirement that preform a pre-step to decide which requirement should be initialized
 * This kind of requirement can not be saved as a draft and it represents an atomic step.
 * The target requirement is decided by the execution of the {@link RequirementResolver#resolve(SInstance)} method.
 * In this method the SIinstance is defined byt the {@link SType} class supplied through the avaiblable constructors.
 * It is possible to pass aditional parameters (URL parameters) to the target requirement using custom {@link RequirementResolverPage}
 * and overriding the {@link RequirementResolverPage#redirectToResolvedRequirement(Long, Map)}
 */
public class SingularRequirementResolver implements SingularRequirement {

    private       String                 name;
    private final Class<? extends SType> preRequirementSelectionForm;
    private Class<? extends RequirementResolverPage> requirementResolverPage = RequirementResolverPage.class;
    private RequirementResolver requirementResolver;

    public SingularRequirementResolver(String name, Class<? extends SType> requirementResolverForm, RequirementResolver requirementResolver) {
        this.name = name;
        this.preRequirementSelectionForm = requirementResolverForm;
        this.requirementResolver = requirementResolver;
    }

    public SingularRequirementResolver(String name, Class<? extends SType> preRequirementSelectionForm, Class<? extends RequirementResolverPage> requirementResolverPage, RequirementResolver requirementResolver) {
        this.name = name;
        this.preRequirementSelectionForm = preRequirementSelectionForm;
        this.requirementResolverPage = requirementResolverPage;
        this.requirementResolver = requirementResolver;
    }


    public SingularRequirement resolve(SIComposite instance) {
        return requirementResolver.resolve(instance);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<? extends SType> getMainForm() {
        return preRequirementSelectionForm;
    }

    @Override
    public final BoundedFlowResolver getFlowResolver() {
        return new ResolveNotAllowedFlowResolver();
    }

    @Override
    public Class<? extends RequirementResolverPage<?, ?>> getDefaultExecutionPage() {
        return (Class<? extends RequirementResolverPage<?, ?>>) requirementResolverPage;
    }

    @Override
    public final Class<? extends RequirementSender> getRequirementSenderBeanClass() {
        return SendNotAllowedRequirementSender.class;
    }

    public static class ResolveNotAllowedFlowResolver extends BoundedFlowResolver {

        public ResolveNotAllowedFlowResolver() {
            super(new FlowResolver() {
                @Override
                public Optional<Class<? extends FlowDefinition>> resolve(FormPageExecutionContext cfg, SIComposite iRoot) {
                    throw new SingularRequirementException("There is no FlowResolver definition for a " + SingularRequirementResolver.class.getSimpleName() + " definition ");
                }
            });
        }
    }

    public static class SendNotAllowedRequirementSender implements RequirementSender {
        @Nonnull
        @Override
        public RequirementSenderFeedback send(@Nonnull RequirementInstance requirement, SInstance instance, @Nullable String codSubmitterActor) {
            throw new SingularRequirementException("There is no RequirementSender definition for a " + SingularRequirementResolver.class.getSimpleName() + " definition ");
        }
    }
}
