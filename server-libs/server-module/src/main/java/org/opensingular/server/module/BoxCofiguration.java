package org.opensingular.server.module;

import org.opensingular.server.module.workspace.ItemBoxFactory;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BoxCofiguration {

    private String                      id              = UUID.randomUUID().toString();
    private Set<SingularRequirementRef> requirementRefs = new LinkedHashSet<>();
    private ItemBoxFactory itemBoxFactory;

    public BoxCofiguration(ItemBoxFactory itemBoxFactory, List<SingularRequirementRef> requirementRefs) {
        this.requirementRefs.addAll(requirementRefs);
        this.itemBoxFactory = itemBoxFactory;
    }

    public BoxCofiguration(ItemBoxFactory itemBox) {
        this.itemBoxFactory = itemBox;
    }

    public String getId() {
        return id;
    }

    public Set<SingularRequirementRef> getRequirementRefs() {
        return requirementRefs;
    }

    public void addRequirementRefs(SingularRequirementRef requirementRefs) {
        this.requirementRefs.add(requirementRefs);
    }

    public ItemBoxFactory getItemBoxFactory() {
        return itemBoxFactory;
    }
}
