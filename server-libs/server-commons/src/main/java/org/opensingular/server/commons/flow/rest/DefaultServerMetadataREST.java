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

package org.opensingular.server.commons.flow.rest;

import org.opensingular.flow.core.Flow;
import org.opensingular.flow.core.MTask;
import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.form.SFormUtil;
import org.opensingular.form.SInfoType;
import org.opensingular.form.SType;
import org.opensingular.form.context.SFormConfig;
import org.opensingular.lib.commons.base.SingularUtil;
import org.opensingular.lib.support.spring.util.AutoScanDisabled;
import org.opensingular.lib.wicket.util.resource.Icone;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.config.ServerContext;
import org.opensingular.server.commons.config.SingularServerConfiguration;
import org.opensingular.server.commons.flow.action.DefaultActions;
import org.opensingular.server.commons.flow.metadata.PetitionHistoryTaskMetaDataValue;
import org.opensingular.server.commons.service.IServerMetadataREST;
import org.opensingular.server.commons.service.dto.FormDTO;
import org.opensingular.server.commons.service.dto.ItemBox;
import org.opensingular.server.commons.service.dto.MenuGroup;
import org.opensingular.server.commons.service.dto.ProcessDTO;
import org.opensingular.server.commons.spring.security.AuthorizationService;
import org.opensingular.server.commons.spring.security.PermissionResolverService;
import org.opensingular.server.commons.spring.security.SingularPermission;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.opensingular.server.commons.flow.action.DefaultActions.ASSIGN;
import static org.opensingular.server.commons.flow.rest.DefaultServerREST.COUNT_TASKS;
import static org.opensingular.server.commons.flow.rest.DefaultServerREST.SEARCH_TASKS;


@AutoScanDisabled
@RequestMapping("/rest/flow")
@RestController
public class DefaultServerMetadataREST implements IServerMetadataREST {

    @Inject
    protected SingularServerConfiguration singularServerConfiguration;

    @Inject
    protected AuthorizationService authorizationService;

    @Inject
    protected PermissionResolverService permissionResolverService;

    @Inject
    @Named("formConfigWithDatabase")
    protected SFormConfig<String> singularFormConfig;

    @Override
    public List<MenuGroup> listMenu(IServerContext context, String user) {
        List<MenuGroup> groups = listMenuGroups();
        filterAccessRight(groups, user);
        customizeMenu(groups, context, user);
        return groups;
    }

    @RequestMapping(value = PATH_LIST_MENU, method = RequestMethod.GET)
    public List<MenuGroup> listMenu(@RequestParam(MENU_CONTEXT) String context, @RequestParam(USER) String user) {
        return listMenu(IServerContext.getContextFromName(context, singularServerConfiguration.getContexts()), user);
    }

    protected List<MenuGroup> listMenuGroups() {
        final List<MenuGroup> groups = new ArrayList<>();
        getDefinitionsMap().forEach((category, definitions) -> {
            MenuGroup menuGroup = new MenuGroup();
            menuGroup.setId("BOX_" + SingularUtil.normalize(category).toUpperCase());
            menuGroup.setLabel(category);
            menuGroup.setProcesses(new ArrayList<>());
            menuGroup.setForms(new ArrayList<>());
            definitions.forEach(d -> {
                List<MTask<?>> tasks = d.getFlowMap().getTasksWithMetadata(PetitionHistoryTaskMetaDataValue.KEY);
                List<String>   allowedHistoryTasks = tasks.stream().map(MTask::getAbbreviation).collect(Collectors.toList());
                menuGroup
                                .getProcesses()
                                .add(new ProcessDTO(d.getKey(), d.getName(), null, allowedHistoryTasks));
                    }
            );
            addForms(menuGroup);
            groups.add(menuGroup);
        });
        return groups;
    }

    protected Map<String, List<ProcessDefinition>> getDefinitionsMap() {
        final Map<String, List<ProcessDefinition>> definitionMap = new HashMap<>();
        Flow.getDefinitions().forEach(d -> {
            if (!definitionMap.containsKey(d.getCategory())) {
                definitionMap.put(d.getCategory(), new ArrayList<>());
            }
            definitionMap.get(d.getCategory()).add(d);
        });
        return definitionMap;
    }

    protected void addForms(MenuGroup menuGroup) {
        for (Class<? extends SType<?>> formClass : singularServerConfiguration.getFormTypes()) {
            SInfoType          annotation = formClass.getAnnotation(SInfoType.class);
            String             name       = SFormUtil.getTypeName(formClass);
            Optional<SType<?>> sTypeOptional       = singularFormConfig.getTypeLoader().loadType(name);
            if (sTypeOptional.isPresent()) {
                SType<?>                  sType      = sTypeOptional.get();
                Class<? extends SType<?>> sTypeClass = (Class<? extends SType<?>>) sType.getClass();
                String                    label      = sType.asAtr().getLabel();
                menuGroup.getForms().add(new FormDTO(name, SFormUtil.getTypeSimpleName(sTypeClass), label, annotation.newable()));
            }
        }
    }

    protected void filterAccessRight(List<MenuGroup> groupDTOs, String user) {
        authorizationService.filterBoxWithPermissions(groupDTOs, user);
    }

    protected void customizeMenu(List<MenuGroup> groupDTOs, IServerContext menuContext, String user) {
        if (Objects.equals(ServerContext.WORKLIST.getName(), menuContext.getName())) {
            for (MenuGroup menuGroup : groupDTOs) {
                List<ItemBox> itemBoxes = new ArrayList<>();
                criarItemCaixaEntrada(itemBoxes);
                criarItemConcluidas(itemBoxes);
                menuGroup.setItemBoxes(itemBoxes);
            }
        }
    }


    protected void criarItemCaixaEntrada(List<ItemBox> itemBoxes) {
        final ItemBox caixaEntrada = new ItemBox();
        caixaEntrada.setName("Caixa de Entrada");
        caixaEntrada.setDescription("Petições aguardando ação do usuário");
        caixaEntrada.setIcone(Icone.DOCS);
        caixaEntrada.setSearchEndpoint(SEARCH_TASKS);
        caixaEntrada.setCountEndpoint(COUNT_TASKS);
        caixaEntrada.setEndedTasks(Boolean.FALSE);
        caixaEntrada.setFieldsDatatable(criarFieldsDatatableWorklist());
        caixaEntrada.addAction(ASSIGN);
        caixaEntrada.addAction(DefaultActions.ANALYSE);
        caixaEntrada.addAction(DefaultActions.RELOCATE);
        caixaEntrada.addAction(DefaultActions.VIEW);
        itemBoxes.add(caixaEntrada);
    }

    protected LinkedHashMap<String, String> criarFieldsDatatableWorklist() {
        LinkedHashMap<String, String> fields = new LinkedHashMap<>(7);
        fields.put("Número", "codPeticao");
        fields.put("Dt. de Entrada", "creationDate");
        fields.put("Solicitante", "solicitante");
        fields.put("Descrição", "description");
        fields.put("Dt. Situação", "situationBeginDate");
        fields.put("Situação", "taskName");
        fields.put("Alocado", "nomeUsuarioAlocado");
        return fields;
    }

    protected void criarItemConcluidas(List<ItemBox> itemBoxes) {
        final ItemBox concluidas = new ItemBox();
        concluidas.setName("Concluídas");
        concluidas.setDescription("Petições concluídas");
        concluidas.setIcone(Icone.DOCS);
        concluidas.setSearchEndpoint(SEARCH_TASKS);
        concluidas.setCountEndpoint(COUNT_TASKS);
        concluidas.setEndedTasks(Boolean.TRUE);
        concluidas.setFieldsDatatable(criarFieldsDatatableWorklistConcluidas());
        concluidas.addAction(DefaultActions.VIEW);
        itemBoxes.add(concluidas);
    }


    protected LinkedHashMap<String, String> criarFieldsDatatableWorklistConcluidas() {
        LinkedHashMap<String, String> fields = new LinkedHashMap<>(6);
        fields.put("Número", "codPeticao");
        fields.put("Dt. de Entrada", "creationDate");
        fields.put("Solicitante", "solicitante");
        fields.put("Descrição", "description");
        fields.put("Dt. Situação", "situationBeginDate");
        fields.put("Situação", "taskName");
        return fields;
    }


    @Override
    @RequestMapping(value = PATH_LIST_PERMISSIONS, method = RequestMethod.GET)
    public List<SingularPermission> listAllPermissions() {
        List<SingularPermission> permissions = new ArrayList<>();

        // Coleta permissões de caixa
        List<SingularPermission> menuPermissions = listMenuGroups().stream()
                .map(menuGroup -> new SingularPermission(menuGroup.getId(), null))
                .collect(Collectors.toList());

        //Agrupa permissoes do Form e do Flow
        permissions.addAll(menuPermissions);
        permissions.addAll(permissionResolverService.listAllTypePermissions());
        permissions.addAll(permissionResolverService.listAllProcessesPermissions());

        // Limpa o internal id por questão de segurança
        for (SingularPermission permission : permissions) {
            permission.setInternalId(null);
        }

        return permissions;
    }
}