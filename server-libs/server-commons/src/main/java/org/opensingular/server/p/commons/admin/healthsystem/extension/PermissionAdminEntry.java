package org.opensingular.server.p.commons.admin.healthsystem.extension;

import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.server.p.commons.admin.healthsystem.panel.PermissionPanel;

public class PermissionAdminEntry implements AdministrationEntryExtension {
    @Override
    public String name() {
        return "Permissions";
    }

    @Override
    public Panel makePanel(String id) {
        return new PermissionPanel(id);
    }
}
