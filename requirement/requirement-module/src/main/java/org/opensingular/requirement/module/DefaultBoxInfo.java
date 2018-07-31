package org.opensingular.requirement.module;

import org.opensingular.requirement.module.workspace.BoxDefinition;

import java.util.LinkedHashSet;
import java.util.Set;

public class DefaultBoxInfo implements BoxInfo {
    private final Class<? extends BoxDefinition> boxDefinitionClass;
    private final Set<Class<? extends SingularRequirement>> requirements = new LinkedHashSet<>();
    private String boxId;

    public DefaultBoxInfo(Class<? extends BoxDefinition> boxDefinitionClass) {
        this.boxDefinitionClass = boxDefinitionClass;
    }

    @Override
    public String getBoxId() {
        return boxId;
    }

    @Override
    public BoxInfo newFor(Class<? extends SingularRequirement> requirement) {
        requirements.add(requirement);
        return this;
    }

    @Override
    public Class<? extends BoxDefinition> getBoxDefinitionClass() {
        return boxDefinitionClass;
    }

    @Override
    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    @Override
    public Set<Class<? extends SingularRequirement>> getRequirements() {
        return requirements;
    }
}