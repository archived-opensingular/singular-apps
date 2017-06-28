package org.opensingular.server.module;


import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.requirement.SingularRequirement;
import org.opensingular.server.commons.service.dto.BoxDefinitionData;
import org.opensingular.server.commons.service.dto.ItemBox;
import org.opensingular.server.module.workspace.ItemBoxFactory;

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

    public SingularRequirement getRequirementById(Long id) {
        return requirements.stream().filter(r -> Objects.equals(r.getId(), id)).map(SingularRequirementRef::getRequirement).findFirst().orElse(null);
    }

    /**
     * runs
     *
     * @param context
     * @return
     */
    public List<BoxDefinitionData> buildItemBoxes(IServerContext context) {
        return itemBoxes
                .stream()
                .filter(boxCofiguration -> boxCofiguration.getItemBoxFactory().appliesTo(context))
                .map(stringItemBoxFactoryEntry -> {
                    ItemBoxFactory factory = stringItemBoxFactoryEntry.getItemBoxFactory();
                    ItemBox itemBox = factory.build(context);
                    itemBox.setId(stringItemBoxFactoryEntry.getBoxId());
                    itemBox.setFieldsDatatable(factory.getDatatableFields());
                    return new BoxDefinitionData(itemBox, stringItemBoxFactoryEntry.getRequirementsData());
                })
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
