/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.server.core.wicket.model;

import org.opensingular.server.commons.box.ItemBoxData;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.service.dto.ItemAction;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class BoxItemModel extends LinkedHashMap<String, Object> implements Serializable {

    private static final long serialVersionUID = 1L;

    private ItemBoxData itemBoxData;

    public BoxItemModel(ItemBoxData itemBoxData) {
        this.itemBoxData = itemBoxData;
        putAll(itemBoxData.getRawMap());
    }

    public Long getCod() {
        return Integer.valueOf((String) get("codPeticao")).longValue();
    }

    public Integer getVersionStamp() {
        Object versionStamp = get("versionStamp");
        if (versionStamp != null) {
            return Integer.valueOf((String) versionStamp);
        } else {
            return null;
        }
    }

    public Long getProcessInstanceId() {
        Object processInstanceId = get("processInstanceId");
        if (processInstanceId != null) {
            return Integer.valueOf((String) processInstanceId).longValue();
        } else {
            return null;
        }
    }

    public String getProcessBeginDate() {
        return (String) get("processBeginDate");
    }

    public Map<String, BoxItemAction> getActionsMap() {
        Map<String, BoxItemAction> actionsMap = new LinkedHashMap<>();
        for (BoxItemAction boxItemAction : itemBoxData.getBoxItemActions()) {
            actionsMap.put(boxItemAction.getName(), boxItemAction);
        }
        return actionsMap;
    }

    public BoxItemAction getActionByName(String actionName) {
        return getActionsMap().get(actionName);
    }

    public boolean hasAction(ItemAction itemAction) {
        return getActionsMap().containsKey(itemAction.getName());
    }

}