package org.opensingular.server.commons.box.action;

import org.opensingular.lib.wicket.util.resource.SingularIcon;
import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.service.dto.ItemActionType;
import org.opensingular.server.commons.wicket.view.util.DispatcherPageUtil;

import static org.opensingular.server.commons.wicket.view.util.DispatcherPageParameters.FORM_NAME;
import static org.opensingular.server.commons.wicket.view.util.DispatcherPageParameters.REQUIREMENT_ID;

public abstract class AbstractURLPopupBoxItemAction extends BoxItemAction {


    public AbstractURLPopupBoxItemAction(String name, String label, SingularIcon icon, FormAction action, String endpoint) {
        super(name, label, icon, ItemActionType.URL_POPUP, action, endpoint);
    }

    public AbstractURLPopupBoxItemAction(String name, String label, SingularIcon icon, FormAction formAction, BoxItemData line) {
        this(name, label, icon, formAction, getEndpointPopUp(line, formAction));
    }

    public AbstractURLPopupBoxItemAction(String name, String label, SingularIcon icon, String endpoint) {
        this(name, label, icon, null, endpoint);
    }


    protected static String getEndpointPopUp(BoxItemData line,
                                             FormAction formAction) {
        return DispatcherPageUtil
                .baseURL("")
                .formAction(formAction.getId())
                .petitionId(line.getPetitionId())
                .param(FORM_NAME, line.getType())
                .param(REQUIREMENT_ID, line.getRequirementDefinitionId())
                .build();

    }

}
