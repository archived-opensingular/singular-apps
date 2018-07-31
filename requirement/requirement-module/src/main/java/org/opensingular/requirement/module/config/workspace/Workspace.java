package org.opensingular.requirement.module.config.workspace;

import org.opensingular.requirement.module.SingularRequirement;
import org.opensingular.requirement.module.workspace.BoxDefinition;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Workspace {
    private Map<Class<? extends BoxDefinition>, Set<Class<? extends SingularRequirement>>> boxAndRequirements = new LinkedHashMap<>();
    private Set<Class<? extends SingularRequirement>> latestSetOfRequirementsAdded;

    public Workspace addBox(Class<? extends BoxDefinition> boxDefitionClass) {
        latestSetOfRequirementsAdded = new LinkedHashSet<>();
        boxAndRequirements.put(boxDefitionClass, latestSetOfRequirementsAdded);
        return this;
    }

    @SafeVarargs
    public final Workspace newFor(Class<? extends SingularRequirement>... singularRequirementClasses) {
        latestSetOfRequirementsAdded.addAll(Arrays.asList(singularRequirementClasses));
        return this;
    }

    public Map<Class<? extends BoxDefinition>, Set<Class<? extends SingularRequirement>>> getBoxAndRequirements() {
        return boxAndRequirements;
    }
}