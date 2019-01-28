/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.requirement.module.workspace;

import org.opensingular.requirement.module.ActionProviderBuilder;
import org.opensingular.requirement.module.provider.RequirementBoxItemDataProvider;
import org.opensingular.requirement.module.provider.RequirementBoxItemDataProviderFactory;
import org.opensingular.requirement.module.service.dto.ItemBox;
import org.opensingular.requirement.module.wicket.box.DateBoxItemDataFilter;
import org.opensingular.requirement.module.wicket.box.OverflowFilter;

import javax.inject.Inject;

/**
 * A Box implementation that handles requirements, this box use {@link RequirementBoxItemDataProvider} to query the database
 */
public abstract class AbstractRequirementBoxDefinition implements BoxDefinition {
    /**
     * The ItenBox, will be created on first call to {@link BoxDefinition#getItemBox()}
     */
    private ItemBox itemBox;

    /**
     * The factory responsible for create the {@link RequirementBoxItemDataProvider}
     */
    @Inject
    private RequirementBoxItemDataProviderFactory requirementBoxItemDataProviderFactory;

    @Override
    public ItemBox getItemBox() {
        if (itemBox == null) {
            itemBox = new ItemBox()
                    .boxDefinitionClass(getClass())
                    .fieldsDatatable(getDatatableFields());
            configure(itemBox);
        }
        return itemBox;
    }

    @Override
    public RequirementBoxItemDataProvider getDataProvider() {
        ActionProviderBuilder builder = new ActionProviderBuilder();
        addActions(builder);
        RequirementBoxItemDataProvider requirementBoxItemDataProvider = requirementBoxItemDataProviderFactory.create(getItemBox().isEvalPermission(), builder);
        addFilters(requirementBoxItemDataProvider);
        return requirementBoxItemDataProvider;
    }

    private void addFilters(RequirementBoxItemDataProvider requirementBoxItemDataProvider) {
        addDateFilters(requirementBoxItemDataProvider);
        addDescriptionFilter(requirementBoxItemDataProvider);
    }

    /**
     * This method is use for include the default description formatter.
     * <p>
     * Note: This method could be overriding for change the description format.
     *
     * @param requirementBoxItemDataProvider The RequirementBoxItemDataProvider.
     */
    protected void addDescriptionFilter(RequirementBoxItemDataProvider requirementBoxItemDataProvider) {
        requirementBoxItemDataProvider.addFilter(new OverflowFilter());
    }

    /**
     * This method is use for include the default data formatter.
     * <p>
     * Note: This method could be overriding for change the date format or the date coluns.
     *
     * @param requirementBoxItemDataProvider The RequirementBoxItemDataProvider.
     */
    protected void addDateFilters(RequirementBoxItemDataProvider requirementBoxItemDataProvider) {
        requirementBoxItemDataProvider.addFilter(new DateBoxItemDataFilter());
    }

    /**
     * Add actions to the providerBuilder
     *
     * @param builder to add actions
     */
    protected abstract void addActions(ActionProviderBuilder builder);

    /**
     * Configure the ItemBox, settings things like name, description...
     *
     * @param itemBox the ItemBox that should be configured
     */
    protected abstract void configure(ItemBox itemBox);
}