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
import org.apache.wicket.request.Request;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.string.StringValue;
import org.opensingular.flow.core.Flow;
import org.opensingular.flow.core.ITaskPageStrategy;
import org.opensingular.flow.core.STask;
import org.opensingular.flow.core.STaskUserExecutable;
import org.opensingular.flow.core.TaskInstance;
import org.opensingular.flow.persistence.entity.TaskInstanceEntity;
import org.opensingular.flow.persistence.entity.TaskInstanceHistoryEntity;
import org.opensingular.form.wicket.enums.AnnotationMode;
import org.opensingular.form.wicket.enums.ViewMode;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.flow.SingularServerTaskPageStrategy;
import org.opensingular.server.commons.flow.SingularWebRef;
import org.opensingular.server.commons.form.FormActions;
import org.opensingular.server.commons.metadata.SingularServerMetadata;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.service.FormPetitionService;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.service.PetitionUtil;
import org.opensingular.server.commons.spring.security.AuthorizationService;
import org.opensingular.server.commons.spring.security.SingularUserDetails;
import org.opensingular.server.commons.wicket.SingularSession;
import org.opensingular.server.commons.wicket.error.AccessDeniedPage;
import org.opensingular.server.commons.wicket.view.SingularHeaderResponseDecorator;
import org.opensingular.server.commons.wicket.view.behavior.SingularJSBehavior;
import org.opensingular.server.commons.wicket.view.form.AbstractFormPage;
import org.opensingular.server.commons.wicket.view.form.DiffFormPage;
import org.opensingular.server.commons.wicket.view.form.FormPageConfig;
import org.opensingular.server.commons.wicket.view.form.ReadOnlyFormPage;
import org.opensingular.server.commons.wicket.view.template.Template;
import org.opensingular.server.module.wicket.view.util.form.FormPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Optional;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;
import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;
import static org.opensingular.server.commons.util.DispatcherPageParameters.*;

@SuppressWarnings("serial")
public abstract class DispatcherPage extends WebPage implements Loggable {

    protected static final Logger logger = LoggerFactory.getLogger(DispatcherPage.class);

    private final WebMarkupContainer bodyContainer = new WebMarkupContainer("body");

    @Inject
    private PetitionService<?,?> petitionService;

    @Inject
    private AuthorizationService authorizationService;

    @Inject
    private FormPetitionService<?> formPetitionService;

    @Inject
    private SingularServerMetadata singularServerMetadata;

    public DispatcherPage() {
        initPage();
        dispatch(parseParameters(getRequest()));
    }

    private static boolean isMandatoryParam(String name) {
        return Arrays.asList(ACTION,
                PETITION_ID,
                FORM_VERSION_KEY,
                FORM_NAME,
                PARENT_PETITION_ID,
                DIFF).contains(name);
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

    private Optional<SingularWebRef> retrieveSingularWebRef(FormPageConfig cfg) {
        Optional<TaskInstance> ti   = findCurrentTaskByPetitionId(cfg.getPetitionId());
        Optional<STask<?>>     task = ti.flatMap(TaskInstance::getFlowTask);
        if (task.isPresent()) {
            if (task.get() instanceof STaskUserExecutable) {
                final ITaskPageStrategy pageStrategy = ((STaskUserExecutable) task.get()).getExecutionPage();
                if (pageStrategy instanceof SingularServerTaskPageStrategy) {
                    return Optional.ofNullable((SingularWebRef) pageStrategy.getPageFor(ti.get(), null));
                } else {
                    logger.warn("Atividade atual possui uma estratégia de página não suportada. A página default será utilizada.");
                }
            } else if (!(ViewMode.READ_ONLY == cfg.getViewMode())) {
                throw SingularServerException.rethrow("Página invocada para uma atividade que não é do tipo STaskUserExecutable");
            }
        }
        return Optional.empty();
    }

    private <T> T createNewInstanceUsingFormPageConfigConstructor(Class<T> clazz, FormPageConfig config) {
        try {
            Constructor c = clazz.getConstructor(FormPageConfig.class);
            return (T) c.newInstance(config);
        } catch (Exception e) {
            throw SingularServerException.rethrow(e.getMessage(), e);
        }
    }

    private WebPage retrieveDestination(FormPageConfig config) {
        if (config.isDiff()) {
            return newDiffPage(config);
        } else if (config.getViewMode().isVisualization() && !(AnnotationMode.EDIT == config.getAnnotationMode())) {
            return newVisualizationPage(config);
        } else {
            return retrieveDestinationUsingSingularWebRef(config, retrieveSingularWebRef(config));
        }
    }

    private WebPage newDiffPage(FormPageConfig config) {
        return new DiffFormPage(config);
    }

    private WebPage newVisualizationPage(FormPageConfig config) {

        Long    formVersionPK;
        Boolean showAnnotations;

        showAnnotations = config.getAnnotationMode() == AnnotationMode.READ_ONLY;

        if (config.getFormVersionPK() != null) {
            formVersionPK = config.getFormVersionPK();
        } else if (config.getPetitionId() != null) {
            PetitionEntity p = petitionService.getPetitionByCod(config.getPetitionId());
            formVersionPK = p.getMainForm().getCurrentFormVersionEntity().getCod();
        } else {
            formVersionPK = null;
        }

        if (formVersionPK != null) {
            return new ReadOnlyFormPage($m.ofValue(formVersionPK), $m.ofValue(showAnnotations));
        }

        throw SingularServerException.rethrow("Não foi possivel identificar qual é o formulario a ser exibido");
    }

    private WebPage retrieveDestinationUsingSingularWebRef(FormPageConfig config, Optional<SingularWebRef> ref) {
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
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    protected void dispatch(FormPageConfig config) {
        if (config != null
                && (!hasAccess(config))) {
            redirectForbidden();
        } else if (config != null) {
            dispatchForDestination(config, retrieveDestination(config));
        } else {
            closeAndReloadParent();
        }
    }

    protected boolean hasAccess(FormPageConfig config) {
        SingularUserDetails userDetails   = SingularSession.get().getUserDetails();
        boolean             hasPermission = authorizationService.hasPermission(config.getPetitionId(), config.getFormType(), String.valueOf(userDetails.getUserPermissionKey()), config.getFormAction().name());

        // Qualquer modo de edição o usuário deve ter permissão e estar alocado na tarefa,
        // para os modos de visualização basta a permissão.
        if (ViewMode.EDIT == config.getFormAction().getViewMode()
                || AnnotationMode.EDIT == config.getFormAction().getAnnotationMode()) {
            return hasPermission && !isTaskAssignedToAnotherUser(config);
        } else {
            return hasPermission;
        }

    }

    private boolean isTaskAssignedToAnotherUser(FormPageConfig config) {
        String username = SingularSession.get().getUsername();
        if (config.getPetitionId() != null) {

            Optional<TaskInstanceEntity> currentTask = petitionService.findCurrentTaskByPetitionId(config.getPetitionId());

            if (currentTask.isPresent() && !currentTask.get().getTaskHistory().isEmpty()) {
                TaskInstanceHistoryEntity taskInstanceHistory = currentTask.get().getTaskHistory().get(currentTask.get().getTaskHistory().size() - 1);

                return taskInstanceHistory.getAllocatedUser() != null
                        && taskInstanceHistory.getEndDateAllocation() == null
                        && !username.equalsIgnoreCase(taskInstanceHistory.getAllocatedUser().getCodUsuario());
            }
        }

        return false;
    }

    private String loadTypeNameFormFormVersionPK(Long formVersionPK) {
        return Optional.of(formVersionPK)
                .map(formPetitionService::loadFormVersionEntity)
                .map(PetitionUtil::getTypeName)
                .orElseThrow(() -> SingularServerException.rethrow("Não possivel idenfiticar o tipo"));
    }

    protected void redirectForbidden() {
        setResponsePage(AccessDeniedPage.class);
    }

    private void dispatchForDestination(FormPageConfig config, WebPage destination) {
        if (destination != null) {
            configureReload(destination);
            onDispatch(destination, config);
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

    private StringValue getParam(Request r, String key) {
        return r.getRequestParameters().getParameterValue(key);
    }

    private FormActions resolveFormAction(StringValue action) {
        return FormActions.getById(Integer.valueOf(action.toString("0")));
    }

    protected FormPageConfig parseParameters(Request r) {

        final StringValue action           = getParam(r, ACTION);
        final StringValue petitionId       = getParam(r, PETITION_ID);
        final StringValue formVersionPK    = getParam(r, FORM_VERSION_KEY);
        final StringValue formName         = getParam(r, FORM_NAME);
        final StringValue parentPetitionId = getParam(r, PARENT_PETITION_ID);
        final StringValue diffValue        = getParam(r, DIFF);

        if (action.isEmpty()) {
            String url = getRequestCycle().getUrlRenderer().renderFullUrl(getRequest().getUrl()) + singularServerMetadata.getServerBaseUrl();
            getLogger().info(" Redirecting to "+url);
            throw new RedirectToUrlException(url);
        }

        final FormActions formAction = resolveFormAction(action);

        final String  pi   = petitionId.toString("");
        final Long    fvk  = formVersionPK.isEmpty() ? null : formVersionPK.toLong();
        final boolean diff = Boolean.parseBoolean(diffValue.toOptionalString());

        String fn = null;

        if (!formName.isEmpty()) {
            fn = formName.toString();
        } else if (fvk != null) {
            fn = loadTypeNameFormFormVersionPK(fvk);
        }

        final FormPageConfig cfg = buildConfig(r, pi, formAction, fn, fvk, parentPetitionId.toOptionalString(), diff);

        addAdditionalParams(r, cfg);

        if (cfg != null && cfg.getProcessDefinition() == null && !cfg.isWithLazyProcessResolver()) {
            throw SingularServerException.rethrow("Nenhum fluxo está configurado");
        }
        return cfg;
    }

    private void addAdditionalParams(Request r, FormPageConfig cfg) {
        for (String name : r.getRequestParameters().getParameterNames()) {
            if (!isMandatoryParam(name)) {
                cfg.addAdditionalParam(name, getParam(r, name).toString());
            }
        }
    }

    protected abstract FormPageConfig buildConfig(Request r, String petitionId, FormActions formAction, String formType, Long formVersionKey, String parentPetitionId, boolean diff);

    /**
     * Possibilita execução de qualquer ação antes de fazer o dispatch
     *
     * @param destination pagina destino
     * @param config      config atual
     */
    protected void onDispatch(WebPage destination, FormPageConfig config) {
    }

    protected Optional<TaskInstance> findCurrentTaskByPetitionId(Long petitionId) {
        if (petitionId == null) {
            return Optional.empty();
        }
        return petitionService.findCurrentTaskByPetitionId(petitionId).map(Flow::getTaskInstance);
    }

    protected Class<? extends AbstractFormPage> getDefaultFormPageClass() {
        return FormPage.class;
    }

}
