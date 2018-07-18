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

package org.opensingular.requirement.module;


import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.opensingular.requirement.module.service.dto.BoxDefinitionData;
import org.opensingular.requirement.module.service.dto.ItemBox;
import org.opensingular.requirement.module.workspace.BoxDefinition;
import org.opensingular.requirement.module.workspace.WorkspaceRegistry;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;


public class SingularModuleConfiguration {

    public static String SERVLET_ATTRIBUTE_SGL_MODULE_CONFIG = "Singular-SingularModuleConfiguration";

    private SingularModule module;
    private WorkspaceRegistry workspaceRegistry;
    private RequirementConfiguration requirementConfiguration;

    public void init(AnnotationConfigWebApplicationContext applicationContext) throws IllegalAccessException, InstantiationException {
        resolveModule();
        resolveRequirements();
        resolveWorkspace(applicationContext);
    }

    private void resolveWorkspace(AnnotationConfigWebApplicationContext applicationContext) {
        WorkspaceRegistry workspaceRegistry = new WorkspaceRegistry(requirementConfiguration, applicationContext);
        module.workspace(workspaceRegistry);
        module.defaultWorkspace(workspaceRegistry);
        this.workspaceRegistry = workspaceRegistry;
    }

    private void resolveRequirements() {
        requirementConfiguration = new RequirementConfiguration();
        module.requirements(requirementConfiguration);
    }

    private void resolveModule() throws IllegalAccessException, InstantiationException {
        Set<Class<? extends SingularModule>> modules = SingularClassPathScanner.get()
                .findSubclassesOf(SingularModule.class)
                .stream()
                .filter(f -> !Modifier.isAbstract(f.getModifiers()) || !Modifier.isInterface(f.getModifiers()))
                .collect(Collectors.toSet());

        if ((long) modules.size() != 1) {
            throw new SingularServerException(String.format("Apenas uma e somente uma implementação de %s é permitida por módulo. Encontradas: %s", SingularModule.class.getName(), String.valueOf(modules.stream().map(c -> c.getName()).collect(Collectors.toList()))));
        }
        Optional<Class<? extends SingularModule>> firstModule = modules.stream().findFirst();
        if (firstModule.isPresent()) {
            module = firstModule.get().newInstance();
        }
    }

    public SingularRequirement getRequirementById(Long id) {
        return requirementConfiguration.getRequirements()
                .stream()
                .filter(r -> Objects.equals(r.getId(), id))
                .map(SingularRequirementRef::getRequirement)
                .findFirst()
                .orElse(null);
    }

    public List<BoxInfo> getBoxByContext(IServerContext context) {
        return workspaceRegistry.get(context).map(WorkspaceConfiguration::getBoxInfos).orElse(Collections.emptyList());
    }

    public Optional<BoxInfo> getBoxByBoxId(String boxId) {
        return workspaceRegistry.listConfigs()
                .stream()
                .map(WorkspaceConfiguration::getBoxInfos)
                .flatMap(Collection::stream)
                .filter(b -> b.getBoxId().equals(boxId)).findFirst();
    }

    public List<SingularRequirementRef> getRequirements() {
        return requirementConfiguration.getRequirements();
    }

    public SingularModule getModule() {
        return module;
    }

    public Set<IServerContext> getContexts() {
        return workspaceRegistry.listContexts();
    }
}
