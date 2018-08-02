package org.opensingular.studio.core.panel;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.studio.core.definition.StudioDefinition;

public class CrudShellPanel extends Panel {
    private final StudioDefinition studioDefinition;

    public CrudShellPanel(String id, StudioDefinition studioDefinition) {
        super(id);
        this.studioDefinition = studioDefinition;
        add(new DocumentoSeiForm());
    }

    private class DocumentoSeiForm extends Form<Void> {
        public DocumentoSeiForm() {
            super("form");
            add(new CrudShell("crudShell", studioDefinition));
        }
    }
}