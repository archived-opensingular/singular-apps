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

package org.opensingular.requirement.module.box;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.opensingular.requirement.module.box.action.BoxItemActionList;
import org.opensingular.requirement.module.service.dto.BoxItemAction;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BoxItemDataImpl implements BoxItemData {

    @JsonDeserialize(contentAs = String.class)
    private Map<String, Serializable> rawMap = new HashMap<>();

    private BoxItemActionList boxItemActions = new BoxItemActionList();

    @Override
    public void addAction(BoxItemAction boxItemAction) {
        boxItemActions.addAction(boxItemAction);
    }

    @Override
    public Serializable get(String key) {
        return rawMap.get(key);
    }

    public void replace(String key, Serializable value) {
        rawMap.replace(key, value);
    }

    @Override
    public BoxItemActionList getBoxItemActions() {
        return boxItemActions;
    }

    @Override
    public void setBoxItemActions(BoxItemActionList boxItemActions) {
        this.boxItemActions = boxItemActions;
    }

    @Override
    public Serializable getAllocatedSUserId() {
        return rawMap.get("codUsuarioAlocado");
    }

    @Override
    public Serializable getRequirementId() {
        return rawMap.get("codRequirement");
    }

    @Override
    public Serializable getType() {
        return rawMap.get("type");
    }

    @Override
    public Serializable getTaskType() {
        return rawMap.get("taskType");
    }

    @Override
    public Serializable getProcessType() {
        return rawMap.get("processType");
    }

    @Override
    public Serializable getSituation() {
        return rawMap.get("situation");
    }

    @Override
    public Serializable getParentRequirement() {
        return rawMap.get("parentRequirement");
    }

    @Override
    public Serializable getRootRequirement() {
        return rawMap.get("rootRequirement");
    }

    @Override
    public Serializable getRequirementDefinitionId() {
        return rawMap.get("requirementDefinitionId");
    }

    public void setRawMap(Map<String, Serializable> rawMap) {
        this.rawMap = rawMap;
    }

    public Map<String, Serializable> getRawMap() {
        return rawMap;
    }
}