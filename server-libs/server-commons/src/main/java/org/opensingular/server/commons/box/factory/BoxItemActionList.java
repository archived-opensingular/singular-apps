package org.opensingular.server.commons.box.factory;


import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.flow.actions.DefaultBoxItemActionFactory;
import org.opensingular.server.commons.service.dto.BoxItemAction;

import java.util.ArrayList;
import java.util.List;

public class BoxItemActionList {

    private List<BoxItemAction> boxItemActions;

    public BoxItemActionList() {
        this.boxItemActions = new ArrayList<>();
    }


    public BoxItemActionList addViewAction(BoxItemData line) {
        add(DefaultBoxItemActionFactory.getVIEW(line));
        return this;
    }

    public BoxItemActionList addDeleteAction(BoxItemData line) {
        add(DefaultBoxItemActionFactory.getDELETE(line));
        return this;
    }

    public BoxItemActionList addAssignAction(BoxItemData line) {
        add(DefaultBoxItemActionFactory.getASSIGN(line));
        return this;
    }


    public BoxItemActionList addRelocateAction(BoxItemData line) {
        add(DefaultBoxItemActionFactory.getRELOCATE(line));
        return this;
    }

    public BoxItemActionList addAnalyseAction(BoxItemData line) {
        add(DefaultBoxItemActionFactory.getANALYSE(line));
        return this;
    }

    public BoxItemActionList addEditAction(BoxItemData line) {
        add(DefaultBoxItemActionFactory.getEDIT(line));
        return this;
    }

    public BoxItemActionList add(BoxItemAction boxItemAction) {
        boxItemActions.add(boxItemAction);
        return this;
    }

    public List<BoxItemAction> getBoxItemActions() {
        return boxItemActions;
    }

    void setBoxItemActions(List<BoxItemAction> boxItemActions) {
        this.boxItemActions = boxItemActions;
    }




}