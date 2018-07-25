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

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.opensingular.requirement.module.box.action.BoxItemActionList;
import org.opensingular.requirement.module.service.dto.BoxItemAction;

@JsonSerialize(as = BoxItemDataImpl.class)
@JsonDeserialize(as = BoxItemDataImpl.class)
public interface BoxItemData extends Serializable {

    void addAction(BoxItemAction boxItemAction);

    Serializable get(String key);

    BoxItemActionList getBoxItemActions();

    void setBoxItemActions(BoxItemActionList boxItemActions);

    Serializable getAllocatedSUserId();

    Serializable getRequirementId();

    Serializable getType();

    Serializable getTaskType();

    Serializable getProcessType();

    Serializable getSituation();

    Serializable getParentRequirement();

    Serializable getRootRequirement();

    Serializable getRequirementDefinitionId();
}
