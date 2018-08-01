package org.opensingular.requirement.module.workspace;

import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.BoxInfo;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.config.workspace.Workspace;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.LinkedHashSet;
import java.util.Set;

public class WorkspaceRegistry implements Loggable {
    private final Set<IServerContext> contexts = new LinkedHashSet<>();
    private final AnnotationConfigWebApplicationContext applicationContext;

    public WorkspaceRegistry(AnnotationConfigWebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public WorkspaceRegistry add(Class<? extends IServerContext> serverContextClass) {
        try {
            IServerContext serverContext = serverContextClass.newInstance();
            Workspace workspace = serverContext.getWorkspace();
            workspace
                    .menu()
                    .listAllBoxInfos()
                    .stream()
                    .map(BoxInfo::getBoxDefinitionClass).forEach(applicationContext::register);
            contexts.add(serverContext);
        } catch (InstantiationException | IllegalAccessException ex) {
            getLogger().error(ex.getMessage(), ex);
            throw SingularServerException.rethrow("Não foi possivel criar uma instancia de " + serverContextClass, ex);
        }
        return this;
    }

    public Set<IServerContext> listContexts() {
        return contexts;
    }
}