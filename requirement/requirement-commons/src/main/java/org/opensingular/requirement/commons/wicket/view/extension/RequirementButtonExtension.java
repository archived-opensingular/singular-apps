/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.requirement.commons.wicket.view.extension;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.opensingular.form.SInstance;
import org.opensingular.lib.commons.extension.SingularExtension;
import org.opensingular.lib.commons.ui.Icon;
import org.opensingular.requirement.commons.service.RequirementInstance;

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
     * @see org.opensingular.server.commons.wicket.view.extension.RequirementButtonExtension.ButtonExtensionActionContext
     * @see org.opensingular.server.commons.wicket.view.form.AbstractFormPage
     * @see org.opensingular.server.commons.wicket.view.form.ExtensionButtonsPanel
     * @see org.opensingular.server.commons.wicket.view.form.ExtensionButtonsPanel#addButtons()
     */
    void onAction(ButtonExtensionActionContext actionContext);

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
    class ButtonExtensionActionContext {
        private final AjaxRequestTarget ajaxRequestTarget;
        private final Form<?>           formComponent;
        private final RequirementInstance petInstance;
        private final SInstance         formInstance;

        /**
         * @param ajaxRequestTarget the wicket ajax target
         * @param formComponent     the page form
         * @param petInstance       a instancia da petição
         * @param formInstance      a instancia do formulario que está sendo editado/visualizado na AbstractFormPage, não sendo necessariamente
         */
        public ButtonExtensionActionContext(AjaxRequestTarget ajaxRequestTarget, Form<?> formComponent,
                             RequirementInstance petInstance, SInstance formInstance) {
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

        public RequirementInstance getPetInstance() {
            return petInstance;
        }

        public SInstance getFormInstance() {
            return formInstance;
        }
    }

}