package org.opensingular.requirement.module.config.workspace;

/**
 * The Singular Workspace, where is defined the context appearance and behaviour
 */
public class Workspace {
    /**
     * The workspace menu, displayed as a sidebar
     */
    private final WorkspaceMenu workspaceMenu = new WorkspaceMenu();

    /**
     * Gets the menu, use this do add menu entries
     * @return the context menu
     */
    public WorkspaceMenu menu() {
        return workspaceMenu;
    }
}