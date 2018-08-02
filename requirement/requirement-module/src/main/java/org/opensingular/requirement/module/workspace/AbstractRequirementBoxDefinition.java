package org.opensingular.requirement.module.workspace;

import org.opensingular.requirement.module.ActionProviderBuilder;
import org.opensingular.requirement.module.SingularRequirement;
import org.opensingular.requirement.module.provider.RequirementBoxItemDataProvider;
import org.opensingular.requirement.module.provider.RequirementBoxItemDataProviderFactory;
import org.opensingular.requirement.module.service.dto.ItemBox;
import org.opensingular.requirement.module.wicket.box.DateBoxItemDataFilter;

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
            itemBox = new ItemBox();
            itemBox.setBoxDefinitionClass(getClass());
            itemBox.setFieldsDatatable(getDatatableFields());
            configure(itemBox);
        }
        return itemBox;
    }

    @Override
    public RequirementBoxItemDataProvider getDataProvider() {
        ActionProviderBuilder builder = new ActionProviderBuilder();
        addActions(builder);
        RequirementBoxItemDataProvider requirementBoxItemDataProvider = requirementBoxItemDataProviderFactory.create(mustEvalPermissions(), builder);
        addDateFilters(requirementBoxItemDataProvider);
        return requirementBoxItemDataProvider;
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
     * @return a value that indicates if the database query must eval the permissions,
     * if true the {@link #getDataProvider()} result will be filtered
     */
    protected abstract Boolean mustEvalPermissions();

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