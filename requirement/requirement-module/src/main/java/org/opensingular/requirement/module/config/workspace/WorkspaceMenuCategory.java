package org.opensingular.requirement.module.config.workspace;

import net.vidageek.mirror.dsl.Mirror;
import org.opensingular.internal.lib.support.spring.injection.SingularSpringInjector;
import org.opensingular.lib.commons.ui.Icon;
import org.opensingular.requirement.module.service.dto.ItemBox;
import org.opensingular.requirement.module.workspace.BoxDefinition;
import org.opensingular.studio.core.definition.StudioDefinition;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class WorkspaceMenuCategory {
    private final Set<WorkspaceMenuItem> workspaceMenuItens = new LinkedHashSet<>();
    private final String name;
    private Icon icon;

    public WorkspaceMenuCategory(String name) {
        this.name = name;
    }

    public WorkspaceMenuCategory addItem(Class<? extends WorkspaceMenuItem> workspaceMenuItemClass) {
        WorkspaceMenuItem workspaceMenuItem = new Mirror().on(workspaceMenuItemClass).invoke().constructor().withoutArgs();
        SingularSpringInjector.get().inject(workspaceMenuItem);
        return addItem(workspaceMenuItem);
    }

    public WorkspaceMenuCategory addItem(WorkspaceMenuItem workspaceMenuItem) {
        workspaceMenuItens.add(workspaceMenuItem);
        return this;
    }

    public <T extends BoxDefinition> WorkspaceMenuCategory addBox(Class<T> boxDefitionClass) {
        workspaceMenuItens.add(new WorkspaceMenuBoxItem(boxDefitionClass));
        return this;
    }

    public WorkspaceMenuCategory addBox(Class<? extends BoxDefinition> boxDefitionClass, Consumer<ItemBox> boxConfigurer) {
        WorkspaceMenuBoxItem workspaceMenuBoxItem = new WorkspaceMenuBoxItem(boxDefitionClass);
        boxConfigurer.accept(workspaceMenuBoxItem.getBoxDefinition().getItemBox());
        workspaceMenuItens.add(workspaceMenuBoxItem);
        return this;
    }

    public WorkspaceMenuCategory addCRUD(Class<? extends StudioDefinition> studioDefinitionClass,
                                         Consumer<WorkspaceMenuCRUDItem> crudConfigurer) {
        StudioDefinition studioDefinition = new Mirror().on(studioDefinitionClass).invoke().constructor().withoutArgs();
        SingularSpringInjector.get().inject(studioDefinition);
        WorkspaceMenuCRUDItem workspaceMenuCRUDItem = new WorkspaceMenuCRUDItem(studioDefinition);
        if (crudConfigurer != null) {
            crudConfigurer.accept(workspaceMenuCRUDItem);
        }
        workspaceMenuItens.add(workspaceMenuCRUDItem);
        return this;
    }

    public WorkspaceMenuCategory addCRUD(Class<? extends StudioDefinition> studioDefinitionClass) {
        return addCRUD(studioDefinitionClass, null);
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
                .collect(Collectors.toCollection(LinkedHashSet::new));
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