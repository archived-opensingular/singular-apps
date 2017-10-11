package org.opensingular.server.p.commons.admin.healthsystem.extension;

import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.server.p.commons.admin.healthsystem.panel.DbPanel;

public class DatabaseTablesAdminEntry implements AdministrationEntryExtension {
    @Override
    public String name() {
        return "Database - Tabelas";
    }

    @Override
    public Panel makePanel(String id) {
        return new DbPanel(id);
    }
}