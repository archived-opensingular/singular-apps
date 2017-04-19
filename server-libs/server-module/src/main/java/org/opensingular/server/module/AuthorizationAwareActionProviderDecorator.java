package org.opensingular.server.module;

import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.box.factory.BoxItemActionList;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.spring.security.AuthorizationService;

public class AuthorizationAwareActionProviderDecorator implements ActionProvider {

    private ActionProvider delegate;

    public AuthorizationAwareActionProviderDecorator(ActionProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public BoxItemActionList getLineActions(BoxInfo boxInfo, BoxItemData line, QuickFilter filter) {
        BoxItemActionList actionList = delegate.getLineActions(boxInfo, line, filter);
        filterAllowedActions(actionList, line, filter);
        return actionList;
    }

    private void filterAllowedActions(BoxItemActionList actions, BoxItemData line, QuickFilter filter) {
        ApplicationContextProvider.get().getBean(AuthorizationService.class).filterActions((String) line.getType(), (Long) line.getCodPeticao(), actions.getBoxItemActions(), filter.getIdUsuarioLogado());
    }
}
