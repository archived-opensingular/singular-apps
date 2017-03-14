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

package org.opensingular.server.commons.spring.security;

import org.opensingular.flow.core.Flow;
import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.flow.core.SingularFlowConfigurationBean;
import org.opensingular.form.SFormUtil;
import org.opensingular.lib.commons.base.SingularUtil;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.cache.SingularCacheForever;
import org.opensingular.server.commons.cache.SingularSessionCache;
import org.opensingular.server.commons.config.SingularServerConfiguration;
import org.opensingular.server.commons.flow.actions.ActionConfig;
import org.opensingular.server.commons.flow.actions.ActionDefinition;
import org.opensingular.server.commons.form.FormActions;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.service.PetitionInstance;
import org.opensingular.server.commons.service.AbstractPetitionService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Classe responsável por resolver as permissões do usuário em permissões do singular
 */
public class PermissionResolverService implements Loggable {

    @Inject
    protected AbstractPetitionService<PetitionEntity, PetitionInstance> petitionService;

    @Inject
    @Named("peticionamentoUserDetailService")
    private SingularUserDetailsService peticionamentoUserDetailService;

    @Inject
    private SingularServerConfiguration singularServerConfiguration;

    @Inject
    private Optional<SingularFlowConfigurationBean> singularFlowConfigurationBean;


    @SingularSessionCache
    public List<SingularPermission> searchPermissions(String idUsuario) {
        return peticionamentoUserDetailService.searchPermissions(idUsuario);
    }

    @SingularCacheForever
    public List<? extends SingularPermission> listAllCategoryPermissions(){
        return Flow.getDefinitions().stream().map( d -> buildCategoryPermission(d.getCategory())).distinct().collect(Collectors.toList());
    }

    @SingularCacheForever
    public List<? extends SingularPermission> listAllTypePermissions() {
        List<SingularPermission> permissions = new ArrayList<>();

        List<String> typeNames = listAllTypeNames();

        for (String typeName : typeNames) {
            for (FormActions action : FormActions.values()) {
                String singularId = action + "_" + typeName;
                permissions.add(new SingularPermission(singularId, null));
            }
        }

        return permissions;
    }

    @SingularCacheForever
    private List<String> listAllTypeNames() {
        return singularServerConfiguration.getFormTypes()
                .stream()
                .map(clazz -> SFormUtil.getTypeSimpleName(clazz).toUpperCase())
                .collect(Collectors.toList());
    }

    @SingularCacheForever
    public List<? extends SingularPermission> listAllProcessesPermissions() {
        List<SingularPermission> permissions = new ArrayList<>();

        if (singularFlowConfigurationBean.isPresent()) {
            for (ProcessDefinition pd : singularFlowConfigurationBean.get().getDefinitions()) {
                permissions.addAll(listPermissions(pd.getClass()));
            }
        }

        return permissions;
    }

    @SingularCacheForever
    private List<? extends SingularPermission> listPermissions(Class<? extends ProcessDefinition> clazz) {
        ProcessDefinition processDefinition = Flow.getProcessDefinition(clazz);
        ActionConfig actionConfig      = (ActionConfig) processDefinition.getMetaDataValue(ActionConfig.KEY);

        if (actionConfig == null) {
            return Collections.emptyList();
        }

        List<ActionDefinition> actions = new ArrayList<>();
        actions.addAll(actionConfig.getDefaultActions());
        actions.addAll(actionConfig.getCustomActions().keySet());

        return actions.stream()
                .map(n -> buildActionPermission(n.getName(), processDefinition.getKey()))
                .collect(Collectors.toList());
    }

    public SingularPermission buildCategoryPermission(String cateogryName) {
        return new SingularPermission("BOX_" + SingularUtil.normalize(cateogryName).toUpperCase(), null);
    }

    private SingularPermission buildActionPermission(String actionName, String processName) {
        String singularId = "ACTION_" + actionName + "_" + processName;
        singularId = singularId.toUpperCase();
        return new SingularPermission(singularId, null);
    }


}
