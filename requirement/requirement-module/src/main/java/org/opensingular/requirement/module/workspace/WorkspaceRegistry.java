package org.opensingular.requirement.module.workspace;

import org.opensingular.lib.commons.base.SingularException;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.WorkspaceConfiguration;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.*;

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
            WorkspaceConfiguration cfg = new WorkspaceConfiguration(applicationContext);
            serverContext.setup(cfg);
            workspaceConfigurationMap.put(serverContext, cfg);
        } catch (InstantiationException | IllegalAccessException ex) {
            getLogger().error(ex.getMessage(), ex);
            throw SingularServerException.rethrow("Não foi possivel criar uma instancia de "+serverContextClass, ex);
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