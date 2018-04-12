/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.requirement.module;

import org.opensingular.flow.core.TaskType;
import org.opensingular.requirement.commons.box.BoxItemData;
import org.opensingular.requirement.commons.box.action.BoxItemActionList;
import org.opensingular.requirement.commons.persistence.filter.QuickFilter;

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

            if (line.getAllocatedSUserId() == null && TaskType.HUMAN == line.getTaskType()) {
                list.addAssignAction(line);
            }
        });
        return this;
    }

    public ActionProviderBuilder addRelocateAction() {
        actionConfigurers.add((boxInfo, line, filter, list) -> {
            if (TaskType.HUMAN == line.getTaskType()) {
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


    public ActionProviderBuilder addHistoryAction() {
        actionConfigurers.add((boxInfo, line, filter, list) -> list.addHistoryAction(line));
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