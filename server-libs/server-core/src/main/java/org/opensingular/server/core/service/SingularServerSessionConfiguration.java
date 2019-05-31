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

package org.opensingular.server.core.service;

import org.fest.util.VisibleForTesting;
import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.WorkspaceConfigurationMetadata;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.config.SingularServerConfiguration;
import org.opensingular.server.commons.connector.ModuleDriver;
import org.opensingular.server.commons.service.RequirementService;
import org.opensingular.server.commons.service.dto.BoxConfigurationData;
import org.opensingular.server.commons.spring.security.SingularUserDetails;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Named
@Scope("session")
public class SingularServerSessionConfiguration implements Loggable {

    private Map<ModuleEntity, WorkspaceConfigurationMetadata> configMaps = new HashMap<>();

    @Inject
    private RequirementService<?, ?> requirementService;

    @Inject
    private SingularServerConfiguration singularServerConfiguration;

    @Inject
    private ModuleDriver rmoduleDriver;

    @PostConstruct
    public void init() {
        try {
            IServerContext menuContext = getMenuContext();
            for (ModuleEntity module : buscarCategorias()) {
                WorkspaceConfigurationMetadata workspaceConfigurationMetadata = rmoduleDriver.retrieveModuleWorkspace(module, menuContext);
                if (workspaceConfigurationMetadata != null) {
                    configMaps.put(module, workspaceConfigurationMetadata);
                }
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
        }
    }

    public void reload() {
        configMaps.clear();
        init();
    }

    public List<ModuleEntity> buscarCategorias() {
        String moduleCod = SingularProperties.get().getProperty(SingularProperties.SINGULAR_MODULE_COD);
        if (moduleCod != null) {
            return Collections.singletonList(requirementService.findByModuleCod(moduleCod));
        } else {
            return requirementService.listAllModules();
        }
    }

    public IServerContext getMenuContext() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest       req = sra.getRequest();
        return IServerContext.getContextFromRequest(req, singularServerConfiguration.getContexts());
    }

    @SuppressWarnings("unchecked")
    private <T extends SingularUserDetails> T getUserDetails() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SingularUserDetails) {
            return (T) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return null;
    }

    public LinkedHashMap<ModuleEntity, List<BoxConfigurationData>> getModuleBoxConfigurationMap() {
        LinkedHashMap<ModuleEntity, List<BoxConfigurationData>> map        = new LinkedHashMap<>();
        List<ModuleEntity>                                      categorias = buscarCategorias();
        for (ModuleEntity categoria : categorias) {
            final List<BoxConfigurationData> boxConfigurationMetadataDTOs = listMenus(categoria);
            if (!boxConfigurationMetadataDTOs.isEmpty()) {
                map.put(categoria, boxConfigurationMetadataDTOs);
            }
        }
        return map;
    }

    private List<BoxConfigurationData> listMenus(ModuleEntity module) {
        if (configMaps.containsKey(module)) {
            return configMaps.get(module).getBoxesConfiguration();
        } else {
            return new ArrayList<>(0);
        }
    }

    /**
     * For testing purposes only. Must not be used in another scenario.
     *
     * @param configMaps
     */
    @VisibleForTesting
    void setConfigMaps(Map<ModuleEntity, WorkspaceConfigurationMetadata> configMaps) {
        this.configMaps = configMaps;
    }
}
