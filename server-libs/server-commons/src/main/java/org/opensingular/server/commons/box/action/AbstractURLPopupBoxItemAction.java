package org.opensingular.server.commons.box.action;

import org.opensingular.lib.wicket.util.resource.Icon;
import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.service.dto.ItemActionType;
import org.opensingular.server.commons.wicket.view.util.DispatcherPageUtil;

import static org.opensingular.server.commons.wicket.view.util.ActionContext.FORM_NAME;
import static org.opensingular.server.commons.wicket.view.util.ActionContext.REQUIREMENT_DEFINITION_ID;

public abstract class AbstractURLPopupBoxItemAction extends BoxItemAction {


    public AbstractURLPopupBoxItemAction(String name, String label, Icon icon, FormAction action, String endpoint) {
        super(name, label, icon, ItemActionType.URL_POPUP, action, endpoint);
    }

    public AbstractURLPopupBoxItemAction(String name, String label, Icon icon, FormAction formAction, BoxItemData line) {
        this(name, label, icon, formAction, getEndpointPopUp(line, formAction));
    }

    public AbstractURLPopupBoxItemAction(String name, String label, Icon icon, String endpoint) {
        this(name, label, icon, null, endpoint);
    }


    protected static String getEndpointPopUp(BoxItemData line,
                                             FormAction formAction) {
        return DispatcherPageUtil
                .baseURL("")
                .formAction(formAction.getId())
                .petitionId(line.getPetitionId())
                .param(FORM_NAME, line.getType())
                .param(REQUIREMENT_DEFINITION_ID, line.getRequirementDefinitionId())
                .build();

    }

}
