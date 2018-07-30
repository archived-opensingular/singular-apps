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
import org.opensingular.requirement.module.workspace.WorkspaceRegistry;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SingularModuleConfiguration {
    private SingularModule module;
    private WorkspaceRegistry workspaceRegistry;
    private RequirementRegistry requirementRegistry;
    private List<String> publicUrls = new ArrayList<>();

    public void init(AnnotationConfigWebApplicationContext applicationContext) throws IllegalAccessException, InstantiationException {
        resolveModule();
        resolveRequirements(applicationContext);
        resolveWorkspace(applicationContext);

        for (IServerContext ctx : workspaceRegistry.listContexts()) {
            this.publicUrls.addAll(ctx.getPublicUrls());
        }
    }

    private void resolveWorkspace(AnnotationConfigWebApplicationContext applicationContext) {
        WorkspaceRegistry workspaceRegistry = new WorkspaceRegistry(applicationContext);
        module.workspace(workspaceRegistry);
        module.defaultWorkspace(workspaceRegistry);
        this.workspaceRegistry = workspaceRegistry;
    }

    private void resolveRequirements(AnnotationConfigWebApplicationContext applicationContext) {
        requirementRegistry = new RequirementRegistry(applicationContext);
        module.requirements(requirementRegistry);
    }

    private void resolveModule() throws IllegalAccessException, InstantiationException {
        Set<Class<? extends SingularModule>> modules = SingularClassPathScanner.get()
                .findSubclassesOf(SingularModule.class)
                .stream()
                .filter(f -> !Modifier.isAbstract(f.getModifiers()) || !Modifier.isInterface(f.getModifiers()))
                .collect(Collectors.toSet());

        if ((long) modules.size() != 1) {
            throw new SingularServerException(String.format("Apenas uma e somente uma implementação de %s " +
                    "é permitida por módulo. Encontradas: %s", SingularModule.class.getName(), String.valueOf(modules
                    .stream().map(Class::getName).collect(Collectors.toList()))));
        }
        Optional<Class<? extends SingularModule>> firstModule = modules.stream().findFirst();
        if (firstModule.isPresent()) {
            module = firstModule.get().newInstance();
        }
    }

    public SingularRequirement getRequirementById(Long id) {
        return requirementRegistry.getRequirements()
                .stream()
                .filter(r -> Objects.equals(r.getDefinitionCod(), id))
                .findFirst()
                .orElse(null);
    }

    public Set<BoxInfo> getBoxByContext(IServerContext context) {
        return workspaceRegistry.get(context).map(WorkspaceConfiguration::getBoxInfos).orElse(Collections.emptySet());
    }

    public Optional<BoxInfo> getBoxByBoxId(String boxId) {
        return workspaceRegistry.listConfigs()
                .stream()
                .map(WorkspaceConfiguration::getBoxInfos)
                .flatMap(Collection::stream)
                .filter(b -> b.getBoxId().equals(boxId)).findFirst();
    }

    public List<SingularRequirement> getRequirements() {
        return requirementRegistry.getRequirements();
    }

    public SingularModule getModule() {
        return module;
    }

    public List<String> getPublicUrls() {
        return publicUrls;
    }

    public void addPublicUrl(String url) {
        publicUrls.add(url);
    }

    public String getModuleCod() {
        return getModule().abbreviation();
    }

    public IServerContext findContextByName(String name) {
        return workspaceRegistry.listContexts()
                .stream()
                .filter(i -> i.getName().equals(name)).findFirst().orElse(null);
    }

    public Set<IServerContext> getContexts() {
        return workspaceRegistry.listContexts();
    }
}