package org.opensingular.server.module;


import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.service.dto.ItemBox;
import org.opensingular.server.commons.service.dto.ItemBoxData;
import org.opensingular.server.module.requirement.SingularRequirement;
import org.opensingular.server.module.workspace.ItemBoxFactory;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.List;
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
    private List<BoxCofiguration>        itemBoxes;

    @PostConstruct
    private void init() throws IllegalAccessException, InstantiationException {
        SingularModule           module                   = resolveModule();
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
        Set<Class<? extends SingularModule>> modules = SingularClassPathScanner.INSTANCE.findSubclassesOf(SingularModule.class);
        if (modules.stream().count() != 1) {
            throw new SingularServerException(String.format("Apenas uma e somente uma implementação de %s é permitida por módulo. Encontradas: %s", SingularModule.class.getName(), String.valueOf(modules.stream().map(c -> c.getName()).collect(Collectors.toList()))));
        }
        this.module = modules.stream().findFirst().get().newInstance();
        return this.module;
    }

    public SingularRequirement getRequirementById(String id) {
        return requirements.stream().filter(r -> r.getId().equals(id)).map(SingularRequirementRef::getRequirement).findFirst().orElse(null);
    }

    /**
     * runs
     *
     * @param context
     * @return
     */
    public List<ItemBoxData> buildItemBoxes(IServerContext context) {
        return itemBoxes
                .stream()
                .filter(boxCofiguration -> boxCofiguration.getItemBoxFactory().appliesTo(context))
                .map(stringItemBoxFactoryEntry -> {
                    ItemBoxFactory factory = stringItemBoxFactoryEntry.getItemBoxFactory();
                    ItemBox itemBox = factory.build(context);
                    itemBox.setId(stringItemBoxFactoryEntry.getId());
                    itemBox.setFieldsDatatable(factory.getDatatableFields());
                    return new ItemBoxData(itemBox, stringItemBoxFactoryEntry.getRequirementsData());
                })
                .collect(Collectors.toList());
    }

    public Optional<ItemBoxFactory> getItemBoxFactory(String id) {
        return itemBoxes.stream().filter(boxCofiguration -> boxCofiguration.getId().equals(id)).map(BoxCofiguration::getItemBoxFactory).findFirst();
    }

}
