package org.opensingular.server.p.commons.admin.healthsystem.extension;

import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.server.p.commons.admin.healthsystem.panel.XSDViewPanel;

public class XSDViewAdminEntry implements AdministrationEntryExtension {
    @Override
    public String name() {
        return "XSD";
    }

    @Override
    public Panel makePanel(String id) {
        return new XSDViewPanel(id);
    }
}