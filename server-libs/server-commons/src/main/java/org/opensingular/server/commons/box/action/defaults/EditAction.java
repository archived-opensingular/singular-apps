package org.opensingular.server.commons.box.action.defaults;

import org.opensingular.lib.wicket.util.resource.Icone;
import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.box.action.AbstractURLPopupBoxItemAction;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.service.dto.ItemActionType;

public class EditAction extends AbstractURLPopupBoxItemAction {

    public EditAction(BoxItemData line) {
        super("edit", "Alterar", Icone.PENCIL, FormAction.FORM_FILL, line);
    }
}
