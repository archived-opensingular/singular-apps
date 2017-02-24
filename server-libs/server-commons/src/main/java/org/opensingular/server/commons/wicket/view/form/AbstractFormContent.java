/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.server.commons.wicket.view.form;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.opensingular.flow.persistence.entity.ProcessInstanceEntity;
import org.opensingular.form.RefService;
import org.opensingular.form.SInstance;
import org.opensingular.form.context.SFormConfig;
import org.opensingular.form.document.RefType;
import org.opensingular.form.document.SDocumentFactory;
import org.opensingular.form.wicket.component.SingularButton;
import org.opensingular.form.wicket.component.SingularValidationButton;
import org.opensingular.form.wicket.enums.AnnotationMode;
import org.opensingular.form.wicket.enums.ViewMode;
import org.opensingular.form.wicket.panel.SingularFormPanel;
import org.opensingular.lib.wicket.util.bootstrap.layout.BSContainer;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;
import org.opensingular.lib.wicket.util.model.IReadOnlyModel;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.wicket.view.template.Content;
import org.springframework.orm.hibernate4.HibernateOptimisticLockingFailureException;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;

public abstract class AbstractFormContent extends Content {


    protected final BSContainer<?> modalContainer = new BSContainer<>("modals");
    protected final String typeName;
    private final BSModalBorder  closeModal          = construirCloseModal();
    protected     IModel<String> msgFlowModel        = new Model<>();
    protected     IModel<String> transitionNameModel = new Model<>();
    protected final SingularFormPanel singularFormPanel;
    @Inject
    @Named("formConfigWithDatabase")
    protected SFormConfig<String>       singularFormConfig;

    public AbstractFormContent(String idWicket, String type, ViewMode viewMode, AnnotationMode annotationMode) {
        super(idWicket, false, false);
        this.typeName = type;
        this.singularFormPanel = new SingularFormPanel("singular-panel");
        this.singularFormPanel.setViewMode(viewMode);
        this.singularFormPanel.setAnnotationMode(annotationMode);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final RefType refType = singularFormConfig.getTypeLoader().loadRefTypeOrException(typeName);

        singularFormPanel.setInstanceCreator(() -> {
            SDocumentFactory extendedFactory = singularFormConfig.getDocumentFactory().extendAddingSetupStep(
                    document -> document.bindLocalService("processService", ProcessFormService.class,
                            RefService.of((ProcessFormService) () -> getProcessInstance())));
            return AbstractFormContent.this.createInstance(extendedFactory, refType);
        });

        onBuildSingularFormPanel(singularFormPanel);


        Form<?> form = new Form<>("save-form");
        form.setMultiPart(true);
        form.add(singularFormPanel);
        form.add(modalContainer);
        BSModalBorder enviarModal = buildConfirmationModal(modalContainer, getInstanceModel());
        form.add(buildSendButton(enviarModal));
        form.add(buildSaveButton());
        form.add(buildSaveAnnotationButton());
        form.add(buildFlowButtons());
        form.add(buildValidateButton());
        form.add(buildCloseButton());
        form.add(closeModal);
        form.add(buildExtraContent("extra-content"));
        add(form);
    }

    protected Component buildExtraContent(String id) {
        return new WebMarkupContainer(id).setVisible(false);
    }

    public final SInstance getInstance() {
        return singularFormPanel.getInstance();
    }

    private final ViewMode getViewMode() { return singularFormPanel.getViewMode(); }

    private final AnnotationMode getAnnotationMode() { return singularFormPanel.getAnnotationMode(); }

    private IReadOnlyModel<SInstance> getInstanceModel() {
        return (IReadOnlyModel<SInstance>) () -> singularFormPanel.getInstance();
    }

    private Component buildFlowButtons() {
        BSContainer<?> buttonContainer = new BSContainer<>("custom-buttons");
        buttonContainer.setVisible(true);

        configureCustomButtons(buttonContainer, modalContainer, getViewMode(), getAnnotationMode(), getFormInstance());

        return buttonContainer;
    }

    protected void configureCustomButtons(BSContainer<?> buttonContainer, BSContainer<?> modalContainer, ViewMode viewMode, AnnotationMode annotationMode, IModel<? extends SInstance> currentInstance) {

    }

    protected abstract SInstance createInstance(SDocumentFactory documentFactory, RefType refType);

    protected void onBuildSingularFormPanel(SingularFormPanel singularFormPanel) {

    }

    private Component buildSendButton(final BSModalBorder enviarModal) {
        final Component button = new SingularButton("send-btn", getFormInstance()) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                enviarModal.show(target);
            }

        };
        return button.add(visibleOnlyIfDraftInEditionBehaviour());
    }

    private Component buildSaveButton() {
        final Component button = new SingularButton("save-btn", getFormInstance()) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                try {
                    saveForm(getFormInstance());
                    addToastrSuccessMessage("message.success");
                    atualizarContentWorklist(target);
                } catch (HibernateOptimisticLockingFailureException e) {
                    getLogger().debug(e.getMessage());
                    addToastrErrorMessage("message.save.concurrent_error");
                }
            }
        };
        return button.add(visibleOnlyInEditionBehaviour());
    }

    protected void atualizarContentWorklist(AjaxRequestTarget target) {
        target.appendJavaScript("Singular.atualizarContentWorklist();");
    }


    private Component buildSaveAnnotationButton() {
        final Component button = new SingularValidationButton("save-annotation-btn", singularFormPanel.getInstanceModel()) {

            protected void save(AjaxRequestTarget target, IModel<? extends SInstance> instanceModel) {
                saveForm(instanceModel);
                atualizarContentWorklist(target);
            }

            @Override
            protected void onValidationSuccess(AjaxRequestTarget target, Form<?> form, IModel<? extends SInstance> instanceModel) {
                try {
                    save(target, instanceModel);
                    addToastrSuccessMessage("message.success");
                } catch (HibernateOptimisticLockingFailureException e) {
                    getLogger().debug(e.getMessage());
                    addToastrErrorMessage("message.save.concurrent_error");
                }
            }

            @Override
            protected void onValidationError(AjaxRequestTarget target, Form<?> form, IModel<? extends SInstance> instanceModel) {
                save(target, instanceModel);
            }
        };
        return button.add(visibleOnlyInAnnotationBehaviour());
    }

    @SuppressWarnings("rawtypes")
    protected AjaxLink<?> buildCloseButton() {
        return new AjaxLink("close-btn") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                if (isReadOnly()) {
                    atualizarContentWorklist(target);
                    target.appendJavaScript("window.close()");
                } else {
                    closeModal.show(target);
                }
            }
        };
    }

    private boolean isReadOnly() {
        return getViewMode() == ViewMode.READ_ONLY && getAnnotationMode() != AnnotationMode.EDIT;
    }

    protected BSModalBorder construirCloseModal() {
        BSModalBorder closeModal = new BSModalBorder("close-modal", getMessage("label.title.close.draft"));
        closeModal.addButton(BSModalBorder.ButtonStyle.CANCEL, "label.button.cancel", new AjaxButton("cancel-close-btn") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                closeModal.hide(target);
            }
        });
        closeModal.addButton(BSModalBorder.ButtonStyle.CONFIRM, "label.button.confirm", new AjaxButton("close-btn") {
            @Override
            protected String getOnClickScript() {
                return " Singular.atualizarContentWorklist();"
                        + "window.close();";
            }
        });

        return closeModal;
    }

    protected Component buildValidateButton() {
        final SingularValidationButton button = new SingularValidationButton("validate-btn", singularFormPanel.getInstanceModel()) {

            @Override
            protected void onValidationSuccess(AjaxRequestTarget target, Form<?> form, IModel<? extends SInstance> instanceModel) {
                addToastrSuccessMessage("message.validation.success");
            }

            @Override
            protected void onValidationError(AjaxRequestTarget target, Form<?> form, IModel<? extends SInstance> instanceModel) {
                super.onValidationError(target, form, instanceModel);
                addToastrErrorMessage("message.validation.error");
            }
        };

        return button.add(visibleOnlyInEditionBehaviour());
    }

    protected abstract BSModalBorder buildConfirmationModal(BSContainer<?> modalContainer, IModel<? extends SInstance> instanceModel);

    protected Behavior visibleOnlyInEditionBehaviour() {
        return $b.visibleIf(() -> getViewMode().isEdition());
    }

    protected Behavior visibleOnlyIfDraftInEditionBehaviour() {
        return $b.visibleIf(() -> !hasProcess() && getViewMode().isEdition());
    }

    protected Behavior visibleOnlyInAnnotationBehaviour() {
        return $b.visibleIf(() -> getAnnotationMode().editable());
    }

    protected IModel<? extends SInstance> getFormInstance() {
        return singularFormPanel.getInstanceModel();
    }

    protected abstract ProcessInstanceEntity getProcessInstance();

    protected abstract void saveForm(IModel<? extends SInstance> currentInstance);

    protected abstract IModel<? extends PetitionEntity> getFormModel();

    protected abstract boolean hasProcess();

    protected abstract String getIdentifier();

    public final SingularFormPanel getSingularFormPanel() {
        return singularFormPanel;
    }

    @FunctionalInterface
    public interface ProcessFormService extends Serializable {
        ProcessInstanceEntity getProcessInstance();
    }
}
