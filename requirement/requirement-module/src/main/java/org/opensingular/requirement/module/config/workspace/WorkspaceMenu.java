package org.opensingular.requirement.module.config.workspace;

import org.opensingular.requirement.module.BoxInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class WorkspaceMenu implements Serializable {
    private List<WorkspaceMenuCategory> categories = new ArrayList<>();

    public WorkspaceMenu addCategory(String name, Consumer<WorkspaceMenuCategory> categoryConfigurer) {
        WorkspaceMenuCategory category = new WorkspaceMenuCategory(name);
        categoryConfigurer.accept(category);
        categories.add(category);
        return this;
    }

    public List<BoxInfo> listAllBoxInfos() {
        return categories.stream()
                .map(WorkspaceMenuCategory::getBoxInfos)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<WorkspaceMenuCategory> getCategories() {
        return categories;
    }
}