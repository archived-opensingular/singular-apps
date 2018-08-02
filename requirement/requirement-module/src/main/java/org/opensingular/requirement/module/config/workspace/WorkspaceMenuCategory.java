package org.opensingular.requirement.module.config.workspace;

import net.vidageek.mirror.dsl.Mirror;
import org.opensingular.internal.lib.support.spring.injection.SingularSpringInjector;
import org.opensingular.lib.commons.ui.Icon;
import org.opensingular.requirement.module.service.dto.ItemBox;
import org.opensingular.requirement.module.workspace.BoxDefinition;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class WorkspaceMenuCategory implements Serializable {
    private final Set<WorkspaceMenuItem> workspaceMenuItens = new LinkedHashSet<>();
    private final String name;
    private Icon icon;

    public WorkspaceMenuCategory(String name) {
        this.name = name;
    }

    public WorkspaceMenuCategory add(Class<? extends WorkspaceMenuItem> workspaceMenuItemClass){
        WorkspaceMenuItem workspaceMenuItem = new Mirror().on(workspaceMenuItemClass).invoke().constructor().withoutArgs();
        workspaceMenuItens.add(workspaceMenuItem);
        return this;
    }

    public WorkspaceMenuCategory addBox(Class<? extends BoxDefinition> boxDefitionClass) {
        BoxDefinition definition = internalAddBox(boxDefitionClass);
        workspaceMenuItens.add(new WorkspaceMenuBoxItem(definition));
        return this;
    }

    public WorkspaceMenuCategory addBox(Class<? extends BoxDefinition> boxDefitionClass, Consumer<ItemBox> boxConfigurer) {
        BoxDefinition definition = internalAddBox(boxDefitionClass);
        boxConfigurer.accept(definition.getItemBox());
        workspaceMenuItens.add(new WorkspaceMenuBoxItem(definition));
        return this;
    }

    private BoxDefinition internalAddBox(Class<? extends BoxDefinition> boxDefitionClass) {
        BoxDefinition definition = new Mirror().on(boxDefitionClass).invoke().constructor().withoutArgs();
        SingularSpringInjector.get().inject(definition);
        return definition;
    }

    public WorkspaceMenuCategory icon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public Set<BoxDefinition> getDefinitions() {
        return workspaceMenuItens.stream()
                .filter(i -> i instanceof WorkspaceMenuBoxItem)
                .map(i -> (WorkspaceMenuBoxItem) i)
                .map(WorkspaceMenuBoxItem::getBoxDefinition)
                .collect(Collectors.toSet());
    }

    public Set<WorkspaceMenuItem> getWorkspaceMenuItens() {
        return workspaceMenuItens;
    }

    public String getName() {
        return name;
    }

    public Icon getIcon() {
        return icon;
    }
}