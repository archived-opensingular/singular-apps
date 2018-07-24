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

import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.requirement.module.persistence.entity.form.RequirementDefinitionEntity;
import org.opensingular.requirement.module.wicket.view.RequirementResolverPage;

import java.util.Map;

/**
 * Defines an 'virtual' requirement that preform a pre-step to decide which requirement should be initialized
 * This kind of requirement can not be saved as a draft and it represents an atomic step.
 * The target requirement is decided by the execution of the {@link RequirementResolver#resolve(SInstance)} method.
 * In this method the SIinstance is defined byt the {@link SType} class supplied through the avaiblable constructors.
 * It is possible to pass aditional parameters (URL parameters) to the target requirement using custom {@link RequirementResolverPage}
 * and overriding the {@link RequirementResolverPage#redirectToResolvedRequirement(String, Map)}
 */
public abstract class SingularRequirementResolver extends RequirementDefinition {

    private Class<? extends RequirementResolverPage> requirementResolverPage = RequirementResolverPage.class;
    private RequirementResolver requirementResolver;

    private RequirementDefinitionEntity requirementDefinitionEntity;

    public SingularRequirementResolver(String key, RequirementResolver requirementResolver) {
        super(key);
        this.requirementResolver = requirementResolver;
    }

    public SingularRequirementResolver(String key, Class<? extends RequirementResolverPage> requirementResolverPage, RequirementResolver requirementResolver) {
        super(key);
        this.requirementResolverPage = requirementResolverPage;
        this.requirementResolver = requirementResolver;
    }


    public RequirementDefinition resolve(SIComposite instance) {
        return requirementResolver.resolve(instance);
    }

    @Override
    public Class<? extends RequirementResolverPage<?, ?>> getDefaultExecutionPage() {
        return (Class<? extends RequirementResolverPage<?, ?>>) requirementResolverPage;
    }

}