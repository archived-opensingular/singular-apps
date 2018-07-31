package org.opensingular.requirement.studio.context;

import org.opensingular.requirement.module.config.ServerContext;
import org.opensingular.requirement.module.config.workspace.Workspace;
import org.opensingular.requirement.module.config.workspace.WorkspaceSettings;
import org.opensingular.requirement.studio.wicket.RequirementStudioApplication;

public class StudioContext extends ServerContext {
    public static final String NAME = "STUDIO";

    public StudioContext() {
        super(NAME);
    }

    @Override
    public void configure(WorkspaceSettings settings) {
        settings
                .contextPath("/*")
                .propertiesBaseKey("singular.studio")
                .wicketApplicationClass(RequirementStudioApplication.class)
                .springSecurityConfigClass(null);
    }

    @Override
    public void configure(Workspace workspace) {
    }
}