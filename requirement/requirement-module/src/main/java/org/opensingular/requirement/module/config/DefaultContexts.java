/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.requirement.module.config;

import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.lib.commons.extension.SingularExtensionUtil;
import org.opensingular.lib.commons.ui.Icon;
import org.opensingular.requirement.module.admin.AdministrationApplication;
import org.opensingular.requirement.module.admin.healthsystem.extension.AdministrationEntryExtension;
import org.opensingular.requirement.module.config.workspace.Workspace;
import org.opensingular.requirement.module.config.workspace.WorkspaceMenuItem;
import org.opensingular.requirement.module.config.workspace.WorkspaceSettings;
import org.opensingular.requirement.module.spring.security.config.SecurityConfigs;
import org.opensingular.requirement.module.wicket.application.RequirementWicketApplication;
import org.opensingular.requirement.module.wicket.application.WorklistWicketApplication;
import org.opensingular.requirement.module.workspace.DefaultDonebox;
import org.opensingular.requirement.module.workspace.DefaultDraftbox;
import org.opensingular.requirement.module.workspace.DefaultInbox;
import org.opensingular.requirement.module.workspace.DefaultOngoingbox;

import java.util.List;

public interface DefaultContexts {
    class WorklistContextWithCAS extends ServerContext {
        public static final String NAME = "WORKLIST";

        public WorklistContextWithCAS() {
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

    class RequirementContextWithCAS extends ServerContext {
        public static final String NAME = "REQUIREMENT";

        public RequirementContextWithCAS() {
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
                    .springSecurityConfigClass(SecurityConfigs.AdministrationSecurity.class)
                    .hideFromStudioMenu(true);
        }

        @Override
        public void configure(Workspace workspace) {
            List<AdministrationEntryExtension> adminEntries = SingularExtensionUtil.get()
                    .findExtensions(AdministrationEntryExtension.class);
            workspace
                    .menu()
                    .addCategory("Administração", admin -> {
                        adminEntries.stream().map(AdminWorkspaceMenuItem::new).forEach(admin::addItem);
                    });
        }
    }

    class AdminWorkspaceMenuItem implements WorkspaceMenuItem {
        private final AdministrationEntryExtension administrationEntryExtension;

        public AdminWorkspaceMenuItem(AdministrationEntryExtension administrationEntryExtension) {
            this.administrationEntryExtension = administrationEntryExtension;
        }

        @Override
        public Panel newContent(String id) {
            return administrationEntryExtension.makePanel(id);
        }

        @Override
        public Icon getIcon() {
            return null;
        }

        @Override
        public String getName() {
            return administrationEntryExtension.name();
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public String getHelpText() {
            return null;
        }
    }
}