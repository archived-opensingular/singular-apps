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

import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.exception.SingularRequirementException;
import org.opensingular.requirement.module.persistence.entity.form.RequirementEntity;
import org.opensingular.requirement.module.service.RequirementInstance;
import org.opensingular.requirement.module.service.RequirementService;
import org.opensingular.requirement.module.wicket.view.form.AbstractFormPage;
import org.opensingular.requirement.module.wicket.view.form.FormPage;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Singular requirement specification.
 * This class groups the main Singular components needed to orchestrate the requirement.
 */
public abstract class RequirementDefinition<RI extends RequirementInstance> implements Loggable {

    private final String    key;
    private final Class<RI> requirementInstanceClass;

    @Inject
    private RequirementService                 requirementService;

    private RequirementDefinitionConfiguration requirementConfiguration;

    /**
     * @param key Unique immutable identifier for requirement
     */
    public RequirementDefinition(String key) {
        this(key, (Class<RI>) RequirementInstance.class);
    }

    public RequirementDefinition(String key, Class<RI> requirementInstanceClass) {
        this.key = key;
        this.requirementInstanceClass = requirementInstanceClass;
    }

    @PostConstruct
    private void init() {
        this.requirementConfiguration = configure(new RequirementConfigurationBuilder());
    }


    public abstract RequirementDefinitionConfiguration configure(RequirementConfigurationBuilder conf);

    private RI newRequirementInstance(@Nonnull RequirementEntity requirementEntity) {
        try {
            return this.requirementInstanceClass.getConstructor(RequirementEntity.class).newInstance(requirementEntity);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            throw new SingularRequirementException(e.getMessage());
        }
    }


    public RI newRequirement() {
        return newRequirementInstance(new RequirementEntity());
    }

    public RI newRequirement(RequirementInstance parent) {
        RI requirementInstance = newRequirementInstance(new RequirementEntity());
        requirementService.configureParentRequirement(requirementInstance, parent);
        return requirementInstance;
    }

    public RI loadRequirement(Long requirementId) {
        return (RI) newRequirementInstance(requirementService.getRequirementEntity(requirementId));
    }


    /**
     * Returns a custom initial form page.
     * Defaults to {@link FormPage}
     *
     * @return
     */
    public Class<? extends AbstractFormPage<?, ?>> getDefaultExecutionPage() {
        return FormPage.class;
    }

    public String getKey() {
        return key;
    }


}
