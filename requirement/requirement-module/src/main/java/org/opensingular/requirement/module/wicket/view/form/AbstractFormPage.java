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

package org.opensingular.requirement.module.wicket.view.form;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.flow.core.STask;
import org.opensingular.flow.core.STransition;
import org.opensingular.flow.core.TaskInstance;
import org.opensingular.flow.core.TransitionAccess;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.form.type.core.annotation.AnnotationClassifier;
import org.opensingular.form.type.core.annotation.AtrAnnotation;
import org.opensingular.form.validation.ValidationError;
import org.opensingular.form.wicket.component.SingularButton;
import org.opensingular.form.wicket.component.SingularSaveButton;
import org.opensingular.form.wicket.component.SingularValidationButton;
import org.opensingular.form.wicket.enums.AnnotationMode;
import org.opensingular.form.wicket.enums.ViewMode;
import org.opensingular.form.wicket.panel.SFormModalEventListenerBehavior;
import org.opensingular.form.wicket.panel.SingularFormPanel;
import org.opensingular.form.wicket.util.WicketFormProcessing;
import org.opensingular.internal.lib.support.spring.injection.SingularSpringInjector;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.wicket.util.bootstrap.layout.BSContainer;
import org.opensingular.lib.wicket.util.bootstrap.layout.TemplatePanel;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;
import org.opensingular.lib.wicket.util.model.IReadOnlyModel;
import org.opensingular.lib.wicket.util.util.Shortcuts;
import org.opensingular.requirement.module.RequirementDefinition;
import org.opensingular.requirement.module.exception.SingularRequirementException;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.opensingular.requirement.module.exception.SingularServerFormValidationError;
import org.opensingular.requirement.module.persistence.entity.form.FormRequirementEntity;
import org.opensingular.requirement.module.service.FormRequirementService;
import org.opensingular.requirement.module.service.RequirementInstance;
import org.opensingular.requirement.module.service.RequirementService;
import org.opensingular.requirement.module.service.RequirementUtil;
import org.opensingular.requirement.module.service.SingularRequirementService;
import org.opensingular.requirement.module.service.dto.RequirementSubmissionResponse;
import org.opensingular.requirement.module.spring.security.SingularRequirementUserDetails;
import org.opensingular.requirement.module.wicket.SingularSession;
import org.opensingular.requirement.module.wicket.builder.HTMLParameters;
import org.opensingular.requirement.module.wicket.builder.MarkupCreator;
import org.opensingular.requirement.module.wicket.view.panel.FeedbackAposEnvioPanel;
import org.opensingular.requirement.module.wicket.view.panel.NotificationPanel;
import org.opensingular.requirement.module.wicket.view.template.ServerTemplate;
import org.opensingular.requirement.module.wicket.view.util.ActionContext;
import org.opensingular.requirement.module.wicket.view.util.ModuleButtonFactory;
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;
import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;
import static org.opensingular.requirement.module.wicket.builder.MarkupCreator.button;
import static org.opensingular.requirement.module.wicket.builder.MarkupCreator.div;
import static org.opensingular.requirement.module.wicket.builder.MarkupCreator.span;

public abstract class AbstractFormPage<RI extends RequirementInstance> extends ServerTemplate implements Loggable {

    protected final String                   typeName;
    protected final FormPageExecutionContext config;
    protected final SingularFormPanel        singularFormPanel;
    protected       Component                containerBehindSingularPanel;
    protected final IModel<Boolean>          inheritParentFormData;
    protected final BSContainer<?>           modalContainer = new BSContainer<>("modals");
    protected final BSModalBorder            closeModal     = construirCloseModal();

    private final Map<String, TransitionController<?>>       transitionControllerMap   = new HashMap<>();
    private       Map<String, STypeBasedFlowConfirmModal<?>> transitionConfirmModalMap = new HashMap<>();
    private       BSModalBorder                              notificacoesModal;
    private       FeedbackAposEnvioPanel                     feedbackAposEnvioPanel    = null;
    private       IModel<RI>                                 requirementInstanceModel;
    private       IModel<Long>                               requirementIdModel        = new Model<>();


    @Inject
    private RequirementService requirementService;

    @Inject
    private FormRequirementService      formRequirementService;
    private AbstractDefaultAjaxBehavior saveFormAjaxBehavior;

    public AbstractFormPage(@Nullable ActionContext context) {
        this(context, null);
    }

    public AbstractFormPage(@Nullable ActionContext context, @Nullable Class<? extends SType<?>> formType) {
        if (context == null) {
            String path = WebApplication.get().getServletContext().getContextPath();
            getLogger().info(" Redirecting to {}", path);
            throw new RedirectToUrlException(path);
        }

        String formName = Optional.ofNullable(formType).map(RequirementUtil::getTypeName).orElse(null);
        this.config = new FormPageExecutionContext(Objects.requireNonNull(context), formName);
        this.inheritParentFormData = $m.ofValue();
        this.typeName = config.getFormName();
        this.singularFormPanel = new SingularFormPanel("singular-panel");
        onBuildSingularFormPanel(singularFormPanel);

        context.getInheritParentFormData().ifPresent(inheritParentFormData::setObject);

        if (this.config.getFormName() == null) {
            throw new SingularServerException("Tipo do formulário da página nao foi definido");
        }
    }


    @Override
    protected void onInitialize() {
        super.onInitialize();

        requirementInstanceModel = $m.loadable(this::loadRequirement);

        fillTransitionControllerMap(transitionControllerMap);
        SingularSpringInjector.get().injectAll(transitionControllerMap.values());
        SingularSpringInjector.get().injectAll(transitionConfirmModalMap.values());

        modalContainer.setOutputMarkupId(true);
        modalContainer.add(new SFormModalEventListenerBehavior(modalContainer));
        singularFormPanel.setViewMode(getViewMode(config));
        singularFormPanel.setAnnotationMode(getAnnotationMode(config));
        singularFormPanel.setAnnotationClassifier(getAnnotationClassifier());
        singularFormPanel.setInstanceCreator(() -> getRequirement().resolveForm(config.getFormName()));
        singularFormPanel.setInstanceInitializer(this::onCreateInstance);
        singularFormPanel.setModalContainer(modalContainer);

        Form<?> form = new Form<>("save-form");
        form.setMultiPart(true);
        form.add(singularFormPanel);
        this.containerBehindSingularPanel = buildBehindSingularPanelContent("container-panel");
        form.add(containerBehindSingularPanel);
        form.add(modalContainer);
        BSModalBorder enviarModal = buildConfirmationModal(modalContainer, getInstanceModel());
        form.add(buildSendButton(enviarModal));
        form.add(buildSaveButton("save-btn"));
        form.add(buildFlowButtons());
        form.add(buildValidateButton());
        form.add(buildExtensionButtons());
        form.add(buildCloseButton());
        form.add(closeModal);
        form.add(buildExtraContent("extra-content"));
        add(form);
        addSaveCallBackUrl();
    }

    protected void onCreateInstance(SInstance instance) {

    }


    @Override
    protected List<String> getInitializerJavascripts() {
        List<String> list = Lists.newArrayList("FlowButtonsConfigurer.configure();");
        list.addAll(super.getInitializerJavascripts());
        return list;
    }

    protected void fillTransitionControllerMap(Map<String, TransitionController<?>> transitionControllerMap) {

    }

    protected Optional<TaskInstance> getCurrentTaskInstance() {
        return Optional.ofNullable(getRequirement().getFlowInstance()).map(FlowInstance::getCurrentTask).map(Optional::get);
    }


    protected Optional<RequirementDefinition> getSingularRequirement(@Nullable ActionContext context) {
        SingularRequirementService requirementService = SingularRequirementService.get();
        return Optional.ofNullable(requirementService.getSingularRequirement(context));
    }

    /**
     * Retorna o serviço de petição.
     */
    @Nonnull
    protected final RequirementService getRequirementService() {
        return Objects.requireNonNull(requirementService);
    }

    /**
     * Retorna o serviço de manipulação de formuluário de petição.
     */
    @Nonnull
    protected final FormRequirementService getFormRequirementService() {
        return Objects.requireNonNull(formRequirementService);
    }

    /**
     * Retorna model que contêm a petição. Se ainda não tiver inicilizado, dispara exception.
     */
    @Nonnull
    protected final IModel<RI> getRequirementModel() {
        if (requirementInstanceModel == null) {
            throw new SingularServerException("A página ainda não foi inicializada");
        }
        return requirementInstanceModel;
    }

    /**
     * Retorna a petição atual ou dispara exception se ainda não estiver configurada.
     */
    @Nonnull
    protected final RI getRequirement() {
        if (requirementInstanceModel != null && requirementInstanceModel.getObject() != null) {
            return requirementInstanceModel.getObject();
        }
        throw new SingularServerException("O requerimento (" + RequirementInstance.class.getName() + ") ainda está null");
    }

    /**
     * Retorna a petição atual.
     */
    @Nonnull
    protected final Optional<RI> getRequirementOptional() {
        if (requirementInstanceModel == null || requirementInstanceModel.getObject() == null) {
            return Optional.empty();
        }
        return Optional.of(requirementInstanceModel.getObject());
    }

    /**
     * Retorna a instância sendo editada no momento (ou dispara exception senão tiver sido inicializada).
     */
    @Nonnull
    protected final SIComposite getInstance() {
        return (SIComposite) getSingularFormPanel().getInstance();
    }


    /**
     * Retorna as configurações da página de edição de formulário.
     */
    @Nonnull
    protected FormPageExecutionContext getConfig() {
        return config;
    }


    @Override
    protected boolean isWithMenu() {
        return false;
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        configurarExibicaoNotificacoes();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptReferenceHeaderItem.forReference(new PackageResourceReference(AbstractFormPage.class, "CollapseFlowButtons.js")));
        response.render(JavaScriptReferenceHeaderItem.forReference(new PackageResourceReference(AbstractFormPage.class, "AbstractFormPage.js")));
        response.render(OnDomReadyHeaderItem.forScript(generateInitJS()));
    }

    private String generateInitJS() {
        return "\n $(function () { "
                + "\n   window.AbstractFormPage.setup(" + new JSONObject()
                .put("callbackUrl", saveFormAjaxBehavior.getCallbackUrl().toString())
                .toString(2) + "); "
                + "\n });";
    }

    /**
     * Panel that will show behind the Singular panel.
     *
     * @param id The id of the panel.
     * @return Returns the panel will be showing behind the Singular Panel.
     */
    public Component buildBehindSingularPanelContent(String id) {
        return new WebMarkupContainer(id).setVisible(false);
    }


    private Component buildExtensionButtons() {
        return new ExtensionButtonsPanel<>("extensions-buttons", requirementInstanceModel, singularFormPanel.getInstanceModel())
                .setRenderBodyOnly(true);
    }

    private RI loadRequirement() {
        RI               requirement;
        Optional<Long>   requirementId            = Optional.ofNullable(config.getRequirementId().orElse(requirementIdModel.getObject()));
        Optional<String> requirementDefinitionKey = config.getRequirementDefinitionKey();
        if (requirementId.isPresent()) {
            if (requirementDefinitionKey.isPresent()) {
                requirement = (RI) getRequirementDefinition().loadRequirement(requirementId.get());
            } else {
                requirement = requirementService.loadRequirementInstance(requirementId.get());
            }
        } else {
            RI             parentRequirement   = null;
            Optional<Long> parentRequirementId = config.getParentRequirementId();
            if (parentRequirementId.isPresent()) {
                parentRequirement = requirementService.loadRequirementInstance(parentRequirementId.get());
                requirement = (RI) getRequirementDefinition().newRequirement(getUserDetails().getApplicantId(), parentRequirement);
            } else {
                requirement = (RI) getRequirementDefinition().newRequirement(getUserDetails().getApplicantId());
            }
        }
        return requirement;
    }

    protected RequirementDefinition getRequirementDefinition() {
        return (RequirementDefinition) getConfig()
                .getRequirementDefinitionKey()
                .map(requirementService::lookupRequirementDefinition)
                .orElseGet(() ->
                        getConfig().getRequirementId()
                                .map(requirementService::lookupRequirementDefinitionForRequirementId)
                                .orElseThrow(
                                        () -> new SingularRequirementException(
                                                "Could not identify the RequirementDefinition, requirement definition key or requirement id must be provided."))
                );


    }

    private Optional<FormRequirementEntity> getFormRequirementEntity(RI requirement) {
        if (isMainForm()) {
            return formRequirementService.findFormRequirementEntityByType(requirement, getFormType());
        } else {
            return formRequirementService.findFormRequirementEntityByTypeAndTask(requirement, getFormType(),
                    requirement.getCurrentTaskOrException());
        }
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
        extraContainer.newComponent(this::buildNotificacoesModal);
        extraContainer.newComponent(this::createFeedbackAposEnvioPanel);
        getTransitionControllerMap().forEach((k, v) -> v.appendExtraContent(extraContainer));
    }

    private Component createFeedbackAposEnvioPanel(String id) {
        this.feedbackAposEnvioPanel = buildFeedbackAposEnvioPanel(id);
        if (feedbackAposEnvioPanel == null) {
            return new WebMarkupContainer(id);
        } else {
            return this.feedbackAposEnvioPanel;
        }
    }

    protected FeedbackAposEnvioPanel buildFeedbackAposEnvioPanel(String id) {
        return null;
    }

    private void onBuildSingularFormPanel(SingularFormPanel singularFormPanel) {
        singularFormPanel.setPreFormPanelFactory(this::buildPreFormPanelContent);
    }

    protected void appendBeforeFormContent(BSContainer container) {
    }


    @Nonnull
    protected Optional<String> getRequirementIdentifier() {
        return getRequirementOptional()
                .map(RequirementInstance::getCod)
                .map(Object::toString);
    }

    protected void configureCustomButtons(BSContainer<?> buttonContainer, BSContainer<?> modalContainer, boolean transitionButtonsVisible, IModel<? extends SInstance> currentInstance) {
        Optional<Long> requirementId = config.getRequirementId();
        if (requirementId.isPresent()) {
            configureDiffButton(requirementId.get(), buttonContainer, currentInstance);
        }

        Optional<TaskInstance> currentTaskInstanceOpt = getCurrentTaskInstance();
        if (!currentTaskInstanceOpt.isPresent()) {
            buttonContainer.setVisible(false).setEnabled(false);
        } else {
            configureTransitionButtons(buttonContainer, modalContainer, transitionButtonsVisible, currentInstance, currentTaskInstanceOpt.get());
        }

        appendViewNotificationsButton(buttonContainer);
    }

    private void configureDiffButton(Long requirementId, BSContainer<?> buttonContainer, IModel<? extends SInstance> currentInstance) {
        if (hasMultipleVersionsAndIsMainForm(requirementId)) {
            appendButtonViewDiff(buttonContainer, requirementId, currentInstance);
        }
    }

    private void configureTransitionButtons(BSContainer<?> buttonContainer, BSContainer<?> modalContainer, boolean transitionButtonsVisibility, IModel<? extends SInstance> currentInstance, TaskInstance taskInstance) {
        int               buttonsCount = 0;
        List<STransition> transitions  = getCurrentTaskInstance().flatMap(TaskInstance::getFlowTask).map(STask::getTransitions).orElse(Collections.emptyList());
        if (transitionButtonsVisibility && CollectionUtils.isNotEmpty(transitions)) {
            int index = 0;
            for (STransition t : transitions) {
                TransitionAccess transitionAccess = getButtonAccess(t, taskInstance);
                if (transitionAccess.isVisible()) {
                    String btnId = "flow-btn" + index;
                    buttonsCount++;
                    buildFlowTransitionButton(
                            btnId,
                            t.getName(),
                            buttonContainer,
                            modalContainer,
                            t.getName(),
                            currentInstance,
                            transitionAccess);
                }

            }
        }
        if (buttonsCount > 2) {
            this.add($b.onReadyScript(s ->
                    "$(window).resize(function(){FlowButtonsConfigurer.configure()});" +
                            "FlowButtonsConfigurer.configure();"
            ));
        }
    }

    /**
     * Verifica se existe mais de uma versão do formulário principal
     * lavando em consideração o rascunho.
     *
     * @param requirementId id da petição a ser verificada
     * @return true em caso afirmativo, false caso contrário
     */
    protected boolean hasMultipleVersionsAndIsMainForm(Long requirementId) {
        if (requirementId != null) {

            int totalVersoes = 0;

            // Verifica se existe rascunho
            RequirementInstance requirement = requirementService.loadRequirementInstance(requirementId);
            String              typeName    = RequirementUtil.getTypeName(requirement);
            if (requirement.getEntity().currentEntityDraftByType(typeName).isPresent()) {
                totalVersoes++;
            }

            // Busca o número de versões consolidadas
            Long versoesConsolidadas = formRequirementService.countVersions(requirement.getEntity().getMainForm().getCod());
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
     * @param requirementId
     * @param currentInstance
     */
    protected void appendButtonViewDiff(BSContainer<?> buttonContainer, Long requirementId, IModel<? extends SInstance> currentInstance) {
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

    protected void buildFlowTransitionButton(String buttonId, String buttonLabel, BSContainer<?> buttonContainer, BSContainer<?> modalContainer, String transitionName, IModel<? extends SInstance> instanceModel, TransitionAccess transitionButtonEnabled) {
        final FlowConfirmPanel modalType = buildFlowConfirmationModal(buttonId, modalContainer, transitionName, instanceModel);
        buildFlowButton(buttonId, buttonLabel, buttonContainer, transitionName, modalType, transitionButtonEnabled);
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
                .addLink(BSModalBorder.ButtonStyle.CANCEL, "label.button.close", new AjaxLink<Void>("cancel-btn") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
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
                        //TODO Não há necessidade desse método. [Avaliar a remoção do mesmo.] -> Validação é feita antes de chamar a modal.
                        addToastrErrorMessage("message.send.error");
                    }

                });
        tpModal.add(enviarModal);
        return enviarModal;
    }

    /**
     * Inicia o fluxo da petição, consolidando os formularios de rascunho e criando o historico
     *
     * @param mi    modal da instancia atual do formulario
     * @param ajxrt ajax request target do wicket
     * @param sm    modal que disparou a confirmação
     */
    protected void send(IModel<? extends SInstance> mi, AjaxRequestTarget ajxrt, BSModalBorder sm) {
        //executa em block try, caso exita rollback deve recarregar a peticao, para que a mesma não
        //tenha dados que sofreram rollback
        RI requirement = getRequirement();
        try {
            //usuario para persistencia
            String username = SingularSession.get().getUsername();

            //save do formulário em transação separada
            saveForm(mi.getObject());

            //executa o envio, iniciando o fluxo informado
            RequirementSubmissionResponse sendedFeedback = requirement.send(username);
            //janela de oportunidade para executar ações apos o envio, normalmente utilizado para mostrar mensagens
            onAfterSend(ajxrt, sm, sendedFeedback);
        } catch (Exception ex) {
            getLogger().error(ex.getMessage(), ex);
            getLogger().error(ex.getMessage(), ex);//recarrega a petição novamente
            getRequirementModel().setObject(requirementService.loadRequirementInstance(requirement.getCod()));
            //faz o rethrow da exeção, algumas são tratadas e exibidas na tela como mensagens informativas
            throw SingularServerException.rethrow(ex.getMessage(), ex);
        }

    }

    protected void onAfterSend(AjaxRequestTarget target, BSModalBorder enviarModal, RequirementSubmissionResponse sendedFeedback) {
        if (feedbackAposEnvioPanel != null) {
            enviarModal.hide(target);
            atualizarContentWorklist(target);
            feedbackAposEnvioPanel.show(target, sendedFeedback);
        } else {
            atualizarContentWorklist(target);
            addAfterSendSuccessMessage();
            target.appendJavaScript("; window.close();");
        }
    }

    protected void addAfterSendSuccessMessage() {
        Optional<String> identifier = getRequirementIdentifier();
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
        final STypeBasedFlowConfirmModal<?> flowConfirmModal = transitionConfirmModalMap.get(transitionName);
        if (flowConfirmModal != null) {
            boolean isFormOnModalValid = WicketFormProcessing.onFormSubmit(form, ajaxRequestTarget,
                    flowConfirmModal.getInstanceModel(), true, true);
            if (!isFormOnModalValid) {
                throw new SingularServerFormValidationError();
            }
        }
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
        RI requirement = getRequirement();

        saveForm(mi.getObject());

        //Executa em bloco try, executa rollback da petição caso exista erro
        try {
            //executa a transicao informada
            //busca os parametros da transicao atual
            requirement.executeTransition(tn);

            //executa chamada, abrindo janela de oportunidade de executar ações apos execução da transicao
            onTransitionExecuted(ajxrt, tn);
        } catch (Exception ex) {
            //recarrega a petição novamente
            getRequirementModel().setObject(requirementService.loadRequirementInstance(requirement.getCod()));
            //faz o rethrow da exeção, algumas são tratadas e exibidas na tela como mensagens informativas
            getLogger().error(ex.getMessage(), ex);
            throw SingularServerException.rethrow(ex.getMessage(), ex);
        }
    }


    protected void onTransitionExecuted(AjaxRequestTarget ajaxRequestTarget, String transitionName) {
        atualizarContentWorklist(ajaxRequestTarget);
        addToastrSuccessMessageWorklist("message.action.success", transitionName);
        closeBrowserWindow(ajaxRequestTarget);
    }

    protected void closeBrowserWindow(AjaxRequestTarget ajaxRequestTarget) {
        ajaxRequestTarget.appendJavaScript("window.close();");
    }

    protected final boolean isFlowInstanceCreated() {
        return getRequirement().isFlowInstanceCreated();
    }

    private void buildFlowButton(String buttonId,
                                 String buttonLabel,
                                 BSContainer<?> buttonContainer,
                                 String transitionName,
                                 FlowConfirmPanel confirmActionFlowModal, TransitionAccess access) {
        final TemplatePanel tp = buttonContainer.newTemplateTag(tt ->
                "<button transition='" + transitionName + " ' type='submit' class='btn flow-btn' wicket:id='" + buttonId + "'>\n <span wicket:id='flowButtonLabel' /> \n</button>\n"
        );
        final SingularButton singularButton = new SingularButton(buttonId, getFormInstance()) {
            @Override
            protected void onSubmit(AjaxRequestTarget ajaxRequestTarget, Form<?> form) {
                showConfirmModal(transitionName, confirmActionFlowModal, ajaxRequestTarget, getFormInstance());
            }

            @Override
            protected void onInitialize() {
                super.onInitialize();
                this.setEnabled(access.isEnabled());
                Optional<String> messageOpt = access.getMessage();
                if (messageOpt.isPresent()) {
                    this.add($b.attr("data-toggle", "tooltip"));
                    this.add($b.attr("data-placement", "top"));
                    this.add($b.attr("title", messageOpt.get()));
                }
            }
        };
        singularButton.add(new Label("flowButtonLabel", buttonLabel).setRenderBodyOnly(true));
        tp.add(singularButton);
    }


    private void showConfirmModal(String transitionName, FlowConfirmPanel modal, AjaxRequestTarget ajaxRequestTarget,
                                  IModel<? extends SInstance> formInstance) {
        TransitionController<?>       controller       = getTransitionControllerMap().get(transitionName);
        STypeBasedFlowConfirmModal<?> flowConfirmModal = transitionConfirmModalMap.get(transitionName);
        boolean                       show             = true;
        if (controller != null) {
            if (controller.isValidatePageForm()) {
                List<ValidationError> retrieveWarningErrors = WicketFormProcessing.retrieveWarningErrors(formInstance.getObject());
                if (CollectionUtils.isNotEmpty(retrieveWarningErrors)) {
                    modal.getModalBorder().updateWarnings(retrieveWarningErrors);
                }
            }
            SInstance transitionControllerInstance = Optional.ofNullable(flowConfirmModal).map(STypeBasedFlowConfirmModal::getInstanceModel).map(IModel::getObject).orElse(null);
            show = controller.onShow(getInstance(), transitionControllerInstance, modal.getModalBorder(), ajaxRequestTarget);
        }
        if (show) {
            modal.onShowUpdate(ajaxRequestTarget);
        }

    }

    /**
     * @param idSuffix  -> button id suffix
     * @param container -> modal container
     * @param tn        -> transition name
     * @param im        -> instance model
     * @return
     */
    private FlowConfirmPanel buildFlowConfirmationModal(String idSuffix, BSContainer<?> container, String tn, IModel<? extends SInstance> im) {
        final FlowConfirmPanel flowConfirmPanel = resolveFlowConfirmModal("confirmPanel" + idSuffix, tn);
        container.appendTag("div", flowConfirmPanel);
        return flowConfirmPanel;
    }

    /**
     * @param id             - the modal id
     * @param transitionName -> the transition name
     * @return the FlowConfirmPanel
     */
    protected FlowConfirmPanel resolveFlowConfirmModal(String id, String transitionName) {
        TransitionController<?> controller = getTransitionControllerMap().get(transitionName);
        if (controller == null || controller.getType() == null) {
            return getSimpleMessageFLowConfirmModal(id, transitionName, this);
        }
        STypeBasedFlowConfirmModal<?> modal = new STypeBasedFlowConfirmModal<RI>(
                id,
                transitionName,
                this,
                requirementInstanceModel,
                controller);
        transitionConfirmModalMap.put(transitionName, modal);
        return modal;
    }

    /**
     * Method to create the Simple Message Flow.
     * This modal is create when don't have a TransitionController for the flow.
     * <p>
     * Note: This should be overridden for customize the Modal.
     *
     * @param id             The id of modal.
     * @param transitionName The name of the transition.
     * @param formPage       The form of the page.
     * @return Instance of the Modal.
     */
    protected SimpleMessageFlowConfirmModal<RI> getSimpleMessageFLowConfirmModal(String id, String transitionName, AbstractFormPage<RI> formPage) {
        return new SimpleMessageFlowConfirmModal<>(id, transitionName, formPage);
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

    protected AnnotationClassifier getAnnotationClassifier() {
        return AtrAnnotation.DefaultAnnotationClassifier.DEFAULT_ANNOTATION;
    }

    private IReadOnlyModel<SInstance> getInstanceModel() {
        return (IReadOnlyModel<SInstance>) singularFormPanel::getInstance;
    }

    private Component buildFlowButtons() {
        BSContainer<?> buttonContainer = new BSContainer<>("custom-buttons");
        buttonContainer.setVisible(true);

        configureCustomButtons(buttonContainer, modalContainer, (ViewMode.EDIT == getViewMode(config) || AnnotationMode.EDIT == getAnnotationMode(config)), getFormInstance());

        return buttonContainer;
    }


    private Component buildSendButton(final BSModalBorder enviarModal) {
        final Component button = makeServerSendButton("send-btn", getFormInstance(), enviarModal);
        return button.add(visibleOnlyIfDraftInEditionBehaviour());
    }


    protected Component buildSaveButton(String id) {
        final Component button = new SingularButton(id, getFormInstance()) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                try {
                    eventOnSaveAction(target);
                    addToastrSuccessMessage("message.success");
                } catch (HibernateOptimisticLockingFailureException e) {
                    getLogger().debug(e.getMessage(), e);
                    addToastrErrorMessage("message.save.concurrent_error");
                }
            }
        };
        return button.add(visibleOnlyInEditionBehaviour());
    }

    protected final void eventOnSaveAction(AjaxRequestTarget target) {
        saveForm(getFormInstance().getObject());
        atualizarContentWorklist(target);
    }

    private void saveForm(SInstance instance) {
        validateUserAllocatedAndUserAction();
        getRequirement().saveForm(instance);
        requirementIdModel.setObject(getRequirement().getCod());
    }

    protected void validateUserAllocatedAndUserAction() {
        String       username     = SingularSession.get().getUsername();
        TaskInstance taskInstance = getCurrentTaskInstance().orElse(null);
        if (taskInstance != null
                && taskInstance.getAllocatedUser() != null
                && !username.equals(taskInstance.getAllocatedUser().getCodUsuario())) {
            throw new SingularServerException("O requerimento não pertence mais a este usuário.");
        }
    }

    private void addSaveCallBackUrl() {
        saveFormAjaxBehavior = new AbstractDefaultAjaxBehavior() {
            @Override
            protected void respond(AjaxRequestTarget target) {
                eventOnSaveAction(target);
            }
        };
        add(saveFormAjaxBehavior);
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
        return getViewMode(config) == ViewMode.READ_ONLY && getAnnotationMode(config) != AnnotationMode.EDIT;
    }

    protected BSModalBorder construirCloseModal() {
        BSModalBorder closeModal = new BSModalBorder("close-modal", getMessage("label.title.close.draft"));
        closeModal.addLink(BSModalBorder.ButtonStyle.CANCEL, "label.button.cancel", new AjaxLink<Void>("cancel-close-btn") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                closeModal.hide(target);
            }
        });
        closeModal.addLink(BSModalBorder.ButtonStyle.CONFIRM, "label.button.confirm", new AjaxLink<Void>("close-btn") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                target.appendJavaScript("Singular.atualizarContentWorklist();window.close();");
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

    protected Behavior visibleOnlyInEditionBehaviour() {
        return $b.visibleIf(() -> getViewMode(config).isEdition() || getAnnotationMode(config).editable());
    }

    protected Behavior visibleOnlyIfDraftInEditionBehaviour() {
        return $b.visibleIf(() -> !isFlowInstanceCreated() && getViewMode(config).isEdition());
    }

    protected IModel<? extends SInstance> getFormInstance() {
        return singularFormPanel.getInstanceModel();
    }

    public final SingularFormPanel getSingularFormPanel() {
        return singularFormPanel;
    }

    @Override
    protected IModel<String> getContentSubtitle() {
        return new Model<>();
    }

    private Map<String, TransitionController<?>> getTransitionControllerMap() {
        return transitionControllerMap;
    }

    protected void configurarExibicaoNotificacoes() {
        notificacoesModal.setVisible(!getNotificacoes().getObject().isEmpty());
    }

    protected IModel<ArrayList<Pair<String, String>>> getNotificacoes() {
        return new Model<>(new ArrayList<>());
    }

    private Component buildNotificacoesModal(String id) {

        final String        modalPanelMarkup = div("modal-panel", null, div("list-view", null, div("notificacao")));
        final TemplatePanel modalPanel       = new TemplatePanel(id, modalPanelMarkup);

        final ListView<Pair<String, String>> listView = new ListView<Pair<String, String>>("list-view", getNotificacoes()) {
            @Override
            protected void populateItem(ListItem<Pair<String, String>> item) {
                item.add(new NotificationPanel("notificacao", item.getModel()));
            }
        };

        final AjaxLink<Void> closeButton = new AjaxLink<Void>("close-button") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                notificacoesModal.hide(target);
            }
        };

        notificacoesModal = new BSModalBorder("modal-panel");
        notificacoesModal.setTitleText(Model.of("Notificações"));
        notificacoesModal.addLink(BSModalBorder.ButtonStyle.DEFAULT, Shortcuts.$m.ofValue("Fechar"), closeButton);
        notificacoesModal.setSize(BSModalBorder.Size.NORMAL);

        return modalPanel.add(notificacoesModal.add(listView));
    }

    private void appendViewNotificationsButton(final BSContainer<?> container) {
        final String markup =
                button("notificacoes", new HTMLParameters().add("type", "button").add("class", "btn"),
                        span("notificacoesLabel"), span("notificacoesBadge", new HTMLParameters().add("class", "badge").add("style", "margin-left:5px;top:-2px;"))
                );

        final TemplatePanel buttonPanel = container.newTemplateTag(tt -> markup);
        final AjaxLink<Void> viewButton = new AjaxLink<Void>("notificacoes") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                notificacoesModal.show(target);
            }
        };
        viewButton.add(new Label("notificacoesLabel", "Visualizar Notificações"));
        viewButton.add($b.visibleIf(() -> !getNotificacoes().getObject().isEmpty()));
        viewButton.add(new Label("notificacoesBadge", $m.get(() -> getNotificacoes().getObject().size())));
        buttonPanel.add(viewButton);
    }

    protected SingularRequirementUserDetails getUserDetails() {
        Session session = Session.get();
        if (session instanceof SingularSession) {
            return SingularSession.get().getUserDetails();
        }
        throw new SingularRequirementException("There is no current logged in user");
    }

    public void onConfirmTransition(String transitionName, IModel<? extends SInstance> instanceModel) {

    }


    /**
     * This button will show modal for confirmation when success,
     * and a toast if has error in validation.
     * This button have already implemented the ValidationSuccess and ValidationError.
     */
    protected static class ServerSendButton extends SingularSaveButton {

        private final BSModalBorder sendModal;

        public ServerSendButton(String id, IModel<? extends SInstance> currentInstance, BSModalBorder sendModal) {
            super(id, currentInstance);
            this.sendModal = sendModal;
        }

        @Override
        protected void onValidationSuccess(AjaxRequestTarget target, Form<?> form, IModel<? extends SInstance> instanceModel) {
            sendModal.show(target);
        }

        @Override
        protected void onValidationError(AjaxRequestTarget target, Form<?> form, IModel<? extends SInstance> instanceModel) {
            findParent(AbstractFormPage.class).addToastrErrorMessage("message.send.error");
        }

    }
}