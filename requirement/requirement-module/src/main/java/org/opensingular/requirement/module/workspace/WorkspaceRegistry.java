package org.opensingular.requirement.module.workspace;

import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.SingularRequirement;
import org.opensingular.requirement.module.WorkspaceConfiguration;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.config.workspace.Workspace;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class WorkspaceRegistry implements Loggable {
    private final Map<IServerContext, WorkspaceConfiguration> workspaceConfigurationMap;
    private final AnnotationConfigWebApplicationContext applicationContext;

    public WorkspaceRegistry(AnnotationConfigWebApplicationContext applicationContext) {
        this.workspaceConfigurationMap = new LinkedHashMap<>();
        this.applicationContext = applicationContext;
    }

    public WorkspaceRegistry add(Class<? extends IServerContext> serverContextClass) {
        try {
            IServerContext serverContext = serverContextClass.newInstance();
            Workspace workspace = serverContext.getWorkspace();
            WorkspaceConfiguration cfg = new WorkspaceConfiguration(applicationContext);
            for (Map.Entry<Class<? extends BoxDefinition>, Set<Class<? extends SingularRequirement>>> entry
                    : workspace.getBoxAndRequirements().entrySet()) {
                cfg.addBox(entry.getKey());
                entry.getValue().forEach(cfg::newFor);
            }
            workspaceConfigurationMap.put(serverContext, cfg);
        } catch (InstantiationException | IllegalAccessException ex) {
            getLogger().error(ex.getMessage(), ex);
            throw SingularServerException.rethrow("Não foi possivel criar uma instancia de " + serverContextClass, ex);
        }
        return this;
    }

    public Optional<WorkspaceConfiguration> get(IServerContext serverContext) {
        return Optional.ofNullable(workspaceConfigurationMap.get(serverContext));
    }

    public Collection<WorkspaceConfiguration> listConfigs() {
        return workspaceConfigurationMap.values();
    }

    public Set<IServerContext> listContexts() {
        return workspaceConfigurationMap.keySet();
    }
}