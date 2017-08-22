package org.opensingular.server.commons.box.action;

import org.opensingular.lib.commons.ui.Icon;
import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.flow.controllers.IController;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.service.dto.ItemActionConfirmation;
import org.opensingular.server.commons.service.dto.ItemActionType;

import static org.opensingular.server.commons.RESTPaths.EXECUTE;
import static org.opensingular.server.commons.RESTPaths.PATH_BOX_ACTION;

public abstract class AbstractExecuteItemAction extends BoxItemAction {


    public AbstractExecuteItemAction(String name, String label, Icon icon, Class<? extends IController> controller, ItemActionConfirmation confirmation, BoxItemData line) {
        super(name, label, icon, ItemActionType.EXECUTE, getEndpointExecute(line), controller, confirmation);
    }

    public AbstractExecuteItemAction(String name, String label, Icon icon, Class<? extends IController> controller, BoxItemData line) {
        super(name, label, icon, ItemActionType.EXECUTE, getEndpointExecute(line), controller, null);
    }

    protected static String getEndpointExecute(BoxItemData line) {
        return PATH_BOX_ACTION + EXECUTE + "?id=" + line.getPetitionId();

    }


}
