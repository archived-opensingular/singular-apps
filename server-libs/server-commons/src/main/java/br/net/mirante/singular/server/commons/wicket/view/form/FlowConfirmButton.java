package br.net.mirante.singular.server.commons.wicket.view.form;

import br.net.mirante.singular.commons.util.Loggable;
import br.net.mirante.singular.form.SInstance;
import br.net.mirante.singular.form.wicket.component.SingularSaveButton;
import br.net.mirante.singular.server.commons.persistence.entity.form.PetitionEntity;
import br.net.mirante.singular.util.wicket.modal.BSModalBorder;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

public class FlowConfirmButton<T extends PetitionEntity> extends SingularSaveButton implements Loggable {

    private final AbstractFormPage<T> formPage;
    private final String              transitionName;
    private final BSModalBorder       modal;

    public FlowConfirmButton(final String transitionName,
                             final String id,
                             final IModel<? extends SInstance> model,
                             final boolean validate,
                             final AbstractFormPage<T> formPage,
                             final BSModalBorder modal) {
        super(id, model, validate);
        this.formPage = formPage;
        this.transitionName = transitionName;
        this.modal = modal;
    }

    @Override
    protected void onValidationSuccess(AjaxRequestTarget ajaxRequestTarget, Form<?> form, IModel<? extends SInstance> model) {
        try {
            formPage.executeTransition(ajaxRequestTarget, form, transitionName, model);
        } catch (Exception e) {
            getLogger().error("Erro ao salvar o XML", e);
            formPage.addToastrErrorMessage("message.save.concurrent_error");
        }
    }

    @Override
    protected void onValidationError(final AjaxRequestTarget ajaxRequestTarget,
                                     final Form<?> form,
                                     final IModel<? extends SInstance> instanceModel) {
        modal.hide(ajaxRequestTarget);
        ajaxRequestTarget.add(form);
    }

}