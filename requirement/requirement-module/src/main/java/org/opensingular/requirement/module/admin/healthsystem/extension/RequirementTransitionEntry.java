package org.opensingular.requirement.module.admin.healthsystem.extension;

import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.requirement.module.admin.healthsystem.panel.RequirementTransitionPanel;

public class RequirementTransitionEntry implements AdministrationEntryExtension {
    @Override
    public String name() {
        return "Transição - Requerimento";
    }

    @Override
    public Panel makePanel(String id) {
        return new RequirementTransitionPanel(id);
    }
}
