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

public class WorkspaceMenuCategory implements Serializable {
    private final Set<BoxDefinition> definitions = new LinkedHashSet<>();
    private final String name;
    private Icon icon;

    public WorkspaceMenuCategory(String name) {
        this.name = name;
    }

    public WorkspaceMenuCategory addBox(Class<? extends BoxDefinition> boxDefitionClass) {
        BoxDefinition definition = internalAddBox(boxDefitionClass);
        definitions.add(definition);
        return this;
    }

    public WorkspaceMenuCategory addBox(Class<? extends BoxDefinition> boxDefitionClass, Consumer<ItemBox> boxConfigurer) {
        BoxDefinition definition = internalAddBox(boxDefitionClass);
        boxConfigurer.accept(definition.getItemBox());
        definitions.add(definition);
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
        return definitions;
    }

    public String getName() {
        return name;
    }

    public Icon getIcon() {
        return icon;
    }
}