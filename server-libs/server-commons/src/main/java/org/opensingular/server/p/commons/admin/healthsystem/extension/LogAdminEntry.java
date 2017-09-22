package org.opensingular.server.p.commons.admin.healthsystem.extension;

import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.server.p.commons.admin.healthsystem.panel.LogPanel;

public class LogAdminEntry implements AdministrationEntryExtension {
    @Override
    public String name() {
        return "Logs";
    }

    @Override
    public Panel makePanel(String id) {
        return new LogPanel(id);
    }
}