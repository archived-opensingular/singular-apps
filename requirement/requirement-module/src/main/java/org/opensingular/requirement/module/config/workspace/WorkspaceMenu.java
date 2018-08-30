package org.opensingular.requirement.module.config.workspace;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * The WorkspaceMenu
 */
public class WorkspaceMenu {
    /**
     * All root categories
     */
    private List<WorkspaceMenuCategory> categories = new ArrayList<>();

    /**
     * Add a category to the menu
     *
     * @param name               the item name
     * @param categoryConfigurer hook to setup the category
     * @return the current menu
     */
    public WorkspaceMenu addCategory(String name, Consumer<WorkspaceMenuCategory> categoryConfigurer) {
        WorkspaceMenuCategory category = new WorkspaceMenuCategory(name);
        categoryConfigurer.accept(category);
        categories.add(category);
        return this;
    }

    /**
     * @return all added categories
     */
    public List<WorkspaceMenuCategory> getCategories() {
        return categories;
    }
}