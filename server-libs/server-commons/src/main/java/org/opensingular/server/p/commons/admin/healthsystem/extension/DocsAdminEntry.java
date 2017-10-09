package org.opensingular.server.p.commons.admin.healthsystem.extension;

import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.server.p.commons.admin.healthsystem.panel.DocsPanel;
import org.opensingular.server.p.commons.admin.healthsystem.panel.PermissionPanel;

public class DocsAdminEntry implements AdministrationEntryExtension {
    @Override
    public String name() {
        return "Docs Tools";
    }

    @Override
    public Panel makePanel(String id) {
        return new DocsPanel(id);
    }
}
