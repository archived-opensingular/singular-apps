package org.opensingular.server.commons.box.factory;


import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.flow.actions.DefaultBoxItemActionFactory;
import org.opensingular.server.commons.service.dto.BoxItemAction;

import java.util.ArrayList;

public class BoxItemActionList extends ArrayList<BoxItemAction> {


    public BoxItemActionList addViewAction(BoxItemData line) {
        addAction(DefaultBoxItemActionFactory.getVIEW(line));
        return this;
    }

    public BoxItemActionList addDeleteAction(BoxItemData line) {
        addAction(DefaultBoxItemActionFactory.getDELETE(line));
        return this;
    }

    public BoxItemActionList addAssignAction(BoxItemData line) {
        addAction(DefaultBoxItemActionFactory.getASSIGN(line));
        return this;
    }


    public BoxItemActionList addRelocateAction(BoxItemData line) {
        addAction(DefaultBoxItemActionFactory.getRELOCATE(line));
        return this;
    }

    public BoxItemActionList addAnalyseAction(BoxItemData line) {
        addAction(DefaultBoxItemActionFactory.getANALYSE(line));
        return this;
    }

    public BoxItemActionList addEditAction(BoxItemData line) {
        addAction(DefaultBoxItemActionFactory.getEDIT(line));
        return this;
    }

    public BoxItemActionList addAction(BoxItemAction boxItemAction) {
        add(boxItemAction);
        return this;
    }


}