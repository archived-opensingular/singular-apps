package org.opensingular.server.commons.box.factory;


import org.opensingular.server.commons.box.ItemBoxData;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.rest.DefaultServerREST;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.wicket.view.util.DispatcherPageUtil;

import java.util.ArrayList;
import java.util.List;

import static org.opensingular.server.commons.flow.actions.DefaultActions.ACTION_DELETE;
import static org.opensingular.server.commons.rest.DefaultServerREST.PATH_BOX_ACTION;
import static org.opensingular.server.commons.util.DispatcherPageParameters.FORM_NAME;

public class BoxItemActionsBuilder {

    private List<BoxItemAction> boxItemActions;

    public BoxItemActionsBuilder() {
        this.boxItemActions = new ArrayList<>();
    }

    public BoxItemActionsBuilder(List<BoxItemAction> configuredActions) {
        this.boxItemActions = new ArrayList<>(configuredActions);
    }

    public BoxItemActionsBuilder addPopupBox(ItemBoxData boxData,
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

    public BoxItemActionsBuilder addExecuteInstante(Object id, String actionName) {
        final BoxItemAction boxItemAction = new BoxItemAction();
        final String endpointUrl = PATH_BOX_ACTION + DefaultServerREST.EXECUTE + "?id=" + id;
        boxItemAction.setName(actionName);
        boxItemAction.setEndpoint(endpointUrl);
        boxItemAction.setUseExecute(true);
        boxItemActions.add(boxItemAction);
        return this;
    }

    public BoxItemActionsBuilder addDeleteAction(ItemBoxData line) {
        String endpointUrl = DefaultServerREST.PATH_BOX_ACTION + DefaultServerREST.DELETE + "?id=" + line.getCodPeticao();
        final BoxItemAction boxItemAction = new BoxItemAction();
        boxItemAction.setName(ACTION_DELETE.getName());
        boxItemAction.setEndpoint(endpointUrl);
        boxItemActions.add(boxItemAction);
        return this;
    }

    public List<BoxItemAction> build() {
        return boxItemActions;
    }


}