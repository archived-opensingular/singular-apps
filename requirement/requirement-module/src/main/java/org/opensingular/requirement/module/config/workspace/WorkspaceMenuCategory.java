package org.opensingular.requirement.module.config.workspace;

import net.vidageek.mirror.dsl.Mirror;
import org.opensingular.internal.lib.support.spring.injection.SingularSpringInjector;
import org.opensingular.lib.commons.ui.Icon;
import org.opensingular.requirement.module.service.dto.ItemBox;
import org.opensingular.requirement.module.workspace.BoxDefinition;
import org.opensingular.studio.core.definition.StudioDefinition;

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

    public WorkspaceMenuCategory add(Class<? extends WorkspaceMenuItem> workspaceMenuItemClass) {
        WorkspaceMenuItem workspaceMenuItem = new Mirror().on(workspaceMenuItemClass).invoke().constructor().withoutArgs();
        SingularSpringInjector.get().inject(workspaceMenuItem);
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
                                         Consumer<WorkspaceMenuStudioItem> crudConfigurer) {
        StudioDefinition studioDefinition = new Mirror().on(studioDefinitionClass).invoke().constructor().withoutArgs();
        SingularSpringInjector.get().inject(studioDefinition);
        WorkspaceMenuStudioItem workspaceMenuStudioItem = new WorkspaceMenuStudioItem(studioDefinition);
        if (crudConfigurer != null) {
            crudConfigurer.accept(workspaceMenuStudioItem);
        }
        workspaceMenuItens.add(workspaceMenuStudioItem);
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