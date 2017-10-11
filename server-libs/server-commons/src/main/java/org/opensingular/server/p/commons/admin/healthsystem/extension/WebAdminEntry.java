package org.opensingular.server.p.commons.admin.healthsystem.extension;

import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.server.p.commons.admin.healthsystem.panel.WebPanel;

public class WebAdminEntry implements AdministrationEntryExtension {
    @Override
    public String name() {
        return "Web";
    }

    @Override
    public Panel makePanel(String id) {
        return new WebPanel(id);
    }
}
