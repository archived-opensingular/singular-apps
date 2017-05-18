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
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.jetbrains.annotations.NotNull;
import org.opensingular.flow.core.STask;
import org.opensingular.flow.core.STransition;
import org.opensingular.flow.core.TaskInstance;
import org.opensingular.flow.core.TransitionAccess;
import org.opensingular.form.RefService;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.form.document.RefType;
import org.opensingular.form.document.SDocument;
import org.opensingular.form.document.SDocumentConsumer;
import org.opensingular.form.persistence.FormKey;
import org.opensingular.form.persistence.entity.FormEntity;
import org.opensingular.form.wicket.component.SingularButton;
import org.opensingular.form.wicket.component.SingularSaveButton;
import org.opensingular.form.wicket.enums.AnnotationMode;
import org.opensingular.form.wicket.enums.ViewMode;
import org.opensingular.form.wicket.panel.SingularFormPanel;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.commons.lambda.IConsumer;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.lib.wicket.util.bootstrap.layout.BSContainer;
import org.opensingular.lib.wicket.util.bootstrap.layout.TemplatePanel;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.exception.SingularServerFormValidationError;
import org.opensingular.server.commons.flow.FlowResolver;
import org.opensingular.server.commons.metadata.SingularServerMetadata;
import org.opensingular.server.commons.persistence.entity.form.DraftEntity;
import org.opensingular.server.commons.persistence.entity.form.FormPetitionEntity;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.requirement.SingularRequirement;
import org.opensingular.server.commons.service.FormPetitionService;
import org.opensingular.server.commons.service.PetitionInstance;
import org.opensingular.server.commons.service.PetitionSender;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.service.PetitionUtil;
import org.opensingular.server.commons.service.ServerSIntanceProcessAwareService;
import org.opensingular.server.commons.service.SingularRequirementService;
import org.opensingular.server.commons.service.dto.PetitionSendedFeedback;
import org.opensingular.server.commons.wicket.SingularSession;
import org.opensingular.server.commons.wicket.builder.MarkupCreator;
import org.opensingular.server.commons.wicket.view.template.Content;
import org.opensingular.server.commons.wicket.view.template.Template;
import org.opensingular.server.commons.wicket.view.util.ActionContext;
import org.opensingular.server.commons.wicket.view.util.ModuleButtonFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;
import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

public abstract class AbstractFormPage<PE extends PetitionEntity, PI extends PetitionInstance> extends Template implements Loggable {

    private final FormPageExecutionContext config;
    private final IModel<FormKey>          formKeyModel;
    private final IModel<FormKey>          parentPetitionformKeyModel;
    private final IModel<Boolean>          inheritParentFormData;

    @Inject
    private PetitionService<PE, PI> petitionService;

    @Inject
    private FormPetitionService<PE> formPetitionService;

    @Inject
    private SingularServerMetadata singularServerMetadata;


    private           IModel<PI>             currentModel;
    private transient Optional<TaskInstance> currentTaskInstance;
    private           AbstractFormContent    content;

    public AbstractFormPage(@Nullable ActionContext context) {
        this(context, null);
    }

    public AbstractFormPage(@Nullable ActionContext context, @Nullable Class<? extends SType<?>> formType) {
        if (context == null) {
            String url = singularServerMetadata.getServerBaseUrl();
            getLogger().info(" Redirecting to {}", url);
            throw new RedirectToUrlException(url);
        }

        this.config = new FormPageExecutionContext(Objects.requireNonNull(context), getTypeName(formType), getFlowResolver(context), getPetitionSender(context));
        this.formKeyModel = $m.ofValue();
        this.parentPetitionformKeyModel = $m.ofValue();
        this.inheritParentFormData = $m.ofValue();

        context.getInheritParentFormData().ifPresent(inheritParentFormData::setObject);

        if (this.config.getFormName() == null) {
            throw new SingularServerException("Tipo do formulário da página nao foi definido");
        }
    }

    private static IConsumer<SDocument> getDocumentExtraSetuper(IModel<? extends PetitionInstance> petitionModel) {
        //É um método estático para garantir que nada inesperado vai ser serializado junto
        return document -> document.bindLocalService("processService", ServerSIntanceProcessAwareService.class,
                RefService.of((ServerSIntanceProcessAwareService) () -> petitionModel.getObject().getProcessInstance()));
    }

    protected Optional<TaskInstance> getCurrentTaskInstance() {
        if (currentTaskInstance == null) {
            if (config.getPetitionId().isPresent()) {
                currentTaskInstance = petitionService.findCurrentTaskInstanceByPetitionId(config.getPetitionId().get());
            } else {
                currentTaskInstance = Optional.empty();
            }
        }
        return currentTaskInstance;
    }

    private String getTypeName(@Nullable Class<? extends SType<?>> formType) {
        if (formType != null) {
            return PetitionUtil.getTypeName(formType);
        }
        return null;
    }

    private FlowResolver getFlowResolver(@Nullable ActionContext context) {
        return getSingularRequirement(context).map(SingularRequirement::getFlowResolver).orElse(null);
    }

    private Class<? extends PetitionSender> getPetitionSender(@Nullable ActionContext context) {
        return getSingularRequirement(context).map(SingularRequirement::getPetitionSenderBeanClass).orElse(null);
    }

    private Optional<SingularRequirement> getSingularRequirement(@Nullable ActionContext context) {
        SingularRequirementService requirementService = SingularRequirementService.get();
        return Optional.ofNullable(requirementService.getSingularRequirement(context));
    }

    /**
     * Retorna o serviço de petição.
     */
    @Nonnull
    protected final PetitionService<PE, PI> getPetitionService() {
        return Objects.requireNonNull(petitionService);
    }

    /**
     * Retorna o serviço de manipulação de formuluário de petição.
     */
    @Nonnull
    protected final FormPetitionService<PE> getFormPetitionService() {
        return Objects.requireNonNull(formPetitionService);
    }

    /**
     * Retorna model que contêm a petição. Se ainda não tiver inicilizado, dispara exception.
     */
    @Nonnull
    protected final IModel<PI> getPetitionModel() {
        if (currentModel == null) {
            throw SingularServerException.rethrow("A página ainda não foi inicializada");
        }
        return currentModel;
    }

    /**
     * Retorna a petição atual ou dispara exception se ainda não estiver configurada.
     */
    @Nonnull
    protected final PI getPetition() {
        if (currentModel != null && currentModel.getObject() != null) {
            return currentModel.getObject();
        }
        throw SingularServerException.rethrow("A petição (" + PetitionInstance.class.getName() + ") ainda está null");
    }

    /**
     * Retorna a petição atual.
     */
    @Nonnull
    protected final Optional<PI> getPetitionOptional() {
        if (currentModel == null || currentModel.getObject() == null) {
            return Optional.empty();
        }
        return Optional.of(currentModel.getObject());
    }

    /**
     * Retorna a instância sendo editada no momento (ou dispara exception senão tiver sido inicializada).
     */
    @Nonnull
    protected final SIComposite getInstance() {
        return (SIComposite) getSingularFormPanel().getInstance();
    }

    @Nonnull
    protected final IModel<? extends SInstance> getInstanceModel() {
        return getSingularFormPanel().getInstanceModel();
    }

    /**
     * Retorna as configurações da página de edição de formulário.
     */
    @Nonnull
    protected final FormPageExecutionContext getConfig() {
        return config;
    }

    protected final SingularFormPanel getSingularFormPanel() {
        return content.getSingularFormPanel();
    }

    @Override
    protected boolean withMenu() {
        return false;
    }

    @Override
    protected void onInitialize() {
        final PI petition = loadPetition();

        currentModel = $m.loadable(() -> petition != null && petition.getCod() != null ? petitionService.getPetition(petition.getCod()) : petition);
        currentModel.setObject(petition);

        super.onInitialize();
    }

    private PI loadPetition() {
        PI             petition;
        Optional<Long> petitionId = config.getPetitionId();
        if (petitionId.isPresent()) {
            petition = petitionService.getPetition(petitionId.get());
            FormEntity formEntityDraftOrPetition = getDraftOrFormEntity(petition);
            if (formEntityDraftOrPetition != null) {
                formKeyModel.setObject(formPetitionService.formKeyFromFormEntity(formEntityDraftOrPetition));
            }
        } else {
            PI parentPetition = null;
            Optional<Long> parentPetitionId = config.getParentPetitionId();
            if (parentPetitionId.isPresent()) {
                parentPetition = petitionService.getPetition(parentPetitionId.get());
                parentPetitionformKeyModel.setObject(formPetitionService.formKeyFromFormEntity(parentPetition.getEntity().getMainForm()));
            }
            petition = petitionService.createNewPetitionWithoutSave(null, parentPetition, this::onNewPetitionCreation);
        }
        return petition;
    }

    private FormEntity getDraftOrFormEntity(PI petition) {
        return petition.getEntity()
                .currentEntityDraftByType(getFormType())
                .map(DraftEntity::getForm)
                .orElseGet(() -> getFormPetitionEntity(petition).map(FormPetitionEntity::getForm).orElse(null));
    }

    private Optional<FormPetitionEntity> getFormPetitionEntity(PI petition) {
        if (isMainForm()) {
            return formPetitionService.findFormPetitionEntityByType(petition, getFormType());
        } else {
            return formPetitionService.findFormPetitionEntityByTypeAndTask(petition, getFormType(),
                    petition.getCurrentTaskOrException());
        }
    }

    @Override
    protected final Content getContent(String id) {
        if (config.getFormName() == null && config.getPetitionId().isPresent()) {
            String url = SingularProperties.get().getProperty(SingularProperties.SINGULAR_SERVER_ADDR);
            getLogger().info(" Redirecting to {}", url);
            throw new RedirectToUrlException(url);
        }

        content = new AbstractFormContent(id, config.getFormName(), getViewMode(config), getAnnotationMode(config)) {

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
            protected void saveForm(IModel<? extends SInstance> currentInstance) {
                AbstractFormPage.this.saveForm(currentInstance);
            }

            @Override
            protected boolean hasProcess() {
                return AbstractFormPage.this.hasProcess();
            }

            @Override
            protected Optional<String> getIdentifier() {
                return AbstractFormPage.this.getIdentifier();
            }

            @Override
            protected Component buildExtraContent(String id) {
                return Optional.ofNullable(AbstractFormPage.this.buildExtraContent(id)).orElse(super.buildExtraContent(id));
            }

            @Override
            protected ServerSendButton makeServerSendButton(String id, BSModalBorder enviarModal) {
                return AbstractFormPage.this.makeServerSendButton(id, getFormInstance(), enviarModal);
            }
        };

        final RefType refType = formPetitionService.loadRefType(config.getFormName());
        content.singularFormPanel.setInstanceCreator(() -> createInstance(refType));

        onBuildSingularFormPanel(content.singularFormPanel);

        return content;
    }

    protected ServerSendButton makeServerSendButton(String id, IModel<? extends SInstance> formInstance, BSModalBorder enviarModal) {
        return new ServerSendButton(id, formInstance, enviarModal);
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

    @Nonnull
    protected abstract Optional<String> getIdentifier();

    protected void onNewPetitionCreation(PI petition) {
    }

    protected void configureCustomButtons(BSContainer<?> buttonContainer, BSContainer<?> modalContainer, ViewMode viewMode, AnnotationMode annotationMode, IModel<? extends SInstance> currentInstance) {
        Optional<Long> petitionId = config.getPetitionId();
        if (petitionId.isPresent()) {
            if (hasMultipleVersionsAndIsMainForm(petitionId.get())) {
                appendButtonViewDiff(buttonContainer, petitionId.get(), currentInstance);
            }
        }

        if (getCurrentTaskInstance().isPresent()) {
            List<STransition> transitions = getCurrentTaskInstance()
                    .flatMap(TaskInstance::getFlowTask)
                    .map(STask::getTransitions)
                    .orElse(Collections.emptyList());
            if (CollectionUtils.isNotEmpty(transitions) && (ViewMode.EDIT == viewMode || AnnotationMode.EDIT == annotationMode)) {
                int index = 0;
                for (STransition t : transitions) {
                    TransitionAccess transitionAccess = getButtonAccess(t, getCurrentTaskInstance().get());
                    if (transitionAccess.isVisible()) {
                        String btnId = "flow-btn" + index;
                        buildFlowTransitionButton(
                                btnId,
                                buttonContainer,
                                modalContainer,
                                t.getName(),
                                currentInstance,
                                viewMode,
                                transitionAccess);
                    }
                }
            } else {
                buttonContainer.setVisible(false).setEnabled(false);
            }
        }
    }

    /**
     * Verifica se existe mais de uma versão do formulário principal
     * lavando em consideração o rascunho.
     *
     * @param petitionId id da petição a ser verificada
     * @return true em caso afirmativo, false caso contrário
     */
    protected boolean hasMultipleVersionsAndIsMainForm(Long petitionId) {
        if (petitionId != null) {

            int totalVersoes = 0;

            // Verifica se existe rascunho
            PetitionInstance petition = petitionService.getPetition(petitionId);
            String typeName = PetitionUtil.getTypeName(petition);
            if (petition.getEntity().currentEntityDraftByType(typeName).isPresent()) {
                totalVersoes++;
            }

            // Busca o número de versões consolidadas
            Long versoesConsolidadas = formPetitionService.countVersions(petition.getEntity().getMainForm().getCod());
            totalVersoes += versoesConsolidadas;

            String formType = getFormType();

            return totalVersoes > 1 && formType.equalsIgnoreCase(typeName);
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
    protected void appendButtonViewDiff(BSContainer<?> buttonContainer, Long petitionId, IModel<? extends SInstance> currentInstance) {
        buttonContainer.appendComponent(id ->
                        new ModuleButtonFactory(ActionContext.fromFormConfig(config), getAdditionalParams())
                                .getDiffButton(id)
        );
    }

    protected Map<String, String> getAdditionalParams() {
        return Collections.emptyMap();
    }


    protected final TransitionAccess getButtonAccess(STransition transition, TaskInstance t) {
        return transition.getAccessFor(t);
    }

    protected final PI getUpdatedPetitionFromInstance(IModel<? extends SInstance> currentInstance, boolean mainForm) {
        PI petition = getPetition();
        if (currentInstance.getObject() instanceof SIComposite && mainForm) {
            petitionService.updatePetitionDescription(currentInstance.getObject(), petition);
        }
        return petition;
    }


    @NotNull
    @Nonnull
    protected SInstance createInstance(@Nonnull RefType refType) {

        SDocumentConsumer extraSetup = SDocumentConsumer.of(getDocumentExtraSetuper(getPetitionModel()));

        if (formKeyModel.getObject() == null) {
            /* clonagem do ultimo formulário da petição */
            if (parentPetitionformKeyModel.getObject() != null
                    && inheritParentFormData.getObject() != null
                    && inheritParentFormData.getObject()) {
                return formPetitionService.newTransientSInstance(parentPetitionformKeyModel.getObject(), refType, false, extraSetup);
            } else {
                return formPetitionService.createInstance(refType, extraSetup);
            }
        } else {
            return formPetitionService.getSInstance(formKeyModel.getObject(), refType, extraSetup);
        }
    }

    protected void buildFlowTransitionButton(String buttonId, BSContainer<?> buttonContainer, BSContainer<?> modalContainer, String transitionName, IModel<? extends SInstance> instanceModel, ViewMode viewMode, TransitionAccess transitionButtonEnabled) {
        final BSModalBorder modal = buildFlowConfirmationModal(buttonId, modalContainer, transitionName, instanceModel, viewMode);
        buildFlowButton(buttonId, buttonContainer, transitionName, modal, transitionButtonEnabled);
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
                .addButton(BSModalBorder.ButtonStyle.CANCEL, "label.button.close", new AjaxButton("cancel-btn") {
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
        SInstance instance = currentInstance.getObject();
        if (instance != null) {
            PI petition = getUpdatedPetitionFromInstance(currentInstance, isMainForm());
            formKeyModel.setObject(petitionService.saveOrUpdate(petition, instance, isMainForm()));
            onSave(petition, transitionName);
        }
    }

    protected void onSave(PI petition, String transitionName) {
    }

    protected boolean onBeforeSend(IModel<? extends SInstance> currentInstance) {
        return true;
    }

    protected void onBeforeSave(IModel<? extends SInstance> currentInstance) {
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
        resolveFlow(mi);
        saveForm(mi);
        if (onBeforeSend(mi)) {

            //peticao atual, atualizações devem ser feitas em before send
            PI petition = getPetition();

            //instancia atual do formulario
            SInstance instance = mi.getObject();

            //usuario para persistencia
            String username = SingularSession.get().getUsername();

            //executa em block try, caso exita rollback deve recarregar a peticao, para que a mesma não
            //tenha dados que sofreram rollback
            try {
                //executa o envio, iniciando o fluxo informado
                Class<? extends PetitionSender> senderClass = config.getPetitionSender();
                PetitionSender sender = ApplicationContextProvider.get().getBean(senderClass);
                if (sender != null) {
                    PetitionSendedFeedback sendedFeedback = sender.send(petition, instance, username);
                    //janela de oportunidade para executar ações apos o envio, normalmente utilizado para mostrar mensagens
                    onAfterSend(ajxrt, sm, sendedFeedback);
                } else {
                    throw new SingularServerException("O PetitionSender não foi configurado corretamente");
                }
            } catch (Exception ex) {
                //recarrega a petição novamente
                getPetitionModel().setObject(petitionService.getPetition(petition.getCod()));
                //faz o rethrow da exeção, algumas são tratadas e exibidas na tela como mensagens informativas
                throw SingularServerException.rethrow(ex.getMessage(), ex);
            }
        }
    }

    protected void resolveFlow(IModel<? extends SInstance> mi) {
        getPetition().setProcessDefinition(config.getFlowResolver()
                .resolve(config, (SIComposite) mi.getObject())
                .orElseThrow(() -> new SingularServerException("Não foi possível determinar o fluxo a ser inicicado.")));
    }


    protected void onAfterSend(AjaxRequestTarget target, BSModalBorder enviarModal, PetitionSendedFeedback sendedFeedback) {
        atualizarContentWorklist(target);
        addAfterSendSuccessMessage();
        target.appendJavaScript("; window.close();");
    }

    protected void addAfterSendSuccessMessage() {
        Optional<String> identifier = getIdentifier();
        if (identifier.isPresent()) {
            addToastrSuccessMessageWorklist("message.send.success.identifier", identifier.get());
        } else {
            addToastrSuccessMessageWorklist("message.send.success");
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
     *
     * @param ajxrt target do wicket
     * @param form  form atual
     * @param tn    transição a ser executada
     * @param mi    model contendo a instancia atual
     * @throws SingularServerFormValidationError caso exista erros de validação
     * @see AbstractFormPage#onBeforeExecuteTransition
     * @see AbstractFormPage#onTransitionExecuted(AjaxRequestTarget, String)
     */
    protected void executeTransition(AjaxRequestTarget ajxrt, Form<?> form, String tn, IModel<? extends SInstance> mi)
            throws SingularServerFormValidationError {

        //relizada a chamada, abrindo janela de oportunidade para salvar e alteradas dados da petição
        onBeforeExecuteTransition(ajxrt, form, tn, mi);

        //petição atual, qualuer alteracao deve ser feita em onBeforeExecuteTransition
        PI petition = getPetition();

        //busca os parametros de transicao do FLOW
        Map<String, String> transitionParams = getTransitionParameters(tn);

        //Executa em bloco try, executa rollback da petição caso exista erro
        try {
            //executa a transicao informada
            petitionService.executeTransition(tn, petition, this::onTransition, transitionParams);

            //executa chamada, abrindo janela de oportunidade de executar ações apos execução da transicao
            onTransitionExecuted(ajxrt, tn);
        } catch (Exception ex) {
            //recarrega a petição novamente
            getPetitionModel().setObject(petitionService.getPetition(petition.getCod()));
            //faz o rethrow da exeção, algumas são tratadas e exibidas na tela como mensagens informativas
            throw SingularServerException.rethrow(ex.getMessage(), ex);
        }
    }

    protected Map<String, String> getTransitionParameters(String transition) {
        return new HashMap<>();
    }

    protected void onTransition(PetitionInstance pe, String transitionName) {

    }

    protected void onTransitionExecuted(AjaxRequestTarget ajaxRequestTarget, String transitionName) {
        atualizarContentWorklist(ajaxRequestTarget);
        addToastrSuccessMessageWorklist("message.action.success", transitionName);
        closeBrowserWindow(ajaxRequestTarget);
    }

    protected void closeBrowserWindow(AjaxRequestTarget ajaxRequestTarget) {
        ajaxRequestTarget.appendJavaScript("window.close();");
    }

    protected final boolean hasProcess() {
        return getPetition().hasProcessInstance();
    }

    protected IModel<?> getContentTitleModel() {
        return new ResourceModel("label.form.content.title", "Nova Solicitação");
    }

    private void buildFlowButton(String buttonId,
                                 BSContainer<?> buttonContainer,
                                 String transitionName,
                                 BSModalBorder confirmarAcaoFlowModal, TransitionAccess access) {
        final TemplatePanel tp = buttonContainer.newTemplateTag(tt ->
                        "<button  type='submit' class='btn' wicket:id='" + buttonId + "'>\n <span wicket:id='flowButtonLabel' /> \n</button>\n"
        );
        final SingularButton singularButton = new SingularButton(buttonId, content.getFormInstance()) {
            @Override
            protected void onSubmit(AjaxRequestTarget ajaxRequestTarget, Form<?> form) {
                showConfirmModal(transitionName, confirmarAcaoFlowModal, ajaxRequestTarget);
            }

            @Override
            protected void onInitialize() {
                super.onInitialize();
                this.setEnabled(access.isEnabled());
                if (access.getMessage().isPresent()) {
                    this.add($b.attr("data-toggle", "tooltip"));
                    this.add($b.attr("data-placement", "top"));
                    this.add($b.attr("title", access.getMessage().get()));
                }
            }
        };
        singularButton.add(new Label("flowButtonLabel", transitionName).setRenderBodyOnly(true));
        tp.add(singularButton);
    }

    protected void showConfirmModal(String transitionName, BSModalBorder modal, AjaxRequestTarget ajaxRequestTarget) {
        modal.show(ajaxRequestTarget);
    }

    /**
     * @param idSuffix  -> button id suffix
     * @param container -> modal container
     * @param tn        -> transition name
     * @param im        -> instance model
     * @param vm        -> view mode
     * @return
     */
    private BSModalBorder buildFlowConfirmationModal(String idSuffix, BSContainer<?> container, String tn, IModel<? extends SInstance> im, ViewMode vm) {
        final FlowConfirmPanel flowConfirmPanel = resolveFlowConfirmModal("confirmPanel" + idSuffix, tn);
        container.appendTag("div", flowConfirmPanel);
        return flowConfirmPanel.getModalBorder();
    }

    /**
     * @param tn -> the transition name
     * @return the FlowConfirmPanel
     */
    protected FlowConfirmPanel resolveFlowConfirmModal(String id, String tn) {
        return new SimpleMessageFlowConfirmModal<>(id, tn, this);
    }

    private boolean isMainForm() {
        return config.isMainForm();
    }

    @Nonnull
    private String getFormType() {
        return config.getFormName();
    }

    protected ViewMode getViewMode(FormPageExecutionContext formPageConfig) {
        return formPageConfig.getViewMode();
    }

    protected AnnotationMode getAnnotationMode(FormPageExecutionContext formPageConfig) {
        return formPageConfig.getAnnotationMode();
    }

    @Nonnull
    protected final Optional<FormKey> loadFormKeyFromTypeAndTask(@Nonnull Class<? extends SType<?>> typeClass, boolean mainForm) {
        return formPetitionService.findFormPetitionEntity(getPetition(), typeClass, mainForm)
                .map(x -> x.getCurrentDraftEntity() == null ? x.getForm() : x.getCurrentDraftEntity().getForm())
                .map(formEntity -> formPetitionService.formKeyFromFormEntity(formEntity));
    }

}