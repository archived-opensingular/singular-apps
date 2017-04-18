package org.opensingular.server.module;

import org.opensingular.flow.core.Flow;
import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.flow.core.TaskType;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.box.ItemBoxData;
import org.opensingular.server.commons.box.factory.BoxItemActionList;
import org.opensingular.server.commons.flow.actions.ActionConfig;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.dto.BoxItemAction;

import java.util.List;
import java.util.stream.Collectors;

import static org.opensingular.server.commons.flow.actions.DefaultActions.ACTION_ANALYSE;
import static org.opensingular.server.commons.flow.actions.DefaultActions.ACTION_ASSIGN;
import static org.opensingular.server.commons.flow.actions.DefaultActions.ACTION_EDIT;
import static org.opensingular.server.commons.flow.actions.DefaultActions.ACTION_RELOCATE;
import static org.opensingular.server.commons.flow.actions.DefaultActions.ACTION_VIEW;


public class DefaultActionProvider implements ActionProvider, Loggable {


    private List<BoxItemAction> findActionConfigAndFilter(ItemBoxData itemBoxData, BoxItemActionList boxItemActionList) {
        String processKey = (String) itemBoxData.getProcessType();

        if (processKey != null) {
            ProcessDefinition<?> processDefinition = Flow.getProcessDefinition(processKey);
            ActionConfig actionConfig = processDefinition.getMetaDataValue(ActionConfig.KEY);
            if (actionConfig != null) {
                return filterByActionConfig(boxItemActionList, actionConfig);
            }
        }

        return boxItemActionList.getBoxItemActions();
    }

    private List<BoxItemAction> filterByActionConfig(BoxItemActionList boxItemActionList, ActionConfig actionConfig) {
        return boxItemActionList.getBoxItemActions().stream()
                .filter(itemAction -> actionConfig.containsAction(itemAction.getName()))
                .collect(Collectors.toList());
    }


    public BoxItemActionList getTaskActions(ItemBoxData line, QuickFilter filter) {
        //petition
        BoxItemActionList boxItemActionList = new BoxItemActionList()
                .addPopupBox(line, FormAction.FORM_FILL, ACTION_EDIT.getName())
                .addPopupBox(line, FormAction.FORM_VIEW, ACTION_VIEW.getName())
                .addDeleteAction(line)
                .addExecuteInstante(line.getCodPeticao(), ACTION_ASSIGN.getName());

        //tasks
        if (line.getCodUsuarioAlocado() == null && TaskType.PEOPLE.name().equals(line.getTaskType())) {
            boxItemActionList.addExecuteInstante(line.getCodPeticao(), ACTION_ASSIGN.getName());
        }
        if (TaskType.PEOPLE.name().equals(line.getTaskType())) {
            boxItemActionList.addExecuteInstante(line.getCodPeticao(), ACTION_RELOCATE.getName());
        }
        if (filter.getIdUsuarioLogado().equalsIgnoreCase((String) line.getCodUsuarioAlocado())) {
            boxItemActionList.addPopupBox(line, FormAction.FORM_ANALYSIS, ACTION_ANALYSE.getName());
        }
        return boxItemActionList.addPopupBox(line, FormAction.FORM_VIEW, ACTION_VIEW.getName());
    }

    @Override
    public BoxItemActionList getLineActions(BoxInfo boxInfo, ItemBoxData line, QuickFilter filter) {
        BoxItemActionList boxItemActionList = getTaskActions(line, filter);
        findActionConfigAndFilter(line, boxItemActionList);
        return boxItemActionList;
    }
}