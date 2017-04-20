package org.opensingular.server.module;

import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.box.BoxItemDataImpl;
import org.opensingular.server.commons.box.action.BoxItemActionList;
import org.opensingular.server.commons.persistence.filter.QuickFilter;

/**
 * Solução para tornar o id do requerimento visível para as ações
 *
 * @deprecated deve ser substituido por solução onde o ID do requerimento é persistente
 */
public class RequirementeIdActionProviderDecorator implements ActionProvider {

    private ActionProvider delegate;


    public RequirementeIdActionProviderDecorator(ActionProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public BoxItemActionList getLineActions(BoxInfo boxInfo, BoxItemData line, QuickFilter filter) {
        populateRequirementId(boxInfo.getBoxId(), line, filter);
        return delegate.getLineActions(boxInfo, line, filter);
    }

    public void populateRequirementId(String boxId, BoxItemData boxItemData, QuickFilter filter) {
        ApplicationContextProvider.get().getBean(SingularModuleConfiguration.class)
                .findRequirmentByFormType(String.valueOf(boxItemData.getType()))
                .ifPresent(requirementRef -> this.addRequirementeId(boxItemData, requirementRef));
    }

    private void addRequirementeId(BoxItemData boxItemData, SingularRequirementRef requirementRef) {
        ((BoxItemDataImpl) boxItemData).setRequirementDefinitionId(requirementRef.getId());
    }


}
