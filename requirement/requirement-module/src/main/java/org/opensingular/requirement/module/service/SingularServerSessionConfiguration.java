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

package org.opensingular.requirement.module.service;

import org.assertj.core.util.VisibleForTesting;
import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.WorkspaceConfigurationMetadata;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.config.SingularServerConfiguration;
import org.opensingular.requirement.module.connector.ModuleDriver;
import org.opensingular.requirement.module.service.RequirementService;
import org.opensingular.requirement.module.service.dto.BoxConfigurationData;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
    private ModuleDriver moduleDriver;

    @PostConstruct
    public void init() {
        try {
            IServerContext menuContext = getMenuContext();
            for (ModuleEntity module : buscarCategorias()) {
                configMaps.put(module, moduleDriver.retrieveModuleWorkspace(module, menuContext));
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
        return requirementService.listAllModules();
    }

    public IServerContext getMenuContext() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = sra.getRequest();
        return IServerContext.getContextFromRequest(req, singularServerConfiguration.getContexts());
    }

    public LinkedHashMap<ModuleEntity, List<BoxConfigurationData>> getModuleBoxConfigurationMap() {
        LinkedHashMap<ModuleEntity, List<BoxConfigurationData>> map = new LinkedHashMap<>();
        List<ModuleEntity> categorias = buscarCategorias();
        for (ModuleEntity categoria : categorias) {
            final List<BoxConfigurationData> boxConfigurationMetadataDTOs = listMenus(categoria);
            map.put(categoria, boxConfigurationMetadataDTOs);
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
