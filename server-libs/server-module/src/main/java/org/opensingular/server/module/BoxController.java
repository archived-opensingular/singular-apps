package org.opensingular.server.module;

import org.opensingular.server.commons.box.BoxItemDataImpl;
import org.opensingular.server.commons.box.BoxItemDataList;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.dto.RequirementData;
import org.opensingular.server.module.workspace.ItemBoxFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BoxController implements BoxInfo {

    private String                      id              = UUID.randomUUID().toString();
    private Set<SingularRequirementRef> requirementRefs = new LinkedHashSet<>();
    private ItemBoxFactory itemBoxFactory;

    public BoxController(ItemBoxFactory itemBoxFactory, List<SingularRequirementRef> requirementRefs) {
        for (SingularRequirementRef ref : requirementRefs) {
            this.requirementRefs.add(ref);
        }
        this.itemBoxFactory = itemBoxFactory;
    }

    public BoxController(ItemBoxFactory itemBox) {
        this.itemBoxFactory = itemBox;
    }

    @Override
    public String getBoxId() {
        return id;
    }

    @Override
    public Set<SingularRequirementRef> getRequirementRefs() {
        return new LinkedHashSet<>(requirementRefs);
    }

    void addRequirementRefs(SingularRequirementRef requirementRefs) {
        this.requirementRefs.add(requirementRefs);
    }

    ItemBoxFactory getItemBoxFactory() {
        return itemBoxFactory;
    }

    public Long countItens(QuickFilter filter) {
        return itemBoxFactory.getDataProvider().count(filter, this);
    }

    public BoxItemDataList searchItens(QuickFilter filter) {
        BoxItemDataProvider             provider       = itemBoxFactory.getDataProvider();
        List<Map<String, Serializable>> itens          = provider.search(filter, this);
        BoxItemDataList                 result         = new BoxItemDataList();

        for (Map<String, Serializable> item : itens) {
            BoxItemDataImpl line = new BoxItemDataImpl();
            line.setRawMap(item);
            line.setBoxItemActions(provider.getActionProvider().getLineActions(this, line, filter));
            result.getBoxItemDataList().add(line);
        }
        return result;
    }


    List<RequirementData> getRequirementsData() {
        List<RequirementData> result = new ArrayList<>();
        for (SingularRequirementRef ref : requirementRefs) {
            result.add(new RequirementData(ref.getId(), ref.getRequirementDescription()));
        }
        return result;
    }
}
