package org.opensingular.server.commons.box;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.opensingular.server.commons.box.action.BoxItemActionList;
import org.opensingular.server.commons.service.dto.BoxItemAction;

import java.io.Serializable;

@JsonSerialize(as = BoxItemDataImpl.class)
@JsonDeserialize(as = BoxItemDataImpl.class)
public interface BoxItemData extends Serializable {

    void addAction(BoxItemAction boxItemAction);

    Serializable get(String key);

    BoxItemActionList getBoxItemActions();

    void setBoxItemActions(BoxItemActionList boxItemActions);

    Serializable getAllocatedSUserId();

    Serializable getPetitionId();

    Serializable getType();

    Serializable getTaskType();

    Serializable getProcessType();

    Serializable getSituation();

    Serializable getParentPetition();

    Serializable getRootPetition();

    Serializable getRequirementDefinitionId();
}
