package org.opensingular.server.module;


import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.service.dto.ItemBox;
import org.opensingular.server.module.requirement.SingularRequirement;
import org.opensingular.server.module.workspace.ItemBoxFactory;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Configuration bean from which the current module
 * requirements configurations are made available.
 */
@Named
public class SingularModuleConfiguration {

    private SingularModule module;
    private List<SingularRequirement> requirements;
    private Map<String, ItemBoxFactory> itemBoxes;

    @PostConstruct
    private void init() throws IllegalAccessException, InstantiationException {
        SingularModule module = resolveModule();
        resolveRequirements(module);
        resolveWorkspace(module);
    }


    private void resolveWorkspace(SingularModule module) {
        WorkspaceConfiguration configuration = new WorkspaceConfiguration();
        module.workspace(configuration);
        this.itemBoxes = configuration.getItemBoxes();
    }

    private void resolveRequirements(SingularModule module) {
        RequirementConfiguration configuration = new RequirementConfiguration();
        module.requirements(configuration);
        this.requirements = configuration.getRequirements();
    }

    private SingularModule resolveModule() throws IllegalAccessException, InstantiationException {
        Set<Class<? extends SingularModule>> modules = SingularClassPathScanner.INSTANCE.findSubclassesOf(SingularModule.class);
        if ((long) modules.size() != 1) {
            throw SingularServerException.rethrow(String.format("Apenas uma e somente uma implementação de %s é permitida por módulo. Encontradas: %s", SingularModule.class.getName(), String.valueOf(modules.stream().map(c -> c.getName()).collect(Collectors.toList()))));
        }
        Optional<Class<? extends SingularModule>> module = modules.stream().findFirst();
        if(module.isPresent()) {
            return this.module = module.get().newInstance();
        }
        return null;
    }

    /**
     * runs
     *
     * @param context
     * @return
     */
    public List<ItemBox> buildItemBoxes(IServerContext context) {
        return itemBoxes.entrySet()
                .stream()
                .filter(entry -> entry.getValue().appliesTo(context))
                .map(entry -> {
                    ItemBox itemBox = entry.getValue().build(context);
                    itemBox.setId(entry.getKey());
                    return itemBox;
                })
                .collect(Collectors.toList());
    }

    public Optional<ItemBoxFactory> getItemBoxFactory(String id) {
        return itemBoxes.entrySet().stream().filter(entry -> entry.getKey().equals(id)).map(Map.Entry::getValue).findFirst();
    }

}
