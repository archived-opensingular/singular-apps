package org.opensingular.server.commons.box;

import org.opensingular.server.commons.box.action.BoxItemActionList;
import org.opensingular.server.commons.service.dto.BoxItemAction;

import java.io.Serializable;

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

    Serializable getRequirementId();
}
