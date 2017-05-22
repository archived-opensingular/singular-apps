package org.opensingular.server.commons.box.action;


import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.box.action.defaults.AnalyseAction;
import org.opensingular.server.commons.box.action.defaults.AssignAction;
import org.opensingular.server.commons.box.action.defaults.DeleteAction;
import org.opensingular.server.commons.box.action.defaults.EditAction;
import org.opensingular.server.commons.box.action.defaults.RelocateAction;
import org.opensingular.server.commons.box.action.defaults.ViewAction;
import org.opensingular.server.commons.service.dto.BoxItemAction;

import java.util.ArrayList;

public class BoxItemActionList extends ArrayList<BoxItemAction> {


    public BoxItemActionList addViewAction(BoxItemData line) {
        addAction(new ViewAction(line));
        return this;
    }

    public BoxItemActionList addDeleteAction(BoxItemData line) {
        addAction(new DeleteAction(line));
        return this;
    }

    public BoxItemActionList addAssignAction(BoxItemData line) {
        addAction(new AssignAction(line));
        return this;
    }


    public BoxItemActionList addRelocateAction(BoxItemData line) {
        addAction(new RelocateAction(line));
        return this;
    }

    public BoxItemActionList addAnalyseAction(BoxItemData line) {
        addAction(new AnalyseAction(line));
        return this;
    }

    public BoxItemActionList addEditAction(BoxItemData line) {
        addAction(new EditAction(line));
        return this;
    }

    public BoxItemActionList addAction(BoxItemAction boxItemAction) {
        add(boxItemAction);
        return this;
    }


}