package org.opensingular.server.p.commons.admin.healthsystem.extension;

import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.server.p.commons.admin.healthsystem.panel.CachePanel;

public class CacheAdminEntry implements AdministrationEntryExtension {
    @Override
    public String name() {
        return "Caches";
    }

    @Override
    public Panel makePanel(String id) {
        return new CachePanel(id);
    }
}