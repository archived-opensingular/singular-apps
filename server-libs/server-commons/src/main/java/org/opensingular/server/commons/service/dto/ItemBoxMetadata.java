package org.opensingular.server.commons.service.dto;

import java.io.Serializable;
import java.util.List;

public class ItemBoxMetadata implements Serializable {

    private List<RequirementMetadata> requirements;
    private ItemBox                   itemBox;

    public ItemBoxMetadata() {
    }

    public ItemBoxMetadata(ItemBox itemBox, List<RequirementMetadata> requirementsMetadata) {
        this.itemBox = itemBox;
        this.requirements = requirementsMetadata;
    }

    public List<RequirementMetadata> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<RequirementMetadata> requirements) {
        this.requirements = requirements;
    }

    public ItemBox getItemBox() {
        return itemBox;
    }

    public void setItemBox(ItemBox itemBox) {
        this.itemBox = itemBox;
    }


}
