package org.opensingular.server.module;

import org.opensingular.server.commons.box.BoxItemDataImpl;
import org.opensingular.server.commons.box.BoxItemDataList;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.dto.RequirementData;
import org.opensingular.server.module.workspace.BoxDefinition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BoxController implements BoxInfo {

    private String                      id;
    private Set<SingularRequirementRef> requirementRefs = new LinkedHashSet<>();
    private BoxDefinition boxDefinition;

    public BoxController(BoxDefinition boxDefinition, List<SingularRequirementRef> requirementRefs) {
        for (SingularRequirementRef ref : requirementRefs) {
            this.requirementRefs.add(ref);
        }
        this.boxDefinition = boxDefinition;
    }

    public BoxController(BoxDefinition itemBox) {
        this.boxDefinition = itemBox;
    }

    @Override
    public String getBoxId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Set<SingularRequirementRef> getRequirementRefs() {
        return new LinkedHashSet<>(requirementRefs);
    }

    void addRequirementRefs(SingularRequirementRef requirementRefs) {
        this.requirementRefs.add(requirementRefs);
    }

    BoxDefinition getBoxDefinition() {
        return boxDefinition;
    }

    public Long countItens(QuickFilter filter) {
        return boxDefinition.getDataProvider().count(filter, this);
    }

    public BoxItemDataList searchItens(QuickFilter filter) {
        BoxItemDataProvider             provider       = boxDefinition.getDataProvider();
        List<Map<String, Serializable>> itens          = provider.search(filter, this);
        BoxItemDataList                 result         = new BoxItemDataList();
        ActionProvider                  actionProvider = addBuiltInDecorators(provider.getActionProvider());

        for (Map<String, Serializable> item : itens) {
            BoxItemDataImpl line = new BoxItemDataImpl();
            line.setRawMap(item);
            line.setBoxItemActions(actionProvider.getLineActions(this, line, filter));
            result.getBoxItemDataList().add(line);
        }
        return result;
    }

    protected ActionProvider addBuiltInDecorators(ActionProvider actionProvider) {
        return
                new AuthorizationAwareActionProviderDecorator(actionProvider);
    }


    List<RequirementData> getRequirementsData() {
        List<RequirementData> result = new ArrayList<>();
        for (SingularRequirementRef ref : requirementRefs) {
            result.add(new RequirementData(ref.getId(), ref.getRequirementDescription()));
        }
        return result;
    }
}
