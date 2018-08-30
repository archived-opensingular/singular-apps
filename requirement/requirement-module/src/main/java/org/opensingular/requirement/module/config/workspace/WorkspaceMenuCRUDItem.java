package org.opensingular.requirement.module.config.workspace;

import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.lib.commons.ui.Icon;
import org.opensingular.studio.core.definition.StudioDefinition;
import org.opensingular.studio.core.panel.CrudShell;

public class WorkspaceMenuCRUDItem implements WorkspaceMenuItem {
    private final StudioDefinition studioDefinition;
    private Icon icon;
    private String description;
    private String helpText;
    private String title;

    public WorkspaceMenuCRUDItem(StudioDefinition studioDefinition) {
        this.studioDefinition = studioDefinition;
    }

    @Override
    public Panel newContent(String id) {
        return new CrudShell(id, studioDefinition);
    }

    @Override
    public Icon getIcon() {
        return icon;
    }

    @Override
    public String getName() {
        if (title != null) {
            return title;
        }
        return studioDefinition.getTitle();
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getHelpText() {
        return helpText;
    }

    public WorkspaceMenuCRUDItem icon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public WorkspaceMenuCRUDItem description(String description) {
        this.description = description;
        return this;
    }

    public WorkspaceMenuCRUDItem helpText(String helpText) {
        this.helpText = helpText;
        return this;
    }

    public WorkspaceMenuCRUDItem title(String title) {
        this.title = title;
        return this;
    }
}