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

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Configuration bean from which the current module
 * requirements configurations are made available.
 */
@Named
public class SingularModuleConfiguration {

    private SingularModule               module;
    private List<SingularRequirementRef> requirements;
    private List<BoxController>          itemBoxes;

    @PostConstruct
    private void init() throws IllegalAccessException, InstantiationException {
        module = resolveModule();
        RequirementConfiguration requirementConfiguration = resolveRequirements(module);
        resolveWorkspace(module, requirementConfiguration);
    }


    private WorkspaceConfiguration resolveWorkspace(SingularModule module, RequirementConfiguration requirementConfiguration) {
        WorkspaceConfiguration configuration = new WorkspaceConfiguration(requirementConfiguration);
        module.workspace(configuration);
        this.itemBoxes = configuration.getItemBoxes();
        return configuration;
    }

    private RequirementConfiguration resolveRequirements(SingularModule module) {
        RequirementConfiguration configuration = new RequirementConfiguration();
        module.requirements(configuration);
        this.requirements = configuration.getRequirements();
        return configuration;
    }

    private SingularModule resolveModule() throws IllegalAccessException, InstantiationException {
        Set<Class<? extends SingularModule>> modules = SingularClassPathScanner.get().findSubclassesOf(SingularModule.class);
        if (modules.stream().count() != 1) {
            throw new SingularServerException(String.format("Apenas uma e somente uma implementação de %s é permitida por módulo. Encontradas: %s", SingularModule.class.getName(), String.valueOf(modules.stream().map(c -> c.getName()).collect(Collectors.toList()))));
        }
        SingularModule module = null;
        if (modules.stream().findFirst().isPresent()) {
            Optional<Class<? extends SingularModule>> first = modules.stream().findFirst();
            if (first.isPresent()) {
                module = first.get().newInstance();
            }
        }
        return module;
    }

    public RequirementDefinition getRequirementById(Long id) {
        return requirements.stream().filter(r -> Objects.equals(r.getId(), id)).map(SingularRequirementRef::getRequirement).findFirst().orElse(null);
    }

    /**
     * runs
     *
     * @param context
     * @return
     */
    public List<BoxDefinitionData> buildItemBoxes(IServerContext context) {
        return getBoxControllerByContext(context)
                .stream()
                .map(boxController -> buildBoxDefinitionData(boxController, context))
                .collect(Collectors.toList());
    }

    public BoxDefinitionData buildBoxDefinitionData(BoxController boxController, IServerContext context) {
        BoxDefinition factory = boxController.getBoxDefinition();
        ItemBox itemBox = factory.build(context);
        itemBox.setFieldsDatatable(factory.getDatatableFields());
        itemBox.setId(boxController.getBoxId());
        return new BoxDefinitionData(itemBox, boxController.getRequirementsData());
    }

    public List<BoxController> getBoxControllerByContext(IServerContext context) {
        return itemBoxes
                .stream()
                .filter(boxCofiguration -> boxCofiguration.getBoxDefinition().appliesTo(context))
                .collect(Collectors.toList());
    }

    public Optional<BoxController> getBoxControllerByBoxId(String boxId) {
        return itemBoxes.stream().filter(b -> b.getBoxId().equals(boxId)).findFirst();
    }


    public List<SingularRequirementRef> getRequirements() {
        return requirements;
    }

    public SingularModule getModule() {
        return module;
    }
}
