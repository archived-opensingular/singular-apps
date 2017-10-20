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

package org.opensingular.server.commons.box;

import org.opensingular.server.commons.service.dto.BoxItemAction;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class BoxItemDataMap extends LinkedHashMap<String, Object> implements Serializable {

    private static final long serialVersionUID = 1L;

    private BoxItemData boxItemData;

    public BoxItemDataMap(BoxItemData boxItemData) {
        this.boxItemData = boxItemData;
        putAll(((BoxItemDataImpl) boxItemData).getRawMap());
    }

    public Long getCod() {
        Object codRequirement = get("codPeticao");
        if (codRequirement instanceof Number) {
            return ((Number) codRequirement).longValue();
        } else if (codRequirement instanceof String) {
            return Integer.valueOf((String) codRequirement).longValue();
        }
        return null;
    }

    public Integer getVersionStamp() {
        Object versionStamp = get("versionStamp");
        if(versionStamp instanceof Number){
            return ((Number) versionStamp).intValue();
        } else if (versionStamp instanceof String) {
            return Integer.valueOf((String) versionStamp);
        } else {
            return null;
        }
    }

    public Long getFlowInstanceId() {
        Object flowInstanceId = get("flowInstanceId");
        if (flowInstanceId instanceof Number) {
            return ((Number) flowInstanceId).longValue();
        } else if (flowInstanceId instanceof String) {
            return Integer.valueOf((String) flowInstanceId).longValue();
        } else {
            return null;
        }
    }

    public String getProcessBeginDate() {
        Object processBeginDate = get("processBeginDate");
        if (processBeginDate != null) {
            return String.valueOf(processBeginDate);
        }
        return null;
    }

    public Map<String, BoxItemAction> getActionsMap() {
        Map<String, BoxItemAction> actionsMap = new LinkedHashMap<>();
        for (BoxItemAction boxItemAction : boxItemData.getBoxItemActions()) {
            actionsMap.put(boxItemAction.getName(), boxItemAction);
        }
        return actionsMap;
    }

    public BoxItemAction getActionByName(String actionName) {
        return getActionsMap().get(actionName);
    }

    public boolean hasAction(BoxItemAction itemAction) {
        return getActionsMap().containsKey(itemAction.getName());
    }

}