package org.opensingular.server.module;

import org.opensingular.flow.core.TaskType;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.box.factory.BoxItemActionList;
import org.opensingular.server.commons.persistence.filter.QuickFilter;


public class DefaultActionProvider implements ActionProvider, Loggable {


    public BoxItemActionList getDefaultActions(BoxItemData line, QuickFilter filter) {

        BoxItemActionList boxItemActionList = new BoxItemActionList();

        //BOTH
        boxItemActionList
                .addViewAction(line);
        //petition
        boxItemActionList
                .addDeleteAction(line)
                .addEditAction(line);
        //.addAssingAction(line);

        // tasks
        if (line.getCodUsuarioAlocado() == null && TaskType.PEOPLE.name().equals(line.getTaskType())) {
            boxItemActionList.addAssignAction(line);
        }
        if (TaskType.PEOPLE.name().equals(line.getTaskType())) {
            boxItemActionList.addRelocateAction(line);
        }
        if (filter.getIdUsuarioLogado().equalsIgnoreCase((String) line.getCodUsuarioAlocado())) {
            boxItemActionList.addAnalyseAction(line);
        }
        return boxItemActionList;
    }

    @Override
    public BoxItemActionList getLineActions(BoxInfo boxInfo, BoxItemData line, QuickFilter filter) {
        BoxItemActionList boxItemActionList = getDefaultActions(line, filter);
        return boxItemActionList;
    }
}