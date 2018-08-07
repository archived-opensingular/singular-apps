package org.opensingular.requirement.module.config.workspace;

import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.lib.commons.ui.Icon;
import org.opensingular.studio.core.definition.StudioDefinition;
import org.opensingular.studio.core.panel.CrudShellPanel;

public class WorkspaceMenuStudioItem implements WorkspaceMenuItem {
    private final StudioDefinition studioDefinition;
    private Icon icon;
    private String description;
    private String helpText;
    private String title;

    public WorkspaceMenuStudioItem(StudioDefinition studioDefinition) {
        this.studioDefinition = studioDefinition;
    }

    @Override
    public Panel newContent(String id) {
        return new CrudShellPanel(id, studioDefinition);
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

    public WorkspaceMenuStudioItem icon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public WorkspaceMenuStudioItem description(String description) {
        this.description = description;
        return this;
    }

    public WorkspaceMenuStudioItem helpText(String helpText) {
        this.helpText = helpText;
        return this;
    }

    public WorkspaceMenuStudioItem title(String title) {
        this.title = title;
        return this;
    }
}