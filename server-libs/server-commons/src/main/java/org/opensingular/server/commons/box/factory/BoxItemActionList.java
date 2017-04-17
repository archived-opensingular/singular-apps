package org.opensingular.server.commons.box.factory;


import org.opensingular.form.SType;
import org.opensingular.server.commons.box.ItemBoxData;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.service.PetitionUtil;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.wicket.view.form.AbstractFormPage;
import org.opensingular.server.commons.wicket.view.util.DispatcherPageParameters;
import org.opensingular.server.commons.wicket.view.util.DispatcherPageUtil;

import java.util.ArrayList;
import java.util.List;

import static org.opensingular.server.commons.RESTPaths.DELETE;
import static org.opensingular.server.commons.RESTPaths.EXECUTE;
import static org.opensingular.server.commons.RESTPaths.PATH_BOX_ACTION;
import static org.opensingular.server.commons.flow.actions.DefaultActions.ACTION_DELETE;
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
        add(boxItemAction);
        return this;
    }

    public BoxItemActionList addExecuteInstante(Object id, String actionName) {
        final BoxItemAction boxItemAction = new BoxItemAction();
        final String        endpointUrl   = PATH_BOX_ACTION + EXECUTE + "?id=" + id;
        boxItemAction.setName(actionName);
        boxItemAction.setEndpoint(endpointUrl);
        boxItemAction.setUseExecute(true);
        add(boxItemAction);
        return this;
    }

    public BoxItemActionList addDeleteAction(ItemBoxData line) {
        String              endpointUrl   = PATH_BOX_ACTION + DELETE + "?id=" + line.getCodPeticao();
        final BoxItemAction boxItemAction = new BoxItemAction();
        boxItemAction.setName(ACTION_DELETE.getName());
        boxItemAction.setEndpoint(endpointUrl);
        add(boxItemAction);
        return this;
    }

    public BoxItemActionList add(BoxItemAction boxItemAction) {
        boxItemActions.add(boxItemAction);
        return this;
    }

    public List<BoxItemAction> getBoxItemActions() {
        return boxItemActions;
    }

    @Deprecated //TODO vinicius.nunes revisar formularios filhos de outr peticao
    public BoxItemActionList addInheritPetitionPopupBox(String actioName,
                                                        ItemBoxData line,
                                                        Class<? extends SType<?>> type,
                                                        Class<? extends AbstractFormPage<?, ?>> executioPage,
                                                        String requirementId,
                                                        boolean inheritFormData) {
        final BoxItemAction boxItemAction = new BoxItemAction();
        boxItemAction.setName(actioName);
        String url = DispatcherPageUtil
                .baseURL("")
                .formAction(FormAction.FORM_FILL.getId())
                .petitionId(null)
                .param(DispatcherPageParameters.FORM_NAME, PetitionUtil.getTypeName(type))
                .param(DispatcherPageParameters.PARENT_PETITION_ID, line.get("codPeticao"))
                .param(DispatcherPageParameters.FORM_PAGE_CLASS, executioPage.getName())
                .param(DispatcherPageParameters.REQUIREMENT_ID, requirementId)
                .param(DispatcherPageParameters.INHERIT_PARENT_FORM_DATA, inheritFormData)
                .build();
        boxItemAction.setEndpoint(url);
        add(boxItemAction);
        return this;
    }
}