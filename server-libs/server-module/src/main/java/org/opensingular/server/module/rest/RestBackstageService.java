package org.opensingular.server.module.rest;


import org.apache.commons.lang3.StringEscapeUtils;
import org.opensingular.flow.core.Flow;
import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.flow.core.STask;
import org.opensingular.form.SFormUtil;
import org.opensingular.form.SInfoType;
import org.opensingular.form.SType;
import org.opensingular.form.context.SFormConfig;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.config.SingularServerConfiguration;
import org.opensingular.server.commons.flow.actions.ActionConfig;
import org.opensingular.server.commons.flow.actions.ActionRequest;
import org.opensingular.server.commons.flow.actions.ActionResponse;
import org.opensingular.server.commons.flow.controllers.IController;
import org.opensingular.server.commons.flow.metadata.PetitionHistoryTaskMetaDataValue;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.service.PetitionInstance;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.service.dto.BoxConfigurationData;
import org.opensingular.server.commons.service.dto.FormDTO;
import org.opensingular.server.commons.service.dto.ProcessDTO;
import org.opensingular.server.commons.spring.security.AuthorizationService;
import org.opensingular.server.commons.spring.security.PermissionResolverService;
import org.opensingular.server.module.SingularModuleConfiguration;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.opensingular.server.commons.flow.actions.DefaultActions.ACTION_DELETE;

@Service
public class RestBackstageService implements Loggable {

    @Inject
    private PetitionService<PetitionEntity, PetitionInstance> petitionService;

    @Inject
    private SingularServerConfiguration singularServerConfiguration;

    @Inject
    private SingularModuleConfiguration singularModuleConfiguration;

    @Inject
    private AuthorizationService authorizationService;

    @Inject
    private PermissionResolverService permissionResolverService;

    @Inject
    @Named("formConfigWithDatabase")
    private SFormConfig<String> singularFormConfig;


    public List<BoxConfigurationData> listMenu(String context, String user) {
        return listMenu(IServerContext.getContextFromName(context, singularServerConfiguration.getContexts()), user);
    }

    /**
     * @param id
     * @param actionRequest
     * @return
     * @deprecated unificar para utilizar um action controller
     */
    @Deprecated
    public ActionResponse excluir(Long id, ActionRequest actionRequest) {
        try {
            boolean hasPermission = authorizationService.hasPermission(id, null, actionRequest.getIdUsuario(), ACTION_DELETE.getName());
            if (hasPermission) {
                petitionService.deletePetition(id);
                return new ActionResponse("Registro excluído com sucesso", true);
            } else {
                return new ActionResponse("Você não tem permissão para executar esta ação.", false);
            }
        } catch (Exception e) {
            final String msg = "Erro ao excluir o item.";
            getLogger().error(msg, e);
            return new ActionResponse(msg, false);
        }
    }

    public ActionResponse executar(Long id, ActionRequest actionRequest) {
        try {
            PetitionInstance petition = petitionService.getPetition(id);
            ProcessDefinition<?> processDefinition = petition.getProcessDefinition();

            IController controller = getActionController(processDefinition, actionRequest);
            return controller.run(petition, actionRequest);
        } catch (Exception e) {
            final String msg = String.format("Erro ao executar a ação %s para o id %d. ", StringEscapeUtils.escapeJava(actionRequest.getName()), id);
            getLogger().error(msg, e);//NOSONAR
            return new ActionResponse(msg, false);
        }

    }

    private IController getActionController(ProcessDefinition<?> processDefinition, ActionRequest actionRequest) {
        final ActionConfig           actionConfig    = processDefinition.getMetaDataValue(ActionConfig.KEY);
        Class<? extends IController> controllerClass = actionConfig.getAction(actionRequest.getName());
        if (ApplicationContextProvider.get().containsBean(controllerClass.getName())) {
            return ApplicationContextProvider.get().getBean(controllerClass);
        } else {
            return ApplicationContextProvider.get().getAutowireCapableBeanFactory().createBean(controllerClass);
        }
    }


    private List<BoxConfigurationData> listMenu(IServerContext context, String user) {
        List<BoxConfigurationData> groups = listMenuGroups();
        filterAccessRight(groups, user);
        customizeMenu(groups, context, user);
        return groups;
    }


    private List<BoxConfigurationData> listMenuGroups() {
        final List<BoxConfigurationData> groups = new ArrayList<>();
        getDefinitionsMap().forEach((category, definitions) -> {
            BoxConfigurationData boxConfigurationMetadata = new BoxConfigurationData();
            boxConfigurationMetadata.setId(permissionResolverService.buildCategoryPermission(category).getSingularId());
            boxConfigurationMetadata.setLabel(category);
            boxConfigurationMetadata.setProcesses(new ArrayList<>());
            definitions.forEach(d -> {
                        List<STask<?>> tasks = d.getFlowMap().getTasksWithMetadata(PetitionHistoryTaskMetaDataValue.KEY);
                        List<String> allowedHistoryTasks = tasks.stream().map(STask::getAbbreviation).collect(Collectors.toList());
                        boxConfigurationMetadata
                                .getProcesses()
                                .add(new ProcessDTO(d.getKey(), d.getName(), null, allowedHistoryTasks));
                    }
            );
            addForms(boxConfigurationMetadata);
            groups.add(boxConfigurationMetadata);
        });
        return groups;
    }

    private Map<String, List<ProcessDefinition>> getDefinitionsMap() {
        final Map<String, List<ProcessDefinition>> definitionMap = new HashMap<>();
        Flow.getDefinitions().forEach(d -> {
            if (!definitionMap.containsKey(d.getCategory())) {
                definitionMap.put(d.getCategory(), new ArrayList<>());
            }
            definitionMap.get(d.getCategory()).add(d);
        });
        return definitionMap;
    }

    @SuppressWarnings("unchecked")
    private void addForms(BoxConfigurationData boxConfigurationMetadata) {
        for (Class<? extends SType<?>> formClass : singularServerConfiguration.getFormTypes()) {
            SInfoType annotation = formClass.getAnnotation(SInfoType.class);
            String name = SFormUtil.getTypeName(formClass);
            Optional<SType<?>> sTypeOptional = singularFormConfig.getTypeLoader().loadType(name);
            if (sTypeOptional.isPresent()) {
                SType<?> sType = sTypeOptional.get();
                Class<? extends SType<?>> sTypeClass = (Class<? extends SType<?>>) sType.getClass();
                String label = sType.asAtr().getLabel();
                boxConfigurationMetadata.getForms().add(new FormDTO(name, SFormUtil.getTypeSimpleName(sTypeClass), label, annotation.newable()));
            }
        }
    }

    private void filterAccessRight(List<BoxConfigurationData> groupDTOs, String user) {
        authorizationService.filterBoxWithPermissions(groupDTOs, user);
    }

    private void customizeMenu(List<BoxConfigurationData> groupDTOs, IServerContext menuContext, String user) {
        for (BoxConfigurationData boxConfigurationMetadata : groupDTOs) {
            boxConfigurationMetadata.setBoxesDefinition(singularModuleConfiguration.buildItemBoxes(menuContext));
        }
    }

}
