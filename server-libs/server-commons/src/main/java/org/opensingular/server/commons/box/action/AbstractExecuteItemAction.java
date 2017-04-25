package org.opensingular.server.commons.box.action;

import org.opensingular.lib.wicket.util.resource.SingularIcon;
import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.flow.controllers.IController;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.service.dto.ItemActionConfirmation;
import org.opensingular.server.commons.service.dto.ItemActionType;
import org.opensingular.server.commons.wicket.view.util.DispatcherPageUtil;

import static org.opensingular.server.commons.RESTPaths.EXECUTE;
import static org.opensingular.server.commons.RESTPaths.PATH_BOX_ACTION;
import static org.opensingular.server.commons.wicket.view.util.DispatcherPageParameters.FORM_NAME;
import static org.opensingular.server.commons.wicket.view.util.DispatcherPageParameters.REQUIREMENT_ID;

public abstract class AbstractExecuteItemAction extends BoxItemAction {


    public AbstractExecuteItemAction(String name, String label, SingularIcon icon, Class<? extends IController> controller, ItemActionConfirmation confirmation, BoxItemData line) {
        super(name, label, icon, ItemActionType.EXECUTE, getEndpointExecute(line), controller, confirmation);
    }

    public AbstractExecuteItemAction(String name, String label, SingularIcon icon, Class<? extends IController> controller, BoxItemData line) {
        super(name, label, icon, ItemActionType.EXECUTE, getEndpointExecute(line), controller, null);
    }

    protected static String getEndpointExecute(BoxItemData line) {
        return PATH_BOX_ACTION + EXECUTE + "?id=" + line.getPetitionId();

    }


}
