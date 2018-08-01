package org.opensingular.requirement.module.config.workspace;

import org.opensingular.lib.commons.ui.Icon;
import org.opensingular.requirement.module.BoxInfo;
import org.opensingular.requirement.module.DefaultBoxInfo;
import org.opensingular.requirement.module.workspace.BoxDefinition;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

public class WorkspaceMenuCategory implements Serializable {
    private final Set<BoxInfo> boxInfos = new LinkedHashSet<>();
    private final String name;
    private Icon icon;


    public WorkspaceMenuCategory(String name) {
        this.name = name;
    }

    public WorkspaceMenuCategory addBox(Class<? extends BoxDefinition> boxDefitionClass) {
        BoxInfo boxInfo = new DefaultBoxInfo(boxDefitionClass);
        boxInfos.add(boxInfo);
        return this;
    }

    public WorkspaceMenuCategory addBox(Class<? extends BoxDefinition> boxDefitionClass, Consumer<BoxInfo> boxConfigurer) {
        BoxInfo boxInfo = new DefaultBoxInfo(boxDefitionClass);
        boxInfos.add(boxInfo);
        boxConfigurer.accept(boxInfo);
        return this;
    }

    public WorkspaceMenuCategory icon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public Set<BoxInfo> getBoxInfos() {
        return boxInfos;
    }

    public String getName() {
        return name;
    }

    public Icon getIcon() {
        return icon;
    }
}