package org.opensingular.server.commons.wicket.view.extension;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.opensingular.form.SInstance;
import org.opensingular.lib.commons.extension.SingularExtension;
import org.opensingular.lib.commons.ui.Icon;
import org.opensingular.server.commons.service.PetitionInstance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * Permite incluir um botão a todas as páginas de requerimento. Deve possui todos os membros serializaveis. pôs
 * será renderizado pelo Wicket
 * <p>
 * Como Registrar: <br />
 * {@link java.util.ServiceLoader}<br />
 * {@link org.opensingular.lib.commons.extension.SingularExtensionUtil}<br />
 * <p>
 * Classes no qual o botão será avaliado: <br />
 * {@link org.opensingular.server.commons.wicket.view.form.AbstractFormPage#buildExtensionButtons}<br />
 * {@link org.opensingular.server.commons.wicket.view.form.ExtensionButtonsPanel}<br />
 * <p>
 * Classe comum de extensões: <br />
 * {@link org.opensingular.lib.commons.extension.SingularExtension}<br />
 */
public interface RequirementButtonExtension extends SingularExtension, Serializable {
    /**
     * @see ButtonView
     */
    ButtonView getButtonView();

    /**
     * Executado quando o botão é disparado
     *
     * @see org.opensingular.server.commons.wicket.view.extension.RequirementButtonExtension.ActionContext
     * @see org.opensingular.server.commons.wicket.view.form.AbstractFormPage
     * @see org.opensingular.server.commons.wicket.view.form.ExtensionButtonsPanel
     * @see org.opensingular.server.commons.wicket.view.form.ExtensionButtonsPanel#addButtons()
     */
    void onAction(ActionContext actionContext);

    /**
     * Informa se o botão deve executr a validação quando acionado, caso sejá marcado como true só
     * ira executar o método {@link RequirementButtonExtension#onAction} caso o form esteja valido.
     *
     * @return se deve validar o formulario
     * @see org.opensingular.server.commons.wicket.view.form.ExtensionButtonsPanel#addButtons
     */
    default boolean shouldValidateForm() {
        return false;
    }

    /**
     * Dados para renderização do botão, como label e icone.
     */
    class ButtonView {
        private String label;
        private Icon   icon;

        public ButtonView(@Nonnull String label) {
            this(label, null);
        }

        public ButtonView(@Nonnull String label, @Nullable Icon icon) {
            this.icon = icon;
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public Icon getIcon() {
            return icon;
        }
    }

    /**
     * Os elementos involvidos durante a execução de uma ação
     */
    class ActionContext {
        private final AjaxRequestTarget ajaxRequestTarget;
        private final Form<?>           formComponent;
        private final PetitionInstance  petInstance;
        private final SInstance         formInstance;

        /**
         * @param ajaxRequestTarget the wicket ajax target
         * @param formComponent     the page form
         * @param petInstance       a instancia da petição
         * @param formInstance      a instancia do formulario que está sendo editado/visualizado na AbstractFormPage, não sendo necessariamente
         */
        public ActionContext(AjaxRequestTarget ajaxRequestTarget, Form<?> formComponent,
                             PetitionInstance petInstance, SInstance formInstance) {
            this.ajaxRequestTarget = ajaxRequestTarget;
            this.formComponent = formComponent;
            this.petInstance = petInstance;
            this.formInstance = formInstance;
        }

        public AjaxRequestTarget getAjaxRequestTarget() {
            return ajaxRequestTarget;
        }

        public Form<?> getFormComponent() {
            return formComponent;
        }

        public PetitionInstance getPetInstance() {
            return petInstance;
        }

        public SInstance getFormInstance() {
            return formInstance;
        }
    }

}