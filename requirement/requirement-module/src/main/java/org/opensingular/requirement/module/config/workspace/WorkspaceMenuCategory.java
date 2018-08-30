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

/**
 * The menu category, used to define the elements of the menu.
 */
public class WorkspaceMenuCategory {

    /**
     * The category itens
     */
    private final Set<WorkspaceMenuItem> workspaceMenuItens = new LinkedHashSet<>();

    /**
     * The Category name
     */
    private final String name;

    /**
     * The Category icon
     */
    private Icon icon;

    /**
     * @param name the category name
     */
    public WorkspaceMenuCategory(String name) {
        this.name = name;
    }

    /**
     * Adds a raw WorkspaceMenuItem, this method will instantiate the class use {@link SingularSpringInjector} to
     * resolve any dependency. Note that the workspaceMenuItemClass will note become a spring bean.
     * <p>
     * Use this to add any custom {@link WorkspaceMenuItem}
     *
     * @param workspaceMenuItemClass the menu item class
     * @return the current WorkspaceMenuCategory
     */
    public WorkspaceMenuCategory addItem(Class<? extends WorkspaceMenuItem> workspaceMenuItemClass) {
        WorkspaceMenuItem workspaceMenuItem = new Mirror().on(workspaceMenuItemClass).invoke().constructor().withoutArgs();
        SingularSpringInjector.get().inject(workspaceMenuItem);
        return addItem(workspaceMenuItem);
    }

    /**
     * Adds a raw WorkspaceMenuItem.
     * <p>
     * Use this to add any custom {@link WorkspaceMenuItem}
     *
     * @param workspaceMenuItem the created item
     * @return the current WorkspaceMenuCategory
     */
    public WorkspaceMenuCategory addItem(WorkspaceMenuItem workspaceMenuItem) {
        workspaceMenuItens.add(workspaceMenuItem);
        return this;
    }

    /**
     * Adds a Singular Box to the menu.
     *
     * @param boxDefitionClass the box
     * @return the current WorkspaceMenuCategory
     */
    public WorkspaceMenuCategory addBox(Class<? extends BoxDefinition> boxDefitionClass) {
        workspaceMenuItens.add(new WorkspaceMenuBoxItem(boxDefitionClass));
        return this;
    }

    /**
     * Adds a Singular Box to the menu.
     *
     * @param boxDefitionClass the box
     * @param boxConfigurer wheere the BoxIten can be customized
     * @return the current WorkspaceMenuCategory
     */
    public WorkspaceMenuCategory addBox(Class<? extends BoxDefinition> boxDefitionClass, Consumer<ItemBox> boxConfigurer) {
        WorkspaceMenuBoxItem workspaceMenuBoxItem = new WorkspaceMenuBoxItem(boxDefitionClass);
        boxConfigurer.accept(workspaceMenuBoxItem.getBoxDefinition().getItemBox());
        workspaceMenuItens.add(workspaceMenuBoxItem);
        return this;
    }

    /**
     * Adds a StudioItem to the menu
     *
     * @param studioDefinitionClass the definition class
     * @param crudConfigurer the configurer
     * @return the current WorkspaceMenuCategory
     */
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

    /**
     * Adds a StudioItem to the menu
     *
     * @param studioDefinitionClass the definition class
     * @return the current WorkspaceMenuCategory
     */
    public WorkspaceMenuCategory addCRUD(Class<? extends StudioDefinition> studioDefinitionClass) {
        return addCRUD(studioDefinitionClass, null);
    }

    /**
     * Sets the category icon
     * @param icon the icon
     * @return the current WorkspaceMenuCategory
     */
    public WorkspaceMenuCategory icon(Icon icon) {
        this.icon = icon;
        return this;
    }

    /**
     * Lookup all box definitions inside this category
     * @return the found definitions
     */
    public Set<BoxDefinition> getDefinitions() {
        return workspaceMenuItens.stream()
                .filter(i -> i instanceof WorkspaceMenuBoxItem)
                .map(i -> (WorkspaceMenuBoxItem) i)
                .map(WorkspaceMenuBoxItem::getBoxDefinition)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * @return the category elements
     */
    public Set<WorkspaceMenuItem> getWorkspaceMenuItens() {
        return workspaceMenuItens;
    }

    /**
     * @return the category name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the category icon
     */
    public Icon getIcon() {
        return icon;
    }
}