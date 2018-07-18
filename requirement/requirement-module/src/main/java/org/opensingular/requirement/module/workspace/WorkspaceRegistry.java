package org.opensingular.requirement.module.workspace;

import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.RequirementConfiguration;
import org.opensingular.requirement.module.WorkspaceConfiguration;
import org.opensingular.requirement.module.config.IServerContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.*;

public class WorkspaceRegistry implements Loggable {
    private final RequirementConfiguration requirementConfiguration;
    private final Map<IServerContext, WorkspaceConfiguration> workspaceConfigurationMap;
    private final AnnotationConfigWebApplicationContext applicationContext;

    public WorkspaceRegistry(RequirementConfiguration requirementConfiguration, AnnotationConfigWebApplicationContext applicationContext) {
        this.requirementConfiguration = requirementConfiguration;
        this.workspaceConfigurationMap = new LinkedHashMap<>();
        this.applicationContext = applicationContext;
    }

    public WorkspaceConfiguration add(Class<? extends IServerContext> serverContextClass) {
        WorkspaceConfiguration cfg = new WorkspaceConfiguration(requirementConfiguration, applicationContext);
        try {
            workspaceConfigurationMap.put(serverContextClass.newInstance(), cfg);
        } catch (InstantiationException | IllegalAccessException ex) {
            getLogger().error(ex.getMessage(), ex);
        }
        return cfg;
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