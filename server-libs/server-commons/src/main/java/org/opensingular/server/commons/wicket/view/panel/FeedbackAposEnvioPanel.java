package org.opensingular.server.commons.wicket.view.panel;


import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.opensingular.lib.commons.lambda.IBiConsumer;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;
import org.opensingular.lib.wicket.util.util.Shortcuts;
import org.opensingular.server.commons.service.dto.PetitionSendedFeedback;

import javax.annotation.Nonnull;
import java.util.Optional;

public abstract class FeedbackAposEnvioPanel extends Panel {

    protected BSModalBorder modal = new BSModalBorder("modal-panel");
    private IBiConsumer<AjaxRequestTarget, BSModalBorder> onClose;

    public FeedbackAposEnvioPanel(String id) {
        super(id);
        build();
    }

    private void build() {
        addTitle();
        addModalButtons();
        configureModalSize();
        add(modal);
    }

    protected void addModalButtons() {
        addCloseButton();
    }

    public void show(AjaxRequestTarget target, PetitionSendedFeedback sendedFeedback) {
        show(target, sendedFeedback, null);
    }

    public void show(AjaxRequestTarget target, PetitionSendedFeedback sendedFeedback, IBiConsumer<AjaxRequestTarget, BSModalBorder> onClose) {
        this.onClose = onClose;
        modal.show(target);
    }

    private void addCloseButton() {
        modal.addButton(BSModalBorder.ButtonStyle.CANCEL, Shortcuts.$m.ofValue("Fechar"), new AjaxButton("close-button") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                getOnClose().ifPresent(f -> f.accept(target, modal));
                target.appendJavaScript("; window.close();");
            }
        });
    }

    private Optional<IBiConsumer<AjaxRequestTarget, BSModalBorder>> getOnClose() {
        return Optional.ofNullable(onClose);
    }

    private void addTitle() {
        modal.setTitleText(Model.of(getTitle()));
    }

    @Nonnull
    public String getTitle() {
        return "Requerimento enviado com sucesso.";
    }

    private void configureModalSize() {
        modal.setSize(BSModalBorder.Size.NORMAL);
    }

}