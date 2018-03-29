/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.requirement.module.rest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.opensingular.flow.core.Flow;
import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.form.SFormUtil;
import org.opensingular.form.SInfoType;
import org.opensingular.form.SType;
import org.opensingular.form.context.SFormConfig;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.requirement.commons.box.BoxItemDataList;
import org.opensingular.requirement.commons.box.action.ActionRequest;
import org.opensingular.requirement.commons.box.action.ActionResponse;
import org.opensingular.requirement.commons.config.IServerContext;
import org.opensingular.requirement.commons.config.SingularServerConfiguration;
import org.opensingular.requirement.commons.exception.SingularServerException;
import org.opensingular.requirement.commons.flow.controllers.IController;
import org.opensingular.requirement.commons.persistence.entity.form.RequirementEntity;
import org.opensingular.requirement.commons.persistence.filter.QuickFilter;
import org.opensingular.requirement.commons.service.RequirementInstance;
import org.opensingular.requirement.commons.service.RequirementService;
import org.opensingular.requirement.commons.service.dto.BoxConfigurationData;
import org.opensingular.requirement.commons.service.dto.FormDTO;
import org.opensingular.requirement.commons.service.dto.RequirementDefinitionDTO;
import org.opensingular.requirement.commons.spring.security.AuthorizationService;
import org.opensingular.requirement.commons.spring.security.PermissionResolverService;
import org.opensingular.requirement.module.BoxController;
import org.opensingular.requirement.module.SingularModuleConfiguration;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ModuleBackstageService implements Loggable {

    @Inject
    private RequirementService<RequirementEntity, RequirementInstance> requirementService;

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


    public ActionResponse executar(Long id, ActionRequest actionRequest) {
        try {
            IController controller = getActionController(actionRequest);
            return controller.run(requirementService.getRequirement(id), actionRequest);
        } catch (Exception e) {
            final String msg = String.format("Erro ao executar a ação %s para o id %d. ", StringEscapeUtils.escapeJava(actionRequest.getAction().getName()), id);
            getLogger().error(msg, e);//NOSONAR
            return new ActionResponse(msg, false);
        }

    }

    private IController getActionController(ActionRequest actionRequest) {
        try {
            return ApplicationContextProvider.get().getBean(actionRequest.getAction().getController());
        } catch (Exception e) {
            throw SingularServerException.rethrow(e.getMessage(), e);
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
                boxConfigurationMetadata.getProcesses().add(
                        new RequirementDefinitionDTO(d.getKey(), d.getName(), null));
            });
            addForms(boxConfigurationMetadata);
            groups.add(boxConfigurationMetadata);
        });
        return groups;
    }

    private Map<String, List<FlowDefinition>> getDefinitionsMap() {
        final Map<String, List<FlowDefinition>> definitionMap = new HashMap<>();
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
                String label = sType.asAtr().getLabel();
                boxConfigurationMetadata.getForms().add(new FormDTO(name, sType.getNameSimple(), label, annotation.newable()));
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

    public Long count(String boxId, QuickFilter filter) {
        Optional<BoxController> boxController = singularModuleConfiguration.getBoxControllerByBoxId(boxId);
        if (boxController.isPresent()) {
            return boxController.get().countItens(filter);
        }
        return 0l;
    }

    public BoxItemDataList search(String boxId, QuickFilter filter) {
        Optional<BoxController> boxController = singularModuleConfiguration.getBoxControllerByBoxId(boxId);
        if (boxController.isPresent()) {
            return boxController.get().searchItens(filter);
        }
        return new BoxItemDataList();
    }

    public List<Actor> listAllowedUsers(Map<String, Object> selectedTask) {
        return requirementService.listAllowedUsers(selectedTask);
    }
}
