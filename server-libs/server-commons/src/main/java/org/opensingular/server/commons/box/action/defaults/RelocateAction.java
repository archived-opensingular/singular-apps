package org.opensingular.server.commons.box.action.defaults;

import org.opensingular.lib.wicket.util.resource.Icone;
import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.box.action.AbstractExecuteItemAction;
import org.opensingular.server.commons.flow.controllers.DefaultDeleteController;
import org.opensingular.server.commons.service.dto.ItemActionConfirmation;

import static org.opensingular.server.commons.RESTPaths.USERS;

public class RelocateAction extends AbstractExecuteItemAction {


    private static final ItemActionConfirmation CONFIRMATION_RELOCATE = new ItemActionConfirmation("Realocar", "Usuário:", "Cancelar", "Realocar", USERS);


    public RelocateAction(BoxItemData line) {
        super("relocate", "Realocar", Icone.SHARE_SQUARE, null, CONFIRMATION_RELOCATE, line);
    }
}
