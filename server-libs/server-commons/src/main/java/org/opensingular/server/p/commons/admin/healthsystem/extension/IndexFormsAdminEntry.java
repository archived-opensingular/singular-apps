package org.opensingular.server.p.commons.admin.healthsystem.extension;

import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.server.p.commons.admin.healthsystem.panel.IndexPanel;

public class IndexFormsAdminEntry implements AdministrationEntryExtension {
    @Override
    public String name() {
        return "Index Forms";
    }

    @Override
    public Panel makePanel(String id) {
        return new IndexPanel(id);
    }
}
