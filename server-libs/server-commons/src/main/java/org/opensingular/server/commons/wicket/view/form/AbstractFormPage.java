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
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.opensingular.flow.core.MTransition;
import org.opensingular.flow.persistence.entity.ProcessInstanceEntity;
import org.opensingular.form.RefService;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.form.context.SFormConfig;
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
import org.opensingular.server.commons.service.PetitionUtil;
import org.opensingular.server.commons.service.ServerSIntanceProcessAwareService;
import org.opensingular.server.commons.util.DispatcherPageParameters;
import org.opensingular.server.commons.wicket.SingularSession;
import org.opensingular.server.commons.wicket.builder.MarkupCreator;
import org.opensingular.server.commons.wicket.view.template.Content;
import org.opensingular.server.commons.wicket.view.template.Template;
import org.opensingular.server.commons.wicket.view.util.DispatcherPageUtil;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.function.BiConsumer;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;
import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

public abstract class AbstractFormPage<T extends PetitionEntity> extends Template implements Loggable {

    @Inject
    private PetitionService<T> petitionService;

    @Inject
    private FormPetitionService<T> formPetitionService;

    private final Class<T>            petitionClass;
    private final FormPageConfig      config;
    private        IModel<T>          currentModel;
    private final IModel<FormKey>     formKeyModel;
    private final IModel<FormKey>     parentPetitionformModel;
    private       AbstractFormContent content;


    public AbstractFormPage(Class<T> petitionClass, FormPageConfig config) {
        if (config == null) {
            throw new RedirectToUrlException("/singular");
        }
        this.petitionClass = Objects.requireNonNull(petitionClass);
        this.config = Objects.requireNonNull(config);
        this.formKeyModel = $m.ofValue();
        this.parentPetitionformModel = $m.ofValue();
        Objects.requireNonNull(getFormType());
    }

    /** Retorna o serviço de petição. */
    @Nonnull
    protected final PetitionService<T> getPetitionService() {
        return Objects.requireNonNull(petitionService);
    }

    /** Retorna o serviço de manipulação de formuluário de petição. */
    @Nonnull
    protected final FormPetitionService<T> getFormPetitionService() {
        return Objects.requireNonNull(formPetitionService);
    }

    /** Retorna model que contêm a petição. Se ainda não tiver inicilizado, dispara exception. */
    @Nonnull
    protected final IModel<T> getPetitionModel() {
        if (currentModel == null) {
            throw SingularServerException.rethrow("A página ainda não foi inicializada");
        }
        return currentModel;
    }

    /** Retorna a petição atual ou dispara exception se ainda não estiver configurada. */
    @Nonnull
    protected final T getPetition() {
        if (currentModel != null && currentModel.getObject() != null) {
            return currentModel.getObject();
        }
        throw SingularServerException.rethrow("Entidade de petição (" + petitionClass.getName() + ") ainda está null");
    }

    /** Retorna a petição atual. */
    @Nonnull
    protected final Optional<T> getPetitionOptional() {
        if (currentModel == null || currentModel.getObject() == null) {
            return Optional.empty();
        }
        return Optional.of(currentModel.getObject());
    }

    /** Retorna a instância sendo editada no momento (ou dispara exception senão tiver sido inicializada). */
    @Nonnull
    protected final SIComposite getInstance() {
        return (SIComposite) getSingularFormPanel().getInstance();
    }

    /** Retorna as configurações da página de edição de formulário. */
    @Nonnull
    protected final FormPageConfig getConfig() {
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
        final T petition;
        petition = loadPetition();

        currentModel = $m.loadable(() -> petition != null && petition.getCod() != null ? petitionService.getPetitionByCod(petition.getCod()) : petition);
        currentModel.setObject(petition);

        super.onInitialize();
    }

    private T loadPetition() {
        T petition;
        if (config.getPetitionId() != null) {
            petition = petitionService.findPetitionByCod(config.getPetitionId()).orElse(null);
            if (petition != null) {
                final FormEntity formEntityDraftOrPetition = getDraftOrFormEntity(petition);
                if (formEntityDraftOrPetition != null) {
                    formKeyModel.setObject(formPetitionService.formKeyFromFormEntity(formEntityDraftOrPetition));
                }
            }
        } else {
            petition = petitionService.createNewPetitionWithoutSave(petitionClass, config, this::onNewPetitionCreation);
        }

        if (config.getParentPetitionId() != null) {
            defineParentPetition(petition);
        }
        return petition;
    }

    private void defineParentPetition(T petition) {
    /* carrega a chave do form da petição pai para posterior clonagem */
        T parentPetition = petitionService.getPetitionByCod(config.getParentPetitionId());
        if (parentPetition != null) {
            parentPetitionformModel.setObject(formPetitionService.formKeyFromFormEntity(parentPetition.getMainForm()));
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
        return petition.currentEntityDraftByType(getFormType())
                .map(DraftEntity::getForm)
                .orElseGet(() -> getFormPetitionEntity(petition).map(FormPetitionEntity::getForm).orElse(null));
    }

    private Optional<FormPetitionEntity> getFormPetitionEntity(T petition) {
        if (isMainForm()) {
            return formPetitionService.findFormPetitionEntityByType(petition.getCod(), getFormType());
        } else {
            return formPetitionService.findFormPetitionEntityByTypeAndTask(petition.getCod(), getFormType(),
                    PetitionUtil.getCurrentTaskDefinition(petition).getCod());
        }
    }

    @Override
    protected final Content getContent(String id) {

        String typeName = getFormType();
        if (typeName == null || config.getPetitionId() == null) {
            String urlServidorSingular = SingularProperties.get().getProperty(SingularProperties.SINGULAR_SERVER_ADDR);
            throw new RedirectToUrlException(urlServidorSingular);
        }

        content = new AbstractFormContent(id, typeName, getViewMode(config), getAnnotationMode(config)) {

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
            protected final IModel<T> getFormModel() {
                return getPetitionModel();
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
        };

        final RefType refType = formPetitionService.loadRefType(typeName);
        content.singularFormPanel.setInstanceCreator(() -> createInstance(refType));

        onBuildSingularFormPanel(content.singularFormPanel);

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

    @Nonnull
    protected abstract Optional<String> getIdentifier();

    protected void onNewPetitionCreation(T petition, FormPageConfig config) {
    }

    protected void configureCustomButtons(BSContainer<?> buttonContainer, BSContainer<?> modalContainer, ViewMode viewMode, AnnotationMode annotationMode, IModel<? extends SInstance> currentInstance) {
        List<MTransition> trans = null;
        if (config.getPetitionId() != null) {
            trans = petitionService.listCurrentTaskTransitions(config.getPetitionId());
        }

        if (hasMultipleVersionsAndIsMainForm(config.getPetitionId())) {
            appendButtonViewDiff(buttonContainer, config.getPetitionId(), currentInstance);
        }

        if (CollectionUtils.isNotEmpty(trans) && (ViewMode.EDIT == viewMode || AnnotationMode.EDIT == annotationMode)) {
            int index = 0;
            trans.stream().filter(this::isTransitionButtonVisibible).forEach(t -> {//NOSONAR
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
    protected boolean hasMultipleVersionsAndIsMainForm(Long petitionId) {
        if (petitionId != null) {

            int totalVersoes = 0;

            // Verifica se existe rascunho
            PetitionEntity petition     = petitionService.getPetitionByCod(petitionId);
            String typeName = PetitionUtil.getTypeName(petition);
            if (petition.currentEntityDraftByType(typeName).isPresent()) {
                totalVersoes++;
            }

            // Busca o número de versões consolidadas
            Long versoesConsolidadas = formPetitionService.countVersions(petition.getMainForm().getCod());
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
                .append('?')
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
        return Boolean.TRUE;
    }

    protected final T getUpdatedPetitionFromInstance(IModel<? extends SInstance> currentInstance, boolean mainForm) {
        T petition = getPetition();
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

    @Nonnull
    protected SInstance createInstance(@Nonnull RefType refType) {

        SDocumentConsumer extraSetup = SDocumentConsumer.of(getDocumentExtraSetuper(getPetitionModel()));

        if (formKeyModel.getObject() == null) {
            /* clonagem do ultimo formulário da petição */
            if (parentPetitionformModel.getObject() != null) {
                return formPetitionService.newTransientSInstance(parentPetitionformModel.getObject(), refType, false, extraSetup);
            } else {
                return formPetitionService.createInstance(refType, extraSetup);
            }
        } else {
            return formPetitionService.getSInstance(formKeyModel.getObject(), refType, extraSetup);
        }
    }

    private static IConsumer<SDocument> getDocumentExtraSetuper(IModel<? extends PetitionEntity> petitionModel) {
        //É um método estático para garantir que nada inesperado vai ser serializado junto
        return document -> document.bindLocalService("processService", ServerSIntanceProcessAwareService.class,
                RefService.of((ServerSIntanceProcessAwareService) () -> petitionModel.getObject().getProcessInstanceEntity()));
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
            T petition = getUpdatedPetitionFromInstance(currentInstance, isMainForm());
            formKeyModel.setObject(petitionService.saveOrUpdate(petition, instance, isMainForm()));
            onSave(petition, transitionName);
        }
    }

    protected void onSave(T petition, String transitionName) {

    }

    protected boolean onBeforeSend(IModel<? extends SInstance> currentInstance) {
        configureLazyFlowIfNeeded(currentInstance, getPetition(), config);
        saveForm(currentInstance);
        return true;
    }

    protected void onBeforeSave(IModel<? extends SInstance> currentInstance) {
        configureLazyFlowIfNeeded(currentInstance, getPetition(), config);
    }

    protected void configureLazyFlowIfNeeded(IModel<? extends SInstance> currentInstance, T petition, FormPageConfig cfg) {
        if (petition.getProcessDefinitionEntity() == null && cfg.isWithLazyProcessResolver()) {
            cfg
                    .getLazyFlowDefinitionResolver()
                    .resolve(cfg, (SIComposite) currentInstance.getObject())
                    .ifPresent(clazz -> petition.setProcessDefinitionEntity(petitionService.findEntityProcessDefinitionByClass(clazz)));
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
            T petition = getPetition();

            //instancia atual do formulario
            SInstance instance = mi.getObject();

            //usuario para persistencia
            String username = SingularSession.get().getUsername();

            //executa em block try, caso exita rollback deve recarregar a peticao, para que a mesma não
            //tenha dados que sofreram rollback
            try {
                //executa o envio, iniciando o fluxo informado
                petitionService.send(petition, instance, username);
                //janela de oportunidade para executar ações apos o envio, normalmente utilizado para mostrar mensagens
                onAfterSend(ajxrt, sm);
            } catch (Exception ex) {
                //recarrega a petição novamente
                getPetitionModel().setObject(petitionService.getPetitionByCod(petition.getCod()));
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
        T petition = getPetition();

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
            getPetitionModel().setObject(petitionService.getPetitionByCod(petition.getCod()));
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
        return getPetition().getProcessInstanceEntity() != null;
    }

    protected ProcessInstanceEntity getProcessInstance() {
        return getPetition().getProcessInstanceEntity();
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

    protected String getFormType() {
        return config.getFormType();
    }

    protected ViewMode getViewMode(FormPageConfig formPageConfig) {
        return formPageConfig.getViewMode();
    }

    protected AnnotationMode getAnnotationMode(FormPageConfig formPageConfig) {
        return formPageConfig.getAnnotationMode();
    }

    @Nonnull
    protected final Optional<FormKey> loadFormKeyFromTypeAndTask(@Nonnull Class<? extends SType<?>> typeClass, boolean mainForm) {
        return formPetitionService.findFormPetitionEntity(getPetition(), typeClass, mainForm)
                .map(x -> x.getCurrentDraftEntity() == null ? x.getForm() : x.getCurrentDraftEntity().getForm())
                .map(formEntity -> formPetitionService.formKeyFromFormEntity(formEntity));
    }

}