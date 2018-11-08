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

import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.lib.commons.context.spring.SpringServiceRegistry;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.builder.RequirementConfigurationBuilder;
import org.opensingular.requirement.module.builder.RequirementDefinitionConfiguration;
import org.opensingular.requirement.module.exception.SingularRequirementException;
import org.opensingular.requirement.module.persistence.entity.form.RequirementDefinitionEntity;
import org.opensingular.requirement.module.persistence.entity.form.RequirementEntity;
import org.opensingular.requirement.module.service.RequirementInstance;
import org.opensingular.requirement.module.service.RequirementService;
import org.opensingular.requirement.module.service.dto.RequirementSubmissionResponse;
import org.opensingular.requirement.module.wicket.view.form.AbstractFormPage;
import org.opensingular.requirement.module.wicket.view.form.FormPage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * Singular requirement specification.
 * This class groups the main Singular components needed to orchestrate the requirement.
 */
public abstract class RequirementDefinition<RI extends RequirementInstance> implements Loggable, Serializable {

    private final String                             key;
    private final Class<RI>                          requirementInstanceClass;
    private       RequirementDefinitionEntity        requirementDefinitionEntityEntity;
    private       RequirementDefinitionConfiguration requirementConfiguration;

    @Inject
    private RequirementService requirementService;

    @Inject
    private SpringServiceRegistry springServiceRegistry;


    /**
     * @param key Unique immutable identifier for requirement
     */
    public RequirementDefinition(String key, Class<RI> requirementInstanceClass) {
        this.key = key;
        this.requirementInstanceClass = requirementInstanceClass;
    }

    private RequirementDefinitionEntity getRequirementDefinitionEntity() {
        if (requirementDefinitionEntityEntity == null) {
            requirementDefinitionEntityEntity = requirementService.getRequirementDefinition(key);
        }
        return requirementDefinitionEntityEntity;
    }

    @PostConstruct
    private void init() {
        this.requirementConfiguration = configure(new RequirementConfigurationBuilder());
    }


    public abstract RequirementDefinitionConfiguration configure(RequirementConfigurationBuilder conf);

    private RI newRequirementInstance(RequirementEntity requirementEntity) {
        try {
            RI instance = this.requirementInstanceClass
                    .getConstructor(RequirementEntity.class, RequirementDefinition.class)
                    .newInstance(requirementEntity, this);
            springServiceRegistry.lookupSingularInjector().inject(instance);
            return instance;
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            throw new SingularRequirementException(e.getMessage());
        }
    }


    public RI newRequirement() {
        RequirementEntity requirementEntity = new RequirementEntity();
        requirementEntity.setRequirementDefinitionEntity(getRequirementDefinitionEntity());
        return newRequirementInstance(requirementEntity);
    }

    public RI newRequirement(RequirementInstance parent) {
        RI requirementInstance = newRequirement();
        requirementService.configureParentRequirement(requirementInstance, parent);
        return requirementInstance;
    }

    public RI loadRequirement(Long requirementId) {
        return (RI) newRequirementInstance(requirementService.getRequirementEntity(requirementId));
    }


    public <SI extends SInstance> Class<SType<SI>> getMainForm() {
        return (Class<SType<SI>>) requirementConfiguration.getMainForm();
    }

    public <RSR extends RequirementSubmissionResponse> RSR send(@Nonnull RI requirementInstance, @Nullable String codSubmitterActor) {
        RequirementSendInterceptor<RI, RSR> listener = requirementConfiguration.getRequirementSendInterceptor();
        springServiceRegistry.lookupSingularInjector().inject(listener);
        return requirementService.sendRequirement(requirementInstance, codSubmitterActor, listener, requirementConfiguration.getFlowDefinition());
    }

    /**
     * Returns a custom initial form page.
     * Defaults to {@link FormPage}
     *
     * @return
     */
    public Class<? extends AbstractFormPage<?>> getDefaultExecutionPage() {
        return requirementConfiguration.getExecutionPage();
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return requirementConfiguration.getName();
    }

}
