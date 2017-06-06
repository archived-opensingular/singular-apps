package org.opensingular.server.module;

import org.opensingular.flow.core.TaskType;
import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.box.action.BoxItemActionList;
import org.opensingular.server.commons.persistence.filter.QuickFilter;

import java.util.ArrayList;
import java.util.List;


public class ActionProviderBuilder implements ActionProvider {

    private List<ActionConfigurer> actionConfigurers = new ArrayList<>();

    public ActionProviderBuilder addViewAction() {
        actionConfigurers.add((boxInfo, line, filter, list) -> list.addViewAction(line));
        return this;

    }

    public ActionProviderBuilder addEditAction() {
        actionConfigurers.add((boxInfo, line, filter, list) -> list.addEditAction(line));
        return this;
    }

    public ActionProviderBuilder addDeleteAction() {
        actionConfigurers.add((boxInfo, line, filter, list) -> list.addDeleteAction(line));
        return this;
    }

    public ActionProviderBuilder addAssignAction() {
        actionConfigurers.add((boxInfo, line, filter, list) -> {
            if (line.getAllocatedSUserId() == null && TaskType.PEOPLE.equals(line.getTaskType())) {
                list.addAssignAction(line);
            }
        });
        return this;
    }

    public ActionProviderBuilder addRelocateAction() {
        actionConfigurers.add((boxInfo, line, filter, list) -> {
            if (TaskType.PEOPLE.equals(line.getTaskType())) {
                list.addRelocateAction(line);
            }
        });
        return this;
    }

    public ActionProviderBuilder addAnalyseAction() {
        actionConfigurers.add((boxInfo, line, filter, list) -> {
            if (filter.getIdUsuarioLogado() != null && filter.getIdUsuarioLogado().equalsIgnoreCase((String) line.getAllocatedSUserId())) {
                list.addAnalyseAction(line);
            }
        });
        return this;
    }

    public ActionProviderBuilder addCustomActions(ActionConfigurer configurer) {
        actionConfigurers.add(configurer);
        return this;
    }


    @Override
    public BoxItemActionList getLineActions(BoxInfo boxInfo, BoxItemData line, QuickFilter filter) {
        BoxItemActionList list = new BoxItemActionList();
        for (ActionConfigurer configurer : actionConfigurers) {
            configurer.configure(boxInfo, line, filter, list);
        }
        return list;
    }


    @FunctionalInterface
    public static interface ActionConfigurer {
        public void configure(BoxInfo boxInfo, BoxItemData line, QuickFilter filter, BoxItemActionList list);
    }
}