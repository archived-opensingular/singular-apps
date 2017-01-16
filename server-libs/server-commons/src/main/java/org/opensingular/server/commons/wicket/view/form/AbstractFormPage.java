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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.opensingular.flow.core.MTransition;
import org.opensingular.flow.persistence.entity.ProcessInstanceEntity;
import org.opensingular.flow.persistence.entity.TaskDefinitionEntity;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.context.SFormConfig;
import org.opensingular.form.document.RefType;
import org.opensingular.form.document.SDocumentFactory;
import org.opensingular.form.persistence.FormKey;
import org.opensingular.form.persistence.entity.FormEntity;
import org.opensingular.form.persistence.entity.FormTypeEntity;
import org.opensingular.form.service.IFormService;
import org.opensingular.form.wicket.component.SingularButton;
import org.opensingular.form.wicket.component.SingularSaveButton;
import org.opensingular.form.wicket.enums.AnnotationMode;
import org.opensingular.form.wicket.enums.ViewMode;
import org.opensingular.form.wicket.panel.SingularFormPanel;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.wicket.util.bootstrap.layout.BSContainer;
import org.opensingular.lib.wicket.util.bootstrap.layout.TemplatePanel;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.exception.SingularServerFormValidationError;
import org.opensingular.server.commons.flow.metadata.ServerContextMetaData;
import org.opensingular.server.commons.persistence.entity.form.DraftEntity;
import org.opensingular.server.commons.persistence.entity.form.FormPetitionEntity;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.service.FormPetitionService;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.util.DispatcherPageParameters;
import org.opensingular.server.commons.wicket.SingularSession;
import org.opensingular.server.commons.wicket.builder.MarkupCreator;
import org.opensingular.server.commons.wicket.view.template.Content;
import org.opensingular.server.commons.wicket.view.template.Template;
import org.opensingular.server.commons.wicket.view.util.DispatcherPageUtil;

import javax.inject.Inject;
import javax.inject.Named;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;
import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

public abstract class AbstractFormPage<T extends PetitionEntity> extends Template implements Loggable {

    @Inject
    protected PetitionService<T> petitionService;

    @Inject
    protected IFormService formService;

    @Inject
    protected FormPetitionService<T> formPetitionService;

    @Inject
    @Named("formConfigWithDatabase")
    protected SFormConfig<String> singularFormConfig;

    protected final Class<T>            petitionClass;
    protected final FormPageConfig      config;
    protected       IModel<T>           currentModel;
    protected final IModel<FormKey>     formModel;
    protected final IModel<FormKey>     parentPetitionformModel;
    protected       AbstractFormContent content;


    public AbstractFormPage(Class<T> petitionClass, FormPageConfig config) {
        if (config == null) {
            throw new RedirectToUrlException("/singular");
        }
        this.petitionClass = Objects.requireNonNull(petitionClass);
        this.config = Objects.requireNonNull(config);
        this.formModel = $m.ofValue();
        this.parentPetitionformModel = $m.ofValue();
        Objects.requireNonNull(getFormType(config));
    }

    @Override
    protected boolean withMenu() {
        return false;
    }

    @Override
    protected void onInitialize() {
        final T petition;
        petition = loadPetition();

        currentModel = $m.loadable(() -> petition != null && petition.getCod() != null ? petitionService.findPetitionByCod(petition.getCod()) : petition);
        currentModel.setObject(petition);

        super.onInitialize();
    }

    private T loadPetition() {
        T petition;
        if (StringUtils.isNotBlank(config.getPetitionId())) {
            petition = petitionService.findPetitionByCod(Long.valueOf(config.getPetitionId()));
            if (petition != null && petition.getCod() != null) {
                final FormEntity formEntityDraftOrPetition = getDraftOrFormEntity(petition);
                if (formEntityDraftOrPetition != null) {
                    formModel.setObject(formService.keyFromObject(formEntityDraftOrPetition.getCod()));
                }
            }
        } else {
            petition = petitionService.createNewPetitionWithoutSave(petitionClass, config, this::onNewPetitionCreation);
        }

        if (StringUtils.isNotBlank(config.getParentPetitionId())) {
            defineParentPetition(petition);
        }
        return petition;
    }

    private void defineParentPetition(T petition) {
    /* carrega a chave do form da petição pai para posterior clonagem */
        T parentPetition = petitionService.findPetitionByCod(Long.valueOf(config.getParentPetitionId()));
        if (parentPetition != null && parentPetition.getMainForm() != null) {
            parentPetitionformModel.setObject(formService.keyFromObject(parentPetition.getMainForm().getCod()));
        }
        if (petition != null) {
            petition.setParentPetition(parentPetition);
            if (parentPetition != null) {
                if (parentPetition.getRootPetition() != null) {
                    petition.setRootPetition(parentPetition.getRootPetition());
                } else {
                    petition.setRootPetition(parentPetition);
                }
            }
        }
    }


    private FormEntity getDraftOrFormEntity(T petition) {
        return Optional
                .ofNullable(petition.currentEntityDraftByType(getFormType(config)))
                .map(DraftEntity::getForm)
                .orElse(getFormPetitionEntity(petition).map(FormPetitionEntity::getForm).orElse(null));
    }

    public Optional<FormPetitionEntity> getFormPetitionEntity(T petition) {
        if (isMainForm()) {
            return formPetitionService.findFormPetitionEntityByTypeName(petition.getCod(), getFormType(config));
        } else {
            return formPetitionService.findFormPetitionEntityByTypeNameAndTask(petition.getCod(), getFormType(config),
                    getCurrentTaskDefinition(petition).map(TaskDefinitionEntity::getCod).orElse(null));
        }
    }

    private Optional<TaskDefinitionEntity> getCurrentTaskDefinition(T petition) {
        final ProcessInstanceEntity processInstanceEntity = petition.getProcessInstanceEntity();
        if (processInstanceEntity != null) {
            return Optional.of(processInstanceEntity.getCurrentTask().getTask().getTaskDefinition());
        }
        return Optional.empty();
    }

    @Override
    protected Content getContent(String id) {

        if (getFormType(config) == null && config.getPetitionId() == null) {
            String urlServidorSingular = SingularProperties.get().getProperty(SingularProperties.SINGULAR_SERVER_ADDR);
            throw new RedirectToUrlException(urlServidorSingular);
        }

        content = new AbstractFormContent(id, getFormType(config), getViewMode(config), getAnnotationMode(config)) {

            @Override
            protected SInstance createInstance(SDocumentFactory documentFactory, RefType refType) {
                return AbstractFormPage.this.createInstance(documentFactory, refType);
            }

            @Override
            protected IModel<?> getContentTitleModel() {
                return AbstractFormPage.this.getContentTitleModel();
            }

            @Override
            protected IModel<?> getContentSubtitleModel() {
                return AbstractFormPage.this.getContentSubtitleModel();
            }

            @Override
            protected void configureCustomButtons(BSContainer<?> buttonContainer, BSContainer<?> modalContainer,
                                                  ViewMode viewMode, AnnotationMode annotationMode,
                                                  IModel<? extends SInstance> currentInstance) {
                AbstractFormPage.this.configureCustomButtons(buttonContainer, modalContainer, viewMode,
                        annotationMode, currentInstance);
            }

            @Override
            protected BSModalBorder buildConfirmationModal(BSContainer<?> modalContainer,
                                                           IModel<? extends SInstance> instanceModel) {
                return AbstractFormPage.this.buildConfirmationModal(modalContainer, instanceModel);
            }

            @Override
            protected ProcessInstanceEntity getProcessInstance() {
                return AbstractFormPage.this.getProcessInstance();
            }

            @Override
            protected void saveForm(IModel<? extends SInstance> currentInstance) {
                AbstractFormPage.this.saveForm(currentInstance);
            }

            @Override
            protected IModel<? extends PetitionEntity> getFormModel() {
                return currentModel;
            }

            @Override
            protected boolean hasProcess() {
                return AbstractFormPage.this.hasProcess();
            }

            @Override
            protected String getIdentifier() {
                return AbstractFormPage.this.getIdentifier();
            }

            @Override
            protected void onBuildSingularFormPanel(SingularFormPanel singularFormPanel) {
                AbstractFormPage.this.onBuildSingularFormPanel(singularFormPanel);
            }

            @Override
            protected Component buildExtraContent(String id) {
                return Optional.ofNullable(AbstractFormPage.this.buildExtraContent(id)).orElse(super.buildExtraContent(id));
            }
        };

        return content;
    }

    private Component buildExtraContent(String id) {
        final TemplatePanel extraPanel     = new TemplatePanel(id, MarkupCreator.div("extraContainer"));
        final BSContainer   extraContainer = new BSContainer("extraContainer");
        extraPanel.add(extraContainer);
        appendExtraContent(extraContainer);
        extraPanel.add($b.visibleIf(() -> extraContainer.visitChildren((object, visit) -> visit.stop("found!")) != null));
        return extraPanel;
    }

    private Component buildPreFormPanelContent(String id) {
        final TemplatePanel extraPanel     = new TemplatePanel(id, MarkupCreator.div("extraContainer"));
        final BSContainer   extraContainer = new BSContainer("extraContainer");
        extraPanel.add(extraContainer);
        appendBeforeFormContent(extraContainer);
        extraPanel.add($b.visibleIf(() -> extraContainer.visitChildren((object, visit) -> visit.stop("found!")) != null));
        return extraPanel;
    }

    protected void appendExtraContent(BSContainer extraContainer) {
    }

    private void onBuildSingularFormPanel(SingularFormPanel singularFormPanel) {
        singularFormPanel.setPreFormPanelFactory(this::buildPreFormPanelContent);
    }

    protected void appendBeforeFormContent(BSContainer container) {
    }

    protected abstract IModel<?> getContentSubtitleModel();

    protected abstract String getIdentifier();

    protected void onNewPetitionCreation(T petition, FormPageConfig config) {
    }

    protected void configureCustomButtons(BSContainer<?> buttonContainer, BSContainer<?> modalContainer, ViewMode viewMode, AnnotationMode annotationMode, IModel<? extends SInstance> currentInstance) {
        List<MTransition> trans = null;
        if (StringUtils.isNotEmpty(config.getPetitionId())) {
            trans = petitionService.listCurrentTaskTransitions(Long.valueOf(config.getPetitionId()));
        }

        if (hasMultipleVersionsAndIsMainForm(config.getPetitionId())) {
            appendButtonViewDiff(buttonContainer, config.getPetitionId(), currentInstance);
        }

        if (CollectionUtils.isNotEmpty(trans) && (ViewMode.EDIT.equals(viewMode) || AnnotationMode.EDIT.equals(annotationMode))) {
            int index = 0;
            trans.stream().filter(this::isTransitionButtonVisibible).forEach(t -> {
                if (t.getMetaDataValue(ServerContextMetaData.KEY) != null && t.getMetaDataValue(ServerContextMetaData.KEY).isEnabledOn(SingularSession.get().getServerContext())) {
                    String btnId = "flow-btn" + index;
                    buildFlowTransitionButton(
                            btnId, buttonContainer,
                            modalContainer, t.getName(),
                            currentInstance, viewMode);
                }
            });

        } else {
            buttonContainer.setVisible(false).setEnabled(false);
        }
    }

    /**
     * Verifica se existe mais de uma versão do formulário principal
     * lavando em consideração o rascunho.
     *
     * @param petitionId id da petição a ser verificada
     * @return true em caso afirmativo, false caso contrário
     */
    protected boolean hasMultipleVersionsAndIsMainForm(String petitionId) {
        if (StringUtils.isNotBlank(petitionId)) {

            int totalVersoes = 0;

            // Verifica se existe rascunho
            PetitionEntity petition     = petitionService.findPetitionByCod(Long.valueOf(petitionId));
            FormTypeEntity mainFormType = petition.getMainForm().getFormType();
            DraftEntity    draftEntity  = petition.currentEntityDraftByType(mainFormType.getAbbreviation());
            if (draftEntity != null) {
                totalVersoes++;
            }

            // Busca o número de versões consolidadas
            Long versoesConsolidadas = formPetitionService.countVersions(petition.getMainForm().getCod());
            totalVersoes += versoesConsolidadas;

            String formType = getFormType(config);

            return totalVersoes > 1
                    && formType.equalsIgnoreCase(mainFormType.getAbbreviation());
        }
        return false;
    }

    /**
     * Adiciona o botão para visualizar o diff na barra de botões.
     *
     * @param buttonContainer
     * @param petitionId
     * @param currentInstance
     */
    protected void appendButtonViewDiff(BSContainer<?> buttonContainer, String petitionId, IModel<? extends SInstance> currentInstance) {

        final String        markup      = "<a class='btn' wicket:id='diffLink'><span wicket:id='label'></span></a>";
        final TemplatePanel buttonPanel = buttonContainer.newTemplateTag(tt -> markup);

        WebMarkupContainer link = new WebMarkupContainer("diffLink");
        link.add($b.attr("target", String.format("diff%s", petitionId)));
        link.add($b.attr("href", mountUrlDiff()));

        buttonPanel.add(link.add(new Label("label", "Visualizar Diferenças")));

    }

    protected String mountUrlDiff() {

        StringBuilder url = new StringBuilder();
        url.append(DispatcherPageUtil.getBaseURL())
                .append("?")
                .append(String.format("%s=%s", DispatcherPageParameters.ACTION, config.getFormAction().getId()))
                .append(String.format("&%s=%s", DispatcherPageParameters.PETITION_ID, config.getPetitionId()))
                .append(String.format("&%s=%s", DispatcherPageParameters.FORM_NAME, config.getFormType()))
                .append(String.format("&%s=%s", DispatcherPageParameters.DIFF, Boolean.TRUE.toString()));

        Map<String, String> additionalParams = getAdditionalParams();

        for (Map.Entry<String, String> entry : additionalParams.entrySet()) {
            url.append(String.format("&%s=%s", entry.getKey(), entry.getValue()));
        }

        return url.toString();
    }

    protected Map<String,String> getAdditionalParams() {
        return Collections.emptyMap();
    }

    protected Boolean isTransitionButtonVisibible(MTransition transition) {
        return true;
    }

    protected final T getUpdatedPetitionFromInstance(IModel<? extends SInstance> currentInstance, boolean mainForm) {
        T petition = currentModel.getObject();
        if (currentInstance.getObject() instanceof SIComposite && mainForm) {
            String description = createPetitionDescriptionFromForm(currentInstance.getObject());
            if (description != null && description.length() > 200) {
                getLogger().error("Descrição do formulário muito extensa. A descrição foi cortada.");
                description = description.substring(0, 197) + "...";
            }
            petition.setDescription(description);
        }
        return petition;
    }

    protected String createPetitionDescriptionFromForm(SInstance instance) {
        return instance.toStringDisplay();
    }

    protected SInstance createInstance(SDocumentFactory documentFactory, RefType refType) {
        if (formModel.getObject() == null) {
            /* clonagem do ultimo formulário da petição */
            if (parentPetitionformModel.getObject() != null) {
                return formService.newTransientSInstance(parentPetitionformModel.getObject(), refType, documentFactory, false);
            } else {
                return documentFactory.createInstance(refType);
            }
        } else {
            return formService.loadSInstance(formModel.getObject(), refType, documentFactory);
        }
    }

    protected void buildFlowTransitionButton(String buttonId, BSContainer<?> buttonContainer, BSContainer<?> modalContainer, String transitionName, IModel<? extends SInstance> instanceModel, ViewMode viewMode) {
        final BSModalBorder modal = buildFlowConfirmationModal(buttonId, modalContainer, transitionName, instanceModel, viewMode);
        buildFlowButton(buttonId, buttonContainer, transitionName, modal);
    }

    public void atualizarContentWorklist(AjaxRequestTarget target) {
        target.appendJavaScript("Singular.atualizarContentWorklist();");
    }

    protected BSModalBorder buildConfirmationModal(BSContainer<?> modalContainer, IModel<? extends SInstance> instanceModel) {
        TemplatePanel tpModal = modalContainer.newTemplateTag(tt ->
                "<div wicket:id='send-modal' class='portlet-body form'>\n"
                        + "<wicket:message key=\"label.confirm.message\"/>\n"
                        + "</div>\n");
        BSModalBorder enviarModal = new BSModalBorder("send-modal", getMessage("label.title.send"));
        enviarModal
                .addButton(BSModalBorder.ButtonStyle.CANCEl, "label.button.close", new AjaxButton("cancel-btn") {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        enviarModal.hide(target);
                    }
                })
                .addButton(BSModalBorder.ButtonStyle.CONFIRM, "label.button.confirm", new SingularSaveButton("confirm-btn", instanceModel) {

                    @Override
                    protected void onValidationSuccess(AjaxRequestTarget target, Form<?> form, IModel<? extends SInstance> instanceModel) {
                        AbstractFormPage.this.send(instanceModel, target, enviarModal);
                    }

                    @Override
                    protected void onValidationError(AjaxRequestTarget target, Form<?> form, IModel<? extends SInstance> instanceModel) {
                        enviarModal.hide(target);
                        target.add(form);
                        addToastrErrorMessage("message.send.error");
                    }

                });
        tpModal.add(enviarModal);
        return enviarModal;
    }

    protected void saveForm(IModel<? extends SInstance> currentInstance) {
        saveForm(currentInstance, null);
    }

    protected void saveForm(IModel<? extends SInstance> currentInstance, String transitionName) {
        onBeforeSave(currentInstance);
        formModel.setObject(petitionService.saveOrUpdate(
                getUpdatedPetitionFromInstance(currentInstance, isMainForm()),
                currentInstance.getObject(),
                isMainForm(),
                singularFormConfig,
                t -> onSave(t, transitionName)
        ));
    }

    protected void onSave(T petition, String transitionName) {

    }

    protected boolean onBeforeSend(IModel<? extends SInstance> currentInstance) {
        configureLazyFlowIfNeeded(currentInstance, currentModel.getObject(), config);
        saveForm(currentInstance);
        return true;
    }

    protected void onBeforeSave(IModel<? extends SInstance> currentInstance) {
        configureLazyFlowIfNeeded(currentInstance, currentModel.getObject(), config);
    }

    protected void configureLazyFlowIfNeeded(IModel<? extends SInstance> currentInstance, T petition, FormPageConfig cfg) {
        if (petition.getProcessDefinitionEntity() == null && cfg.isWithLazyProcessResolver()) {
            cfg
                    .getLazyFlowDefinitionResolver()
                    .resolve(cfg, (SIComposite) currentInstance.getObject())
                    .ifPresent(clazz -> {
                        petition.setProcessDefinitionEntity(petitionService.findEntityProcessDefinitionByClass(clazz));
                    });
        }
    }

    /**
     * Inicia o fluxo da petição, consolidando os formularios de rascunho e criando o historico
     *
     * @param mi    modal da instancia atual do formulario
     * @param ajxrt ajax request target do wicket
     * @param sm    modal que disparou a confirmação
     */
    protected void send(IModel<? extends SInstance> mi, AjaxRequestTarget ajxrt, BSModalBorder sm) {
        //executa antes de enviar, chance para fazer validações, salvar o formulario e alterar dados na peticao
        if (onBeforeSend(mi)) {

            //peticao atual, atualizações devem ser feitas em before send
            T petition = currentModel.getObject();

            //instancia atual do formulario
            SInstance instance = mi.getObject();

            //usuario para persistencia
            String username = SingularSession.get().getUsername();

            //executa em block try, caso exita rollback deve recarregar a peticao, para que a mesma não
            //tenha dados que sofreram rollback
            try {
                //executa o envio, iniciando o fluxo informado
                petitionService.send(petition, instance, username, singularFormConfig);
                //janela de oportunidade para executar ações apos o envio, normalmente utilizado para mostrar mensagens
                onAfterSend(ajxrt, sm);
            } catch (Exception ex) {
                //recarrega a petição novamente
                currentModel.setObject(petitionService.findPetitionByCod(petition.getCod()));
                //faz o rethrow da exeção, algumas são tratadas e exibidas na tela como mensagens informativas
                throw SingularServerException.rethrow(ex.getMessage(), ex);
            }
        }
    }

    protected void onAfterSend(AjaxRequestTarget target, BSModalBorder enviarModal) {
        atualizarContentWorklist(target);
        addAfterSendSuccessMessage();
        target.appendJavaScript("; window.close();");
    }

    protected void addAfterSendSuccessMessage() {
        if (getIdentifier() == null) {
            addToastrSuccessMessageWorklist("message.send.success");
        } else {
            addToastrSuccessMessageWorklist("message.send.success.identifier", getIdentifier());
        }
    }

    protected void onBeforeExecuteTransition(AjaxRequestTarget ajaxRequestTarget,
                                             Form<?> form,
                                             String transitionName,
                                             IModel<? extends SInstance> currentInstance)
            throws SingularServerFormValidationError {
        saveForm(currentInstance, transitionName);
    }

    /**
     * Executa a transição da tarefa para a transição informada
     * @param ajxrt target do wicket
     * @param form form atual
     * @param tn transição a ser executada
     * @param mi model contendo a instancia atual
     * @throws SingularServerFormValidationError caso exista erros de validação
     * @see AbstractFormPage#onBeforeExecuteTransition
     * @see PetitionService#executeTransition(String, PetitionEntity, SFormConfig, BiConsumer, Map)
     * @see AbstractFormPage#onTransitionExecuted(AjaxRequestTarget, String)
     */
    protected void executeTransition(AjaxRequestTarget ajxrt, Form<?> form, String tn, IModel<? extends SInstance> mi)
            throws SingularServerFormValidationError
    {

        //relizada a chamada, abrindo janela de oportunidade para salvar e alteradas dados da petição
        onBeforeExecuteTransition(ajxrt, form, tn, mi);

        //petição atual, qualuer alteracao deve ser feita em onBeforeExecuteTransition
        T petition = currentModel.getObject();

        //busca os parametros de transicao do FLOW
        Map<String, String> transitionParams = getTransitionParameters(tn);

        //Executa em bloco try, executa rollback da petição caso exista erro
        try {
            //executa a transicao informada
            petitionService.executeTransition(tn, petition, singularFormConfig, this::onTransition, transitionParams);

            //executa chamada, abrindo janela de oportunidade de executar ações apos execução da transicao
            onTransitionExecuted(ajxrt, tn);
        } catch (Exception ex) {
            //recarrega a petição novamente
            currentModel.setObject(petitionService.findPetitionByCod(petition.getCod()));
            //faz o rethrow da exeção, algumas são tratadas e exibidas na tela como mensagens informativas
            throw SingularServerException.rethrow(ex.getMessage(), ex);
        }
    }

    protected Map<String, String> getTransitionParameters(String transition) {
        return null;
    }

    protected void onTransition(PetitionEntity pe, String transitionName) {

    }

    protected void onTransitionExecuted(AjaxRequestTarget ajaxRequestTarget, String transitionName) {
        atualizarContentWorklist(ajaxRequestTarget);
        addToastrSuccessMessageWorklist("message.action.success", transitionName);
        closeBrowserWindow(ajaxRequestTarget);
    }

    protected void closeBrowserWindow(AjaxRequestTarget ajaxRequestTarget) {
        ajaxRequestTarget.appendJavaScript("window.close();");
    }

    protected boolean hasProcess() {
        return currentModel.getObject().getProcessInstanceEntity() != null;
    }

    protected ProcessInstanceEntity getProcessInstance() {
        return currentModel.getObject().getProcessInstanceEntity();
    }

    protected void setProcessInstance(ProcessInstanceEntity pie) {
        currentModel.getObject().setProcessInstanceEntity(pie);
    }

    protected IModel<?> getContentTitleModel() {
        return new ResourceModel("label.form.content.title");
    }

    private void buildFlowButton(String buttonId,
                                 BSContainer<?> buttonContainer,
                                 String transitionName,
                                 BSModalBorder confirmarAcaoFlowModal) {
        final TemplatePanel tp = buttonContainer.newTemplateTag(tt ->
                "<button  type='submit' class='btn' wicket:id='" + buttonId + "'>\n <span wicket:id='flowButtonLabel' /> \n</button>\n"
        );
        final SingularButton singularButton = new SingularButton(buttonId, content.getFormInstance()) {
            @Override
            protected void onSubmit(AjaxRequestTarget ajaxRequestTarget, Form<?> form) {
                showConfirmModal(transitionName, confirmarAcaoFlowModal, ajaxRequestTarget);
            }
        };
        singularButton.add(new Label("flowButtonLabel", transitionName).setRenderBodyOnly(true));
        tp.add(singularButton);
    }

    protected void showConfirmModal(String transitionName, BSModalBorder modal, AjaxRequestTarget ajaxRequestTarget) {
        modal.show(ajaxRequestTarget);
    }

    /**
     * @param idSuffix -> button id suffix
     * @param mc       -> modal container
     * @param tn       -> transition name
     * @param im       -> instance model
     * @param vm       -> view mode
     * @return
     */
    private BSModalBorder buildFlowConfirmationModal(String idSuffix, BSContainer<?> mc, String tn, IModel<? extends SInstance> im, ViewMode vm) {
        final FlowConfirmModal flowConfirmModal   = resolveFlowConfirmModal(tn);
        final TemplatePanel    modalTemplatePanel = mc.newTemplateTag(t -> flowConfirmModal.getMarkup(idSuffix));
        final BSModalBorder    modal              = flowConfirmModal.init(idSuffix, tn, im, vm);
        modalTemplatePanel.add(modal);
        return modal;
    }

    /**
     * @param tn -> the transition name
     * @return the FlowConfirmModal
     */
    protected FlowConfirmModal<T> resolveFlowConfirmModal(String tn) {
        return new SimpleMessageFlowConfirmModal<>(this);
    }

    protected boolean isMainForm() {
        return true;
    }

    protected String getFormType(FormPageConfig formPageConfig) {
        return formPageConfig.getFormType();
    }

    protected ViewMode getViewMode(FormPageConfig formPageConfig) {
        return formPageConfig.getViewMode();
    }

    protected AnnotationMode getAnnotationMode(FormPageConfig formPageConfig) {
        return formPageConfig.getAnnotationMode();
    }

    protected FormKey loadFormKeyFromTypeAndTask(String typeName, boolean mainForm) {
        return Optional
                .ofNullable(formPetitionService.findFormPetitionEntity(currentModel.getObject(), typeName, mainForm))
                .map(x -> {
                    if (x.getCurrentDraftEntity() != null) {
                        return x.getCurrentDraftEntity().getForm();
                    } else {
                        return x.getForm();
                    }
                })
                .map(FormEntity::getCod)
                .map(cod -> formService.keyFromObject(cod))
                .orElse(null);
    }

}