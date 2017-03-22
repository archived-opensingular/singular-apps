package org.opensingular.server.commons.box.factory;


import org.opensingular.server.commons.box.ItemBoxData;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.wicket.view.util.DispatcherPageUtil;

import java.util.ArrayList;
import java.util.List;

import static org.opensingular.server.commons.flow.actions.DefaultActions.ACTION_DELETE;
import static org.opensingular.server.commons.util.RESTPaths.*;
import static org.opensingular.server.commons.wicket.view.util.DispatcherPageParameters.FORM_NAME;

public class BoxItemActionList {

    private List<BoxItemAction> boxItemActions;

    public BoxItemActionList() {
        this.boxItemActions = new ArrayList<>();
    }

    public BoxItemActionList(BoxItemActionList boxItemActionList) {
        this.boxItemActions = new ArrayList<>(boxItemActionList.getBoxItemActions());
    }

    public BoxItemActionList addPopupBox(ItemBoxData boxData,
                                         FormAction formAction,
                                         String action) {
        String endpoint = DispatcherPageUtil
                .baseURL("")
                .formAction(formAction.getId())
                .petitionId(boxData.getCodPeticao())
                .param(FORM_NAME, boxData.getType())
                .build();

        final BoxItemAction boxItemAction = new BoxItemAction();
        boxItemAction.setName(action);
        boxItemAction.setEndpoint(endpoint);
        boxItemAction.setFormAction(formAction);

        boxItemActions.add(boxItemAction);
        return this;
    }

    public BoxItemActionList addExecuteInstante(Object id, String actionName) {
        final BoxItemAction boxItemAction = new BoxItemAction();
        final String endpointUrl = PATH_BOX_ACTION + EXECUTE + "?id=" + id;
        boxItemAction.setName(actionName);
        boxItemAction.setEndpoint(endpointUrl);
        boxItemAction.setUseExecute(true);
        boxItemActions.add(boxItemAction);
        return this;
    }

    public BoxItemActionList addDeleteAction(ItemBoxData line) {
        String endpointUrl = PATH_BOX_ACTION + DELETE + "?id=" + line.getCodPeticao();
        final BoxItemAction boxItemAction = new BoxItemAction();
        boxItemAction.setName(ACTION_DELETE.getName());
        boxItemAction.setEndpoint(endpointUrl);
        boxItemActions.add(boxItemAction);
        return this;
    }

    public BoxItemActionList add(BoxItemAction boxItemAction){
        boxItemActions.add(boxItemAction);
        return this;
    }

    public List<BoxItemAction> getBoxItemActions() {
        return boxItemActions;
    }
}