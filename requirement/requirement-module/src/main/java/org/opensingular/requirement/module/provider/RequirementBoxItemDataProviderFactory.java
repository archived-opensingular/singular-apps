package org.opensingular.requirement.module.provider;


import org.opensingular.requirement.module.ActionProvider;
import org.opensingular.requirement.module.service.RequirementService;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * TODO Vinicius, devemos conversar sobre a maneira padrão de criar objetos que usam spring beans e ao
 * mesmo tempo possuem estado, acredito que essa abordagem é mais facil de refatorar
 */
@Named
public class RequirementBoxItemDataProviderFactory {
    @Inject
    private RequirementService requirementService;

    public RequirementBoxItemDataProvider create(Boolean evalPermission, ActionProvider actionProvider) {
        return new RequirementBoxItemDataProvider(evalPermission, actionProvider, requirementService);
    }
}