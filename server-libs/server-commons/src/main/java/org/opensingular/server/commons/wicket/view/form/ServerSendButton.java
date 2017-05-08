package org.opensingular.server.commons.wicket.view.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.opensingular.form.SInstance;
import org.opensingular.form.wicket.component.SingularButton;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;

public class ServerSendButton extends SingularButton {

    private final BSModalBorder sendModal;

    public ServerSendButton(String id, IModel<? extends SInstance> currentInstance, BSModalBorder sendModal) {
        super(id, currentInstance);
        this.sendModal = sendModal;
    }

    @Override
    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
        super.onSubmit(target, form);
        sendModal.show(target);
    }

}
