package org.opensingular.requirement.module.config.workspace;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.opensingular.lib.commons.ui.Icon;
import org.opensingular.requirement.module.wicket.box.BoxContent;
import org.opensingular.requirement.module.workspace.BoxDefinition;

public class WorkspaceMenuBoxItem implements WorkspaceMenuItem {
    private final BoxDefinition boxDefinition;

    public WorkspaceMenuBoxItem(BoxDefinition boxDefinition) {
        this.boxDefinition = boxDefinition;
    }

    @Override
    public Panel newContent(String id) {
        return new BoxContent(id, new Model<>(boxDefinition));
    }

    @Override
    public Icon getIcon() {
        return boxDefinition.getItemBox().getIcone();
    }

    @Override
    public String getName() {
        return boxDefinition.getItemBox().getName();
    }

    @Override
    public String getDescription() {
        return boxDefinition.getItemBox().getDescription();
    }

    @Override
    public String getHelpText() {
        return boxDefinition.getItemBox().getHelpText();
    }

    public BoxDefinition getBoxDefinition() {
        return boxDefinition;
    }
}