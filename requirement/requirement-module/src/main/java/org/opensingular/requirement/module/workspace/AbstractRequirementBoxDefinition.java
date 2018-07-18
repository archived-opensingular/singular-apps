package org.opensingular.requirement.module.workspace;

import org.opensingular.requirement.module.ActionProvider;
import org.opensingular.requirement.module.provider.RequirementBoxItemDataProvider;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * BOX que lida especificamente com requerimentos
 */
public abstract class AbstractRequirementBoxDefinition implements BoxDefinition {

    private Provider<RequirementBoxItemDataProvider> requirementBoxItemDataProviderObjectFactory;

    @Override
    public RequirementBoxItemDataProvider getDataProvider() {
        RequirementBoxItemDataProvider requirementBoxItemDataProvider = requirementBoxItemDataProviderObjectFactory.get();
        requirementBoxItemDataProvider.setEvalPermissions(shouldEvalPermissions());
        requirementBoxItemDataProvider.setActionProvider(actionProvider());
        return requirementBoxItemDataProvider;
    }

    protected abstract Boolean shouldEvalPermissions();

    protected abstract ActionProvider actionProvider();

    @Inject
    public void setRequirementBoxItemDataProviderObjectFactory(Provider<RequirementBoxItemDataProvider>
                                                                       requirementBoxItemDataProviderObjectFactory) {
        this.requirementBoxItemDataProviderObjectFactory = requirementBoxItemDataProviderObjectFactory;
    }
}