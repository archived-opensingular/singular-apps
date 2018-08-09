package org.opensingular.requirement.module.config.workspace;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WorkspaceMenu {
    private List<WorkspaceMenuCategory> categories = new ArrayList<>();

    public WorkspaceMenu addCategory(String name, Consumer<WorkspaceMenuCategory> categoryConfigurer) {
        WorkspaceMenuCategory category = new WorkspaceMenuCategory(name);
        categoryConfigurer.accept(category);
        categories.add(category);
        return this;
    }

    public List<WorkspaceMenuCategory> getCategories() {
        return categories;
    }
}