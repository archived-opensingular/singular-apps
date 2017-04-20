package org.opensingular.server.module;

import org.jetbrains.annotations.NotNull;
import org.opensingular.form.SFormUtil;
import org.opensingular.form.SType;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.box.BoxItemDataImpl;
import org.opensingular.server.commons.box.action.BoxItemActionList;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.requirement.SingularRequirement;

/**
 * Solução para tornar o id do requerimento visível para as ações
 * @deprecated
 * deve ser substituido por solução onde o ID do requerimento é persistente
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
                .getRequirements()
                .stream()
                .filter(requirementRef -> isSameType(boxItemData, requirementRef))
                .forEach(requirementRef -> addRequirementeId(boxItemData, requirementRef));
    }

    private void addRequirementeId(BoxItemData boxItemData, SingularRequirementRef requirementRef) {
        ((BoxItemDataImpl) boxItemData).setRequirementId(requirementRef.getId());
    }

    private boolean isSameType(BoxItemData boxItemData, SingularRequirementRef requirementRef) {
        return getTypeName(requirementRef.getRequirement()).equals(boxItemData.getType());
    }

    @SuppressWarnings("unchecked")
    @NotNull
    private String getTypeName(SingularRequirement requirement) {
        return SFormUtil.getTypeName((Class<? extends SType<?>>) requirement.getMainForm());
    }


}
