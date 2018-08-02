package org.opensingular.requirement.module.workspace;

import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.exception.SingularServerException;

import java.util.LinkedHashSet;
import java.util.Set;

public class WorkspaceRegistry implements Loggable {
    private final Set<IServerContext> contexts = new LinkedHashSet<>();

    public WorkspaceRegistry add(Class<? extends IServerContext> serverContextClass) {
        try {
            contexts.add(serverContextClass.newInstance());
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