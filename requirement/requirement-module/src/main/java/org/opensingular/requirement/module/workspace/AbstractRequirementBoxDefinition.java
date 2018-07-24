package org.opensingular.requirement.module.workspace;

import org.opensingular.requirement.module.ActionProvider;
import org.opensingular.requirement.module.provider.RequirementBoxItemDataProvider;
import org.opensingular.requirement.module.provider.RequirementBoxItemDataProviderFactory;

import javax.inject.Inject;

/**
 * BOX que lida especificamente com requerimentos
 */
public abstract class AbstractRequirementBoxDefinition implements BoxDefinition {
    @Inject
    private RequirementBoxItemDataProviderFactory requirementBoxItemDataProviderFactory;

    @Override
    public RequirementBoxItemDataProvider getDataProvider() {
        return requirementBoxItemDataProviderFactory.create(shouldEvalPermissions(), actionProvider());
    }

    protected abstract Boolean shouldEvalPermissions();

    protected abstract ActionProvider actionProvider();
}