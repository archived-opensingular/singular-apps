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

package org.opensingular.server.module.wicket.view.util.dispatcher;

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
import org.opensingular.flow.persistence.entity.AbstractTaskInstanceEntity;
import org.opensingular.flow.persistence.entity.TaskInstanceEntity;
import org.opensingular.flow.persistence.entity.TaskInstanceHistoryEntity;
import org.opensingular.form.SFormUtil;
import org.opensingular.form.SType;
import org.opensingular.form.wicket.enums.ViewMode;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.flow.SingularServerTaskPageStrategy;
import org.opensingular.server.commons.flow.SingularWebRef;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.requirement.SingularRequirement;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.service.SingularRequirementService;
import org.opensingular.server.commons.spring.security.AuthorizationService;
import org.opensingular.server.commons.spring.security.SingularUserDetails;
import org.opensingular.server.commons.wicket.SingularSession;
import org.opensingular.server.commons.wicket.error.AccessDeniedPage;
import org.opensingular.server.commons.wicket.view.SingularHeaderResponseDecorator;
import org.opensingular.server.commons.wicket.view.behavior.SingularJSBehavior;
import org.opensingular.server.commons.wicket.view.form.AbstractFormPage;
import org.opensingular.server.commons.wicket.view.form.DiffFormPage;
import org.opensingular.server.commons.wicket.view.form.ReadOnlyFormPage;
import org.opensingular.server.commons.wicket.view.template.Template;
import org.opensingular.server.commons.wicket.view.util.ActionContext;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.util.Optional;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;
import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

@SuppressWarnings("serial")
public class DispatcherPage extends WebPage implements Loggable {

    private final WebMarkupContainer bodyContainer = new WebMarkupContainer("body");

    @Inject
    private PetitionService<?, ?> petitionService;

    @Inject
    private AuthorizationService authorizationService;

    @Inject
    private SingularRequirementService singularRequirementService;

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
        SingularRequirement req = singularRequirementService.getSingularRequirement(context);
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
        response.render(JavaScriptReferenceHeaderItem.forReference(new PackageResourceReference(Template.class, "singular.js")));
    }

    private Optional<SingularWebRef> retrieveSingularWebRef(ActionContext actionContext) {
        Optional<TaskInstance> ti = actionContext.getPetitionId().flatMap(this::findCurrentTaskByPetitionId);
        Optional<STask<?>> task = ti.flatMap(TaskInstance::getFlowTask);
        if (task.isPresent()) {
            if (task.get() instanceof STaskUserExecutable) {
                final ITaskPageStrategy pageStrategy = ((STaskUserExecutable) task.get()).getExecutionPage();
                if (pageStrategy instanceof SingularServerTaskPageStrategy) {
                    return Optional.ofNullable((SingularWebRef) pageStrategy.getPageFor(ti.get(), null));
                } else {
                    getLogger().warn("Atividade atual possui uma estratégia de página não suportada. A página default será utilizada.");
                }
            } else if (!(ViewMode.READ_ONLY == actionContext.getFormAction().get().getViewMode())) {
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
        if (context.getDiffEnabled()) {
            return newDiffPage(context);
        } else if (isViewModeReadOnly(context) && !isAnnotationModeEdit(context)) {
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

    private WebPage newDiffPage(ActionContext context) {
        return new DiffFormPage(context);
    }

    private WebPage newVisualizationPage(ActionContext context) {
        Long formVersionPK;
        Boolean showAnnotations;
        showAnnotations = isAnnotationModeReadOnly(context);

        if (context.getFormVersionId().isPresent()) {
            formVersionPK = context.getFormVersionId().get();
        } else if (context.getPetitionId().isPresent()) {
            PetitionEntity p = petitionService.getPetitionByCod(context.getPetitionId().get());
            formVersionPK = p.getMainForm().getCurrentFormVersionEntity().getCod();
        } else {
            formVersionPK = null;
        }

        if (formVersionPK != null) {
            return new ReadOnlyFormPage($m.ofValue(formVersionPK), $m.ofValue(showAnnotations));
        }

        throw SingularServerException.rethrow("Não foi possivel identificar qual é o formulario a ser exibido");
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
        if (context != null && (!hasAccess(context))) {
            redirectForbidden();
        } else if (context != null) {
            dispatchForDestination(context, retrieveDestination(context));
        } else {
            closeAndReloadParent();
        }
    }

    private boolean hasAccess(ActionContext context) {

        SingularUserDetails userDetails = SingularSession.get().getUserDetails();

        boolean hasPermission = authorizationService.hasPermission(
                context.getPetitionId().orElse(null),
                context.getFormName().orElse(null),
                String.valueOf(userDetails.getUserPermissionKey()),
                context.getFormAction().map(FormAction::name).orElse(null)
        );

        // Qualquer modo de edição o usuário deve ter permissão e estar alocado na tarefa,
        // para os modos de visualização basta a permissão.
        if (isViewModeEdit(context) || isAnnotationModeEdit(context)) {
            return hasPermission && !isTaskAssignedToAnotherUser(context);
        } else {
            return hasPermission;
        }

    }

    private boolean isViewModeEdit(ActionContext context) {
        return context.getFormAction().map(FormAction::isViewModeEdit).orElse(Boolean.FALSE);
    }

    private boolean isTaskAssignedToAnotherUser(ActionContext config) {
        String username = SingularSession.get().getUsername();
        if (config.getPetitionId().isPresent()) {
            return petitionService.findCurrentTaskByPetitionId(config.getPetitionId().get())
                    .map(AbstractTaskInstanceEntity::getTaskHistory)
                    .filter(histories -> !histories.isEmpty())
                    .map(histories -> histories.get(histories.size() - 1))
                    .map(history -> history.getAllocatedUser() != null
                            && history.getEndDateAllocation() == null
                            && !username.equalsIgnoreCase(history.getAllocatedUser().getCodUsuario()))
                    .orElse(Boolean.FALSE);
        }
        return false;
    }

    private void redirectForbidden() {
        setResponsePage(AccessDeniedPage.class);
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
                response.render(JavaScriptReferenceHeaderItem.forReference(new PackageResourceReference(Template.class, "singular.js")));
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

    protected Optional<TaskInstance> findCurrentTaskByPetitionId(Long petitionId) {
        if (petitionId != null) {
            return petitionService.findCurrentTaskByPetitionId(petitionId).map(Flow::getTaskInstance);
        } else {
            return Optional.empty();
        }
    }

    private Class<? extends AbstractFormPage> getFormPageClass(ActionContext config) {
        Optional<Class<? extends AbstractFormPage<?, ?>>> formPageClass = config.getFormPageClass();
        if (formPageClass.isPresent()) {
            return formPageClass.get();
        } else {
            SingularRequirement singularRequirement = singularRequirementService.getSingularRequirement(config);
            if (singularRequirement != null) {
                return singularRequirement.getDefaultExecutionPage();
            }
        }
        return null;
    }

}