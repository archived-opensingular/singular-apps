package org.opensingular.server.module;

import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.box.action.BoxItemActionList;
import org.opensingular.server.commons.persistence.filter.QuickFilter;

public abstract class ActionProviderDecorator implements ActionProvider {

    private ActionProvider delegate;

    public ActionProviderDecorator(ActionProvider delegate) {
        this.delegate = delegate;
    }


    public BoxItemActionList getLineActions(BoxInfo boxInfo, BoxItemData line, QuickFilter filter) {
        return decorate(delegate.getLineActions(boxInfo, line, filter), boxInfo, line, filter);
    }


    public abstract BoxItemActionList decorate(BoxItemActionList boxItemActionList, BoxInfo boxInfo, BoxItemData line, QuickFilter filter);
}
