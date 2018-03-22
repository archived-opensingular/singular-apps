package org.opensingular.server.commons.admin.healthsystem.extension;

import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.server.commons.admin.healthsystem.panel.HtmlToPdfPanel;


public class HtmlToPdfAdminEntry implements AdministrationEntryExtension {

    @Override
    public String name() {
        return "HtmlToPdf";
    }

    @Override
    public Panel makePanel(String id) {
        return new HtmlToPdfPanel(id);
    }
}
