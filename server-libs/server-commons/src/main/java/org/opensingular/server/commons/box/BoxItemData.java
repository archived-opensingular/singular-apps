package org.opensingular.server.commons.box;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.opensingular.server.commons.box.factory.BoxItemActionList;
import org.opensingular.server.commons.service.dto.BoxItemAction;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BoxItemData implements Serializable {

    @JsonDeserialize(contentAs = String.class)
    private Map<String, Serializable> rawMap = new HashMap<>();

    private BoxItemActionList boxItemActions = new BoxItemActionList();

    public void addAction(BoxItemAction boxItemAction) {
        boxItemActions.addAction(boxItemAction);
    }

    public Serializable get(String key) {
        return rawMap.get(key);
    }

    public void replace(String key, Serializable value) {
        rawMap.replace(key, value);
    }

    public BoxItemActionList getBoxItemActions() {
        return boxItemActions;
    }

    public void setBoxItemActions(BoxItemActionList boxItemActions) {
        this.boxItemActions = boxItemActions;
    }

    public Map<String, Serializable> getRawMap() {
        return rawMap;
    }

    public BoxItemData setRawMap(Map<String, Serializable> rawMap) {
        this.rawMap = rawMap;
        return this;
    }

    public Serializable getAllocatedSUserId() {
        return rawMap.get("codUsuarioAlocado");
    }

    public Serializable getPetitionId() {
        return rawMap.get("codPeticao");
    }

    public Serializable getType() {
        return rawMap.get("type");
    }

    public Serializable getTaskType() {
        return rawMap.get("taskType");
    }

    public Serializable getProcessType() {
        return rawMap.get("processType");
    }

    public Serializable getSituation() {
        return rawMap.get("situation");
    }

    public Serializable getParentPetition() {
        return rawMap.get("parentPetition");
    }

    public Serializable getRootPetition() {
        return rawMap.get("rootPetition");
    }
}