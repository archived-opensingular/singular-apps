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
import org.opensingular.flow.core.MTask;
import org.opensingular.flow.core.MTaskUserExecutable;
import org.opensingular.flow.core.TaskInstance;
import org.opensingular.flow.persistence.entity.TaskInstanceEntity;
import org.opensingular.flow.persistence.entity.TaskInstanceHistoryEntity;
import org.opensingular.form.SFormUtil;
import org.opensingular.form.SType;
import org.opensingular.form.wicket.enums.AnnotationMode;
import org.opensingular.form.wicket.enums.ViewMode;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.flow.SingularServerTaskPageStrategy;
import org.opensingular.server.commons.flow.SingularWebRef;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.service.PetitionService;
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
import org.opensingular.server.module.SingularModule;
import org.opensingular.server.module.SingularModuleConfiguration;
import org.opensingular.server.module.SingularRequirementRef;
import org.opensingular.server.module.requirement.SingularRequirement;
import org.opensingular.server.module.wicket.view.util.form.FormPage;

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


    public DispatcherPage() {
        initPage();
        dispatch(setFormNameActionContext(new ActionContext(getRequest().getOriginalUrl().getQueryString())));
    }

    /**
     * Método temporário, remover após migrar os links para usar id do requerimento ao invés do nome do form
     * @return
     * @param context
     */
    //TODO REFACTOR
    @SuppressWarnings("unchecked")
    @Deprecated
    private ActionContext setFormNameActionContext(ActionContext context){
        SingularRequirement req = ApplicationContextProvider.get().getBean(SingularModuleConfiguration.class).getRequirementById(context.getRequirementId().orElse(null));
        if (req != null && !context.getFormName().isPresent()) {
            context.setFormName(SFormUtil.getTypeName((Class<? extends SType<?>>) req.getMainForm()));
        }
        return context;
    }

    private void initPage() {
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
        Optional<TaskInstance> ti   = findCurrentTaskByPetitionId(actionContext.getPetitionId());
        Optional<MTask<?>>     task = ti.flatMap(TaskInstance::getFlowTask);
        if (task.isPresent()) {
            if (task.get() instanceof MTaskUserExecutable) {
                final ITaskPageStrategy pageStrategy = ((MTaskUserExecutable) task.get()).getExecutionPage();
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
        } else if (context.getFormAction().get().getViewMode().isVisualization() && !(AnnotationMode.EDIT == context.getFormAction().get().getAnnotationMode())) {
            return newVisualizationPage(context);
        } else {
            return retrieveDestinationUsingSingularWebRef(context, retrieveSingularWebRef(context));
        }
    }

    private WebPage newDiffPage(ActionContext context) {
        return new DiffFormPage(context);
    }

    private WebPage newVisualizationPage(ActionContext context) {

        Long    formVersionPK;
        Boolean showAnnotations;

        showAnnotations = context.getFormAction().get().getAnnotationMode() == AnnotationMode.READ_ONLY;

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

    private WebPage retrieveDestinationUsingSingularWebRef(ActionContext config, Optional<SingularWebRef> ref) {
        try {
            if (!ref.map(SingularWebRef::getPageClass).isPresent()) {
                return createNewInstanceUsingFormPageConfigConstructor(getFormPageClass(config), config);
            } else if (AbstractFormPage.class.isAssignableFrom(ref.get().getPageClass())) {
                return createNewInstanceUsingFormPageConfigConstructor(ref.get().getPageClass(), config);
            } else {
                return ref.get().getPageClass().newInstance();
            }
        } catch (Exception e) {
            closeAndReloadParent();
            getLogger().error(e.getMessage(), e);
        }
        return null;
    }

    protected void dispatch(ActionContext context) {
        if (context != null && (!hasAccess(context))) {
            redirectForbidden();
        } else if (context != null) {
            dispatchForDestination(context, retrieveDestination(context));
        } else {
            closeAndReloadParent();
        }
    }

    protected boolean hasAccess(ActionContext context) {
        SingularUserDetails userDetails   = SingularSession.get().getUserDetails();
        boolean             hasPermission = authorizationService.hasPermission(context.getPetitionId().orElse(null), context.getFormName().get(), String.valueOf(userDetails.getUserPermissionKey()), context.getFormAction().map(FormAction::name).orElse(null));

        // Qualquer modo de edição o usuário deve ter permissão e estar alocado na tarefa,
        // para os modos de visualização basta a permissão.
        if (ViewMode.EDIT == context.getFormAction().get().getViewMode()
                || AnnotationMode.EDIT == context.getFormAction().get().getAnnotationMode()) {
            return hasPermission && !isTaskAssignedToAnotherUser(context);
        } else {
            return hasPermission;
        }

    }



    private boolean isTaskAssignedToAnotherUser(ActionContext config) {
        String username = SingularSession.get().getUsername();
        if (config.getPetitionId().isPresent()) {

            Optional<TaskInstanceEntity> currentTask = petitionService.findCurrentTaskByPetitionId(config.getPetitionId().get());

            if (currentTask.isPresent() && !currentTask.get().getTaskHistory().isEmpty()) {
                TaskInstanceHistoryEntity taskInstanceHistory = currentTask.get().getTaskHistory().get(currentTask.get().getTaskHistory().size() - 1);

                return taskInstanceHistory.getAllocatedUser() != null
                        && taskInstanceHistory.getEndDateAllocation() == null
                        && !username.equalsIgnoreCase(taskInstanceHistory.getAllocatedUser().getCodUsuario());
            }
        }

        return false;
    }


    protected void redirectForbidden() {
        setResponsePage(AccessDeniedPage.class);
    }

    private void dispatchForDestination(ActionContext context, WebPage destination) {
        if (destination != null) {
            configureReload(destination);
            onDispatch(destination, context);
            setResponsePage(destination);
        }
    }

    protected void configureReload(WebPage destination) {
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
     * @param context
     */
    protected void onDispatch(WebPage destination, ActionContext context) {
    }

    protected Optional<TaskInstance> findCurrentTaskByPetitionId(Optional<Long> petitionId) {
        if (petitionId.isPresent()) {
            return petitionService.findCurrentTaskByPetitionId(petitionId.get()).map(Flow::getTaskInstance);
        } else {
            return Optional.empty();
        }
    }

    protected Class<? extends AbstractFormPage> getFormPageClass(ActionContext config) {
        return FormPage.class;
    }

}
