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
import org.opensingular.requirement.module.config.workspace.Workspace;
import org.opensingular.requirement.module.config.workspace.WorkspaceMenu;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.opensingular.requirement.module.workspace.WorkspaceRegistry;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SingularModuleConfiguration {
    /**
     * O modulo singular
     */
    private SingularModule module;

    /**
     * Os dados de workspace
     */
    private WorkspaceRegistry workspaceRegistry;

    public void init(AnnotationConfigWebApplicationContext applicationContext) throws IllegalAccessException, InstantiationException {
        resolveModule();
        resolveRequirements(applicationContext);
        resolveWorkspace(applicationContext);
    }

    private void resolveWorkspace(AnnotationConfigWebApplicationContext applicationContext) {
        WorkspaceRegistry workspaceRegistry = new WorkspaceRegistry(applicationContext);
        module.workspace(workspaceRegistry);
        module.defaultWorkspace(workspaceRegistry);
        this.workspaceRegistry = workspaceRegistry;
    }

    /**
     * TODO, mover para {@link WorkspaceAppInitializerListener}
     */
    private void resolveRequirements(AnnotationConfigWebApplicationContext applicationContext) {
        RequirementRegistry requirementRegistry = new RequirementRegistry(applicationContext);
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

    public List<BoxInfo> listBoxByContext(IServerContext context) {
        return context.getWorkspace().menu().listAllBoxInfos();
    }

    public Optional<BoxInfo> getBoxByBoxId(String boxId) {
        return workspaceRegistry.listContexts()
                .stream()
                .map(IServerContext::getWorkspace)
                .map(Workspace::menu)
                .map(WorkspaceMenu::listAllBoxInfos)
                .flatMap(Collection::stream)
                .filter(b -> b.getBoxId().equals(boxId)).findFirst();
    }

    public SingularModule getModule() {
        return module;
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