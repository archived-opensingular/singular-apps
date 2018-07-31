package org.opensingular.requirement.module.config.workspace;

import org.opensingular.requirement.module.BoxInfo;
import org.opensingular.requirement.module.DefaultBoxInfo;
import org.opensingular.requirement.module.workspace.BoxDefinition;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Workspace {
    private final Set<BoxInfo> boxInfos = new LinkedHashSet<>();

    public Workspace addBox(Class<? extends BoxDefinition> boxDefitionClass) {
        BoxInfo boxInfo = new DefaultBoxInfo(boxDefitionClass);
        boxInfos.add(boxInfo);
        return this;
    }

    public Workspace addBox(Class<? extends BoxDefinition> boxDefitionClass, Consumer<BoxInfo> boxConfigurer) {
        BoxInfo boxInfo = new DefaultBoxInfo(boxDefitionClass);
        boxInfos.add(boxInfo);
        boxConfigurer.accept(boxInfo);
        return this;
    }

    public Set<BoxInfo> getBoxInfos() {
        return boxInfos;
    }
}