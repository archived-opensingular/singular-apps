package org.opensingular.server.p.commons.admin.healthsystem.extension;

import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.server.p.commons.admin.healthsystem.panel.JobPanel;

public class JobsAdminEntry implements AdministrationEntryExtension {
    @Override
    public String name() {
        return "Jobs";
    }

    @Override
    public Panel makePanel(String id) {
        return new JobPanel(id);
    }
}