package org.opensingular.server.module;

import org.opensingular.flow.core.TaskType;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.box.action.BoxItemActionList;
import org.opensingular.server.commons.persistence.filter.QuickFilter;


public class DefaultActionProvider implements ActionProvider, Loggable {

    protected void addViewAction(BoxInfo boxInfo, BoxItemData line, QuickFilter filter, BoxItemActionList list) {
        list.addViewAction(line);
    }


    protected void addEditAction(BoxInfo boxInfo, BoxItemData line, QuickFilter filter, BoxItemActionList list) {
        list.addEditAction(line);
    }


    protected void addDeleteAction(BoxInfo boxInfo, BoxItemData line, QuickFilter filter, BoxItemActionList list) {
        list.addDeleteAction(line);
    }


    protected void addAssignAction(BoxInfo boxInfo, BoxItemData line, QuickFilter filter, BoxItemActionList list) {
        if (line.getAllocatedSUserId() == null && TaskType.PEOPLE.name().equals(line.getTaskType())) {
            list.addAssignAction(line);
        }
    }

    protected void addRelocateAction(BoxInfo boxInfo, BoxItemData line, QuickFilter filter, BoxItemActionList list) {
        if (TaskType.PEOPLE.name().equals(line.getTaskType())) {
            list.addRelocateAction(line);
        }
    }

    protected void addAnalyseAction(BoxInfo boxInfo, BoxItemData line, QuickFilter filter, BoxItemActionList list) {
        if (filter.getIdUsuarioLogado().equalsIgnoreCase((String) line.getAllocatedSUserId())) {
            list.addAnalyseAction(line);
        }
    }


    protected BoxItemActionList getDefaultActions(BoxInfo boxInfo, BoxItemData line, QuickFilter filter) {
        BoxItemActionList list = new BoxItemActionList();
        addViewAction(boxInfo, line, filter, list);
        addEditAction(boxInfo, line, filter, list);
        addDeleteAction(boxInfo, line, filter, list);
        addAssignAction(boxInfo, line, filter, list);
        addRelocateAction(boxInfo, line, filter, list);
        addAnalyseAction(boxInfo, line, filter, list);
        return list;
    }

    @Override
    public BoxItemActionList getLineActions(BoxInfo boxInfo, BoxItemData line, QuickFilter filter) {
        BoxItemActionList boxItemActionList = getDefaultActions(boxInfo, line, filter);
        return boxItemActionList;
    }
}