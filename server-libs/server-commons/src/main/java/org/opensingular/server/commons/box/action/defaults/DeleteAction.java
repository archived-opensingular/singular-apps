package org.opensingular.server.commons.box.action.defaults;

import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.box.action.AbstractExecuteItemAction;
import org.opensingular.server.commons.flow.controllers.DefaultDeleteController;
import org.opensingular.server.commons.service.dto.ItemActionConfirmation;

public class DeleteAction extends AbstractExecuteItemAction {


    private static final ItemActionConfirmation CONFIRMATION_DELETE = new ItemActionConfirmation("Excluir o rascunho", "Confirma a exclusão?", "Cancelar", "Remover", null);

    public DeleteAction(BoxItemData line) {
        super("delete", "Excluir", DefaultIcons.MINUS, DefaultDeleteController.class, CONFIRMATION_DELETE, line);
    }
}
