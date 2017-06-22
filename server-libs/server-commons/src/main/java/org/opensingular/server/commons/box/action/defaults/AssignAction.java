package org.opensingular.server.commons.box.action.defaults;

import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.box.action.AbstractExecuteItemAction;
import org.opensingular.server.commons.flow.controllers.DefaultAssignController;

public class AssignAction extends AbstractExecuteItemAction {

    public AssignAction(BoxItemData line) {
        super("assign", "Atribuir", DefaultIcons.ARROW_DOWN, DefaultAssignController.class, line);
    }
}
