package org.opensingular.server.commons.box.action.defaults;

import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.box.action.AbstractURLPopupBoxItemAction;
import org.opensingular.server.commons.form.FormAction;

public class EditAction extends AbstractURLPopupBoxItemAction {

    public EditAction(BoxItemData line) {
        super("edit", "Alterar", DefaultIcons.PENCIL, FormAction.FORM_FILL, line);
    }
}
