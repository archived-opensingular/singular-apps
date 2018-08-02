package org.opensingular.requirement.studio.context;

import org.opensingular.requirement.module.WorkspaceConfiguration;
import org.opensingular.requirement.module.config.ServerContext;
import org.opensingular.requirement.module.spring.security.AbstractSingularSpringSecurityAdapter;
import org.opensingular.requirement.studio.wicket.RequirementStudioApplication;

public class StudioContext extends ServerContext {
    public static final String NAME = "STUDIO";

    public StudioContext() {
        super(NAME, "/*", "singular.studio");
    }

    @Override
    public Class<? extends RequirementStudioApplication> getWicketApplicationClass() {
        return RequirementStudioApplication.class;
    }

    @Override
    public Class<? extends AbstractSingularSpringSecurityAdapter> getSpringSecurityConfigClass() {
        return null;
    }

    @Override
    public void setup(WorkspaceConfiguration workspaceConfiguration) {

    }
}