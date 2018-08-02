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
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.box.BoxItemData;
import org.opensingular.requirement.module.box.action.BoxItemActionList;
import org.opensingular.requirement.module.persistence.filter.BoxFilter;


public class DefaultActionProvider implements ActionProvider, Loggable {

    protected void addViewAction(BoxItemData line, BoxFilter filter, BoxItemActionList list) {
        list.addViewAction(line);
    }


    protected void addEditAction(BoxItemData line, BoxFilter filter, BoxItemActionList list) {
        list.addEditAction(line);
    }


    protected void addDeleteAction(BoxItemData line, BoxFilter filter, BoxItemActionList list) {
        list.addDeleteAction(line);
    }


    protected void addAssignAction(BoxItemData line, BoxFilter filter, BoxItemActionList list) {
        if (line.getAllocatedSUserId() == null && TaskType.HUMAN == line.getTaskType()) {
            list.addAssignAction(line);
        }
    }

    protected void addRelocateAction(BoxItemData line, BoxFilter filter, BoxItemActionList list) {
        if (TaskType.HUMAN == line.getTaskType()) {
            list.addRelocateAction(line);
        }
    }

    protected void addHistoryAction(BoxItemData line, BoxFilter filter, BoxItemActionList list) {
        list.addHistoryAction(line);
    }

    protected void addAnalyseAction(BoxItemData line, BoxFilter filter, BoxItemActionList list) {
        if (filter.getIdUsuarioLogado() != null && filter.getIdUsuarioLogado().equalsIgnoreCase((String) line.getAllocatedSUserId())) {
            list.addAnalyseAction(line);
        }
    }

    protected void addCustomActions(BoxItemData line, BoxFilter filter, BoxItemActionList list) {

    }


    protected BoxItemActionList getDefaultActions(BoxItemData line, BoxFilter filter) {
        BoxItemActionList list = new BoxItemActionList();
        addAssignAction(line, filter, list);
        addAnalyseAction(line, filter, list);
        addEditAction(line, filter, list);
        addRelocateAction(line, filter, list);
        addCustomActions(line, filter, list);
        addViewAction(line, filter, list);
        addDeleteAction(line, filter, list);
        addHistoryAction(line, filter, list);
        return list;
    }

    @Override
    public BoxItemActionList getLineActions(BoxItemData line, BoxFilter filter) {
        return getDefaultActions(line, filter);
    }
}