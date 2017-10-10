package org.opensingular.server.module.admin.extension;

import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.server.module.admin.extension.panel.RequirementViewPanel;
import org.opensingular.server.p.commons.admin.healthsystem.extension.AdministrationEntryExtension;

/**
 * Carregado por :
 * @see org.opensingular.server.p.commons.admin.healthsystem.HealthSystemPage#loadExtensions()
 * Registrado em:
 * @see
 */
public class RequirementViewAdminEntry implements AdministrationEntryExtension {
    @Override
    public String name() {
        return "Requerimentos";
    }

    @Override
    public Panel makePanel(String id) {
        return new RequirementViewPanel(id);
    }
}