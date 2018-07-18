package org.opensingular.requirement.module;

import org.opensingular.requirement.module.service.dto.RequirementData;
import org.opensingular.requirement.module.workspace.BoxDefinition;

import java.util.ArrayList;
import java.util.List;

public class DefaultBoxInfo implements BoxInfo{
    private final Class<? extends BoxDefinition> boxDefinitionClass;
    private final List<SingularRequirementRef> requirementRefs = new ArrayList<>();
    private String boxId;

    public DefaultBoxInfo(Class<? extends BoxDefinition> boxDefinitionClass) {
        this.boxDefinitionClass = boxDefinitionClass;
    }

    @Override
    public String getBoxId() {
        return boxId;
    }

    @Override
    public void addSingularRequirementRef(SingularRequirementRef ref) {
        requirementRefs.add(ref);
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
    public List<RequirementData> getRequirementsData() {
        List<RequirementData> result = new ArrayList<>();
        for (SingularRequirementRef ref : requirementRefs) {
            result.add(new RequirementData(ref.getId(), ref.getRequirementDescription()));
        }
        return result;
    }
}
