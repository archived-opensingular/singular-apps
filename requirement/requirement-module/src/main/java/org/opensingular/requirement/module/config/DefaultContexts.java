package org.opensingular.requirement.module.config;

import org.opensingular.requirement.module.admin.AdministrationApplication;
import org.opensingular.requirement.module.config.workspace.Workspace;
import org.opensingular.requirement.module.config.workspace.WorkspaceSettings;
import org.opensingular.requirement.module.spring.security.config.SecurityConfigs;
import org.opensingular.requirement.module.wicket.application.RequirementWicketApplication;
import org.opensingular.requirement.module.wicket.application.WorklistWicketApplication;
import org.opensingular.requirement.module.workspace.DefaultDonebox;
import org.opensingular.requirement.module.workspace.DefaultDraftbox;
import org.opensingular.requirement.module.workspace.DefaultInbox;
import org.opensingular.requirement.module.workspace.DefaultOngoingbox;

public interface DefaultContexts {
    class WorklistContextWithCAS extends ServerContext {
        public static final String NAME = "WORKLIST_WITH_CAS";

        public WorklistContextWithCAS() {
            super(NAME);
        }

        @Override
        public void configure(WorkspaceSettings settings) {
            settings
                    .contextPath("/worklist/*")
                    .propertiesBaseKey("singular.worklist")
                    .wicketApplicationClass(WorklistWicketApplication.class)
                    .springSecurityConfigClass(SecurityConfigs.CASAnalise.class);
        }

        @Override
        public void configure(Workspace workspace) {
            workspace
                    .menu()
                    .addCategory("Worklist", category -> category
                            .addBox(DefaultInbox.class)
                            .addBox(DefaultDonebox.class));

        }
    }

    class RequirementContextWithCAS extends ServerContext {
        public static final String NAME = "REQUIREMENT_WITH_CAS";

        public RequirementContextWithCAS() {
            super(NAME);
        }

        @Override
        public void configure(WorkspaceSettings settings) {
            settings
                    .contextPath("/requirement/*")
                    .propertiesBaseKey("singular.requirement")
                    .wicketApplicationClass(RequirementWicketApplication.class)
                    .springSecurityConfigClass(SecurityConfigs.CASPeticionamento.class)
                    .checkOwner(true);
        }

        @Override
        public void configure(Workspace workspace) {
            workspace
                    .menu()
                    .addCategory("Requirement", category -> category
                            .addBox(DefaultDraftbox.class)
                            .addBox(DefaultOngoingbox.class));
        }
    }

    class WorklistContext extends ServerContext {
        public static final String NAME = "WORKLIST";

        public WorklistContext() {
            super(NAME);
        }

        @Override
        public void configure(WorkspaceSettings settings) {
            settings
                    .contextPath("/worklist/*")
                    .propertiesBaseKey("singular.worklist")
                    .wicketApplicationClass(WorklistWicketApplication.class)
                    .springSecurityConfigClass(SecurityConfigs.WorklistSecurity.class);
        }

        @Override
        public void configure(Workspace workspace) {
            workspace
                    .menu()
                    .addCategory("Worklist", category -> category
                            .addBox(DefaultInbox.class)
                            .addBox(DefaultDonebox.class));
        }
    }

    class RequirementContext extends ServerContext {
        public static final String NAME = "REQUIREMENT";

        public RequirementContext() {
            super(NAME);
        }

        @Override
        public void configure(WorkspaceSettings settings) {
            settings
                    .contextPath("/requirement/*")
                    .propertiesBaseKey("singular.requirement")
                    .wicketApplicationClass(RequirementWicketApplication.class)
                    .springSecurityConfigClass(SecurityConfigs.RequirementSecurity.class)
                    .checkOwner(true);
        }

        @Override
        public void configure(Workspace workspace) {
            workspace
                    .menu()
                    .addCategory("Requirement", category -> category
                            .addBox(DefaultDraftbox.class)
                            .addBox(DefaultOngoingbox.class));
        }
    }

    class AdministrationContext extends ServerContext {
        public static final String NAME = "ADMINISTRATION";

        public AdministrationContext() {
            super(NAME);
        }

        @Override
        public void configure(WorkspaceSettings settings) {
            settings
                    .contextPath("/administration/*")
                    .propertiesBaseKey("singular.administration")
                    .wicketApplicationClass(AdministrationApplication.class)
                    .springSecurityConfigClass(SecurityConfigs.AdministrationSecurity.class);
        }

        @Override
        public void configure(Workspace workspace) {
        }
    }
}