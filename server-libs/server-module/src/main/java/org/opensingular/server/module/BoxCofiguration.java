package org.opensingular.server.module;

import org.opensingular.server.commons.service.dto.RequirementData;
import org.opensingular.server.module.workspace.ItemBoxFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BoxCofiguration {

    private String                              id              = UUID.randomUUID().toString();
    private Map<String, SingularRequirementRef> requirementRefs = new LinkedHashMap<>();
    private ItemBoxFactory itemBoxFactory;

    public BoxCofiguration(ItemBoxFactory itemBoxFactory, List<SingularRequirementRef> requirementRefs) {
        for (SingularRequirementRef ref : requirementRefs) {
            this.requirementRefs.put(UUID.randomUUID().toString(), ref);
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
        return new LinkedHashSet<>(requirementRefs.values());
    }

    public void addRequirementRefs(SingularRequirementRef requirementRefs) {
        this.requirementRefs.put(UUID.randomUUID().toString(), requirementRefs);
    }

    public ItemBoxFactory getItemBoxFactory() {
        return itemBoxFactory;
    }

    public List<RequirementData> getRequirementsData() {
        List<RequirementData> result = new ArrayList<>();
        for (Map.Entry<String, SingularRequirementRef> ref : requirementRefs.entrySet()) {
            result.add(new RequirementData(ref.getKey(), ref.getValue().getRequirementDescription()));
        }
        return result;
    }
}
