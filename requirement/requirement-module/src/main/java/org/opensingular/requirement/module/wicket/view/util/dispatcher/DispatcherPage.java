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

package org.opensingular.requirement.module.wicket.view.util.dispatcher;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.opensingular.flow.core.Flow;
import org.opensingular.flow.core.ITaskPageStrategy;
import org.opensingular.flow.core.STask;
import org.opensingular.flow.core.STaskUserExecutable;
import org.opensingular.flow.core.TaskInstance;
import org.opensingular.form.SFormUtil;
import org.opensingular.form.SType;
import org.opensingular.form.wicket.enums.ViewMode;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.RequirementDefinition;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.opensingular.requirement.module.flow.SingularRequirementTaskPageStrategy;
import org.opensingular.requirement.module.flow.SingularWebRef;
import org.opensingular.requirement.module.form.FormAction;
import org.opensingular.requirement.module.persistence.entity.form.RequirementEntity;
import org.opensingular.requirement.module.service.RequirementService;
import org.opensingular.requirement.module.service.SingularRequirementService;
import org.opensingular.requirement.module.spring.security.AuthorizationService;
import org.opensingular.requirement.module.wicket.SingularSession;
import org.opensingular.requirement.module.wicket.error.Page403;
import org.opensingular.requirement.module.wicket.view.SingularHeaderResponseDecorator;
import org.opensingular.requirement.module.wicket.view.behavior.SingularJSBehavior;
import org.opensingular.requirement.module.wicket.view.form.AbstractFormPage;
import org.opensingular.requirement.module.wicket.view.form.FormPage;
import org.opensingular.requirement.module.wicket.view.form.ReadOnlyFormPage;
import org.opensingular.requirement.module.wicket.view.template.ServerTemplate;
import org.opensingular.requirement.module.wicket.view.util.ActionContext;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.util.Optional;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;
import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

@SuppressWarnings("serial")
public class DispatcherPage extends WebPage implements Loggable {

    private final WebMarkupContainer bodyContainer = new WebMarkupContainer("body");

    @Inject
    private RequirementService requirementService;

    @Inject
    private SingularRequirementService singularRequirementService;

    @Inject
    private AuthorizationService authorizationService;

    @Inject
    private IServerContext serverContext;

    public DispatcherPage() {
        buildPage();
        dispatch(setFormNameActionContext(new ActionContext(getRequest().getOriginalUrl().getQueryString())));
    }

    /**
     * Método temporário, remover após migrar os links para usar id do requerimento ao invés do nome do form
     *
     * @param context
     * @return
     */
    //TODO REFACTOR
    @SuppressWarnings("unchecked")
    @Deprecated
    private ActionContext setFormNameActionContext(ActionContext context) {
        RequirementDefinition req = singularRequirementService.getSingularRequirement(context);
        if (req != null && !context.getFormName().isPresent()) {
            context.setFormName(SFormUtil.getTypeName((Class<? extends SType<?>>) req.getMainForm()));
        }
        return context;
    }


    private void buildPage() {
        getApplication().setHeaderResponseDecorator(new SingularHeaderResponseDecorator());
        bodyContainer
                .add(new HeaderResponseContainer("scripts", "scripts"))
                .add(new SingularJSBehavior());
        add(bodyContainer);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptReferenceHeaderItem.forReference(new PackageResourceReference(ServerTemplate.class, "singular.js")));
    }

    private Optional<SingularWebRef> retrieveSingularWebRef(ActionContext actionContext) {
        Optional<Class<? extends AbstractFormPage>> pageParam = actionContext.getFormPageClass();
        if (pageParam.isPresent()) {
            return Optional.of(new SingularWebRef(pageParam.get()));
        }
        Optional<TaskInstance> ti   = actionContext.getRequirementId().flatMap(this::findCurrentTaskByRequirementId);
        Optional<STask<?>>     task = ti.flatMap(TaskInstance::getFlowTask);
        if (task.isPresent()) {

            Optional<FormAction> formActionOpt = actionContext.getFormAction();
            if (task.get() instanceof STaskUserExecutable) {
                final ITaskPageStrategy pageStrategy = ((STaskUserExecutable) task.get()).getExecutionPage();
                if (pageStrategy instanceof SingularRequirementTaskPageStrategy) {
                    return Optional.ofNullable((SingularWebRef) pageStrategy.getPageFor(
                            ti.orElseThrow(() -> new SingularServerException("TaskInstance não encontrada")), null));
                } else {
                    getLogger().warn("Atividade atual possui uma estratégia de página não suportada. A página default será utilizada.");
                }
            } else if (formActionOpt.isPresent() && ViewMode.READ_ONLY != formActionOpt.get().getViewMode()) {
                throw new SingularServerException("Página invocada para uma atividade que não é do tipo MTaskUserExecutable");
            }
        }
        return Optional.empty();
    }

    private <T> T createNewInstanceUsingFormPageConfigConstructor(Class<? extends WebPage> clazz, ActionContext context) {
        try {
            Constructor c = clazz.getConstructor(ActionContext.class);
            return (T) c.newInstance(context);
        } catch (Exception e) {
            throw SingularServerException.rethrow(e.getMessage(), e);
        }
    }

    private WebPage retrieveDestination(ActionContext context) {
        if (isViewModeReadOnly(context) && !isAnnotationModeEdit(context)) {
            return newVisualizationPage(context);
        } else {
            return retrieveSingularWebRef(context)
                    .map(SingularWebRef::getPageClass)
                    .map(refPageClass -> retrieveDestinationUsingSingularWebRefPageClass(context, refPageClass))
                    .orElseGet(() -> createNewInstanceUsingFormPageConfigConstructor(getFormPageClass(context), context));
        }
    }

    private boolean isAnnotationModeEdit(ActionContext context) {
        return context.getFormAction().map(FormAction::isAnnotationModeEdit).orElse(Boolean.FALSE);
    }

    private boolean isViewModeReadOnly(ActionContext context) {
        return context.getFormAction().map(FormAction::isViewModeReadOnly).orElse(Boolean.FALSE);
    }

    private WebPage newVisualizationPage(ActionContext context) {
        Long    formVersionPK;
        Boolean showAnnotations;
        showAnnotations = isAnnotationModeReadOnly(context);

        Optional<Long> formVersionId = context.getFormVersionId();
        Optional<Long> requirementId = context.getRequirementId();

        if (formVersionId.isPresent()) {
            formVersionPK = formVersionId.get();
        } else if (requirementId.isPresent()) {
            RequirementEntity p = requirementService.getRequirementByCod(requirementId.get());
            formVersionPK = p.getMainForm().getCurrentFormVersionEntity().getCod();
        } else {
            formVersionPK = null;
        }

        if (formVersionPK != null) {
            return new ReadOnlyFormPage($m.ofValue(formVersionPK), $m.ofValue(showAnnotations), showCompareLastVersionButton());
        }

        throw new SingularServerException("Não foi possivel identificar qual é o formulário a ser exibido");
    }

    /**
     * This method is responsible for showing the Compare Last Version Button.
     * <p>
     * Default: the button will be invisible.
     * <p>
     * Note: Override this method for show the button.
     *
     * @return True for show, false for not.
     */
    protected boolean showCompareLastVersionButton() {
        return false;
    }

    private boolean isAnnotationModeReadOnly(ActionContext context) {
        return context.getFormAction()
                .map(FormAction::isAnnotationModeReadOnly)
                .orElse(Boolean.FALSE);
    }

    private WebPage retrieveDestinationUsingSingularWebRefPageClass(ActionContext config, Class<? extends WebPage> pageClass) {
        try {
            if (AbstractFormPage.class.isAssignableFrom(pageClass)) {
                return createNewInstanceUsingFormPageConfigConstructor(pageClass, config);
            } else {
                return pageClass.newInstance();
            }
        } catch (Exception e) {
            closeAndReloadParent();
            getLogger().error(e.getMessage(), e);
        }
        return null;
    }

    private void dispatch(ActionContext context) {
        Long    requirementId = context.getRequirementId().orElse(null);
        String  formType      = context.getFormName().orElse(null);
        String  actionName    = context.getActionName();
        boolean readonly      = !(isViewModeEdit(context) || isAnnotationModeEdit(context));
        String  idUsuario     = null;
        String  idApplicant   = null;
        if (SingularSession.exists()) {
            idUsuario = SingularSession.get().getUserDetails().getUsername();
            idApplicant = SingularSession.get().getUserDetails().getApplicantId();
        }
        if (!authorizationService.hasPermission(requirementId, formType, idUsuario, idApplicant, actionName, serverContext, readonly)) {
            redirectForbidden();
        } else {
            dispatchForDestination(context, retrieveDestination(context));
        }
    }

    private boolean isViewModeEdit(ActionContext context) {
        return context.getFormAction().map(FormAction::isViewModeEdit).orElse(Boolean.FALSE);
    }

    private void redirectForbidden() {
        setResponsePage(Page403.class);
    }

    private void dispatchForDestination(ActionContext context, WebPage destination) {
        if (destination != null) {
            configureReload(destination);
            onDispatch(destination, context);
            setResponsePage(destination);
        }
    }

    private void configureReload(WebPage destination) {
        destination.add(new Behavior() {
            @Override
            public void renderHead(Component component, IHeaderResponse response) {
                response.render(JavaScriptReferenceHeaderItem.forReference(new PackageResourceReference(ServerTemplate.class, "singular.js")));
            }
        });
        destination.add($b.onReadyScript(() -> " Singular.atualizarContentWorklist(); "));
    }

    private void closeAndReloadParent() {
        add($b.onReadyScript(() -> " Singular.atualizarContentWorklist(); window.close(); "));
    }

    /**
     * Possibilita execução de qualquer ação antes de fazer o dispatch
     *
     * @param context     config atual
     * @param destination pagina destino
     */
    protected void onDispatch(WebPage destination, ActionContext context) {
    }

    protected Optional<TaskInstance> findCurrentTaskByRequirementId(Long requirementId) {
        if (requirementId != null) {
            return requirementService.findCurrentTaskEntityByRequirementId(requirementId).map(Flow::getTaskInstance);
        } else {
            return Optional.empty();
        }
    }

    private Class<? extends AbstractFormPage> getFormPageClass(ActionContext config) {
        RequirementDefinition requirementDefinition = singularRequirementService.getSingularRequirement(config);
        if (requirementDefinition != null) {
            return requirementDefinition.getDefaultExecutionPage();
        }

        return FormPage.class;
    }

}