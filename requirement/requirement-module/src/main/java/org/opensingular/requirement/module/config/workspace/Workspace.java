package org.opensingular.requirement.module.config.workspace;

public class Workspace {
    private final WorkspaceMenu workspaceMenu = new WorkspaceMenu();

    public WorkspaceMenu menu() {
        return workspaceMenu;
    }

}