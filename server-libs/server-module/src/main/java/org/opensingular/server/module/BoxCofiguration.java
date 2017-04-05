package org.opensingular.server.module;

import org.opensingular.server.commons.service.dto.RequirementData;
import org.opensingular.server.module.workspace.ItemBoxFactory;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BoxCofiguration {

    private String                      id              = UUID.randomUUID().toString();
    private Set<SingularRequirementRef> requirementRefs = new LinkedHashSet<>();
    private ItemBoxFactory itemBoxFactory;

    public BoxCofiguration(ItemBoxFactory itemBoxFactory, List<SingularRequirementRef> requirementRefs) {
        for (SingularRequirementRef ref : requirementRefs) {
            this.requirementRefs.add(ref);
        }
        this.itemBoxFactory = itemBoxFactory;
    }

    public BoxCofiguration(ItemBoxFactory itemBox) {
        this.itemBoxFactory = itemBox;
    }

    public String getId() {
        return id;
    }

    public Set<SingularRequirementRef> getRequirementRefs() {
        return new LinkedHashSet<>(requirementRefs);
    }

    public void addRequirementRefs(SingularRequirementRef requirementRefs) {
        this.requirementRefs.add(requirementRefs);
    }

    public ItemBoxFactory getItemBoxFactory() {
        return itemBoxFactory;
    }

    public List<RequirementData> getRequirementsData() {
        List<RequirementData> result = new ArrayList<>();
        for (SingularRequirementRef ref : requirementRefs) {
            result.add(new RequirementData(ref.getId(), ref.getRequirementDescription()));
        }
        return result;
    }
}
