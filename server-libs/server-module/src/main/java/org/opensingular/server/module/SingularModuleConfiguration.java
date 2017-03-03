package org.opensingular.server.module;


import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.service.dto.ItemBox;
import org.opensingular.server.module.requirement.SingularRequirement;
import org.opensingular.server.module.workspace.SingularItemBox;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Named
public class SingularModuleConfiguration {

    private SingularModule            module;
    private List<SingularRequirement> requirements;
    private List<SingularItemBox>     itemBoxes;

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
        if (modules.stream().count() != 1) {
            throw SingularServerException.rethrow(String.format("Apenas uma e somente uma implementação de %s é permitida por módulo. Encontradas: %s", SingularModule.class.getName(), String.valueOf(modules.stream().map(c -> c.getName()).collect(Collectors.toList()))));
        }

        this.module = modules.stream().findFirst().get().newInstance();
        return this.module;
    }

    public List<ItemBox> buildItemBoxes(IServerContext context) {
        return itemBoxes
                .stream()
                .filter(sib -> sib.appliesTo(context))
                .map(sib -> sib.build(context))
                .collect(Collectors.toList());
    }


}
