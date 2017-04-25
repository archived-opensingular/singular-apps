package org.opensingular.server.commons.box.action.defaults;

import org.opensingular.lib.wicket.util.resource.Icone;
import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.box.action.AbstractExecuteItemAction;
import org.opensingular.server.commons.flow.controllers.DefaultAssignController;
import org.opensingular.server.commons.flow.controllers.DefaultDeleteController;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.service.dto.ItemActionConfirmation;
import org.opensingular.server.commons.service.dto.ItemActionType;

public class AssignAction extends AbstractExecuteItemAction {

    public AssignAction(BoxItemData line) {
        super("assign", "Atribuir", Icone.ARROW_DOWN, DefaultAssignController.class, line);
    }
}
