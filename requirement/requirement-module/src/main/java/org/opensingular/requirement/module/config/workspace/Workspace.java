package org.opensingular.requirement.module.config.workspace;

import java.io.Serializable;

public class Workspace implements Serializable {
    private final WorkspaceMenu workspaceMenu = new WorkspaceMenu();

    public WorkspaceMenu menu() {
        return workspaceMenu;
    }

}