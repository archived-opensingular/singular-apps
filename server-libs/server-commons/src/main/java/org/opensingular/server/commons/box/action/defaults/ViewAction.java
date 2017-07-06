package org.opensingular.server.commons.box.action.defaults;

import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.box.action.AbstractURLPopupBoxItemAction;
import org.opensingular.server.commons.form.FormAction;

public class ViewAction extends AbstractURLPopupBoxItemAction {


    public ViewAction(BoxItemData line) {
        super("view", "Visualizar", DefaultIcons.EYE, FormAction.FORM_VIEW, line);
    }
}
