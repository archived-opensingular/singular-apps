package org.opensingular.server.commons.box;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.opensingular.server.commons.box.factory.BoxItemActionList;
import org.opensingular.server.commons.service.dto.BoxItemAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoxItemData implements Serializable {

    @JsonDeserialize(contentAs = String.class)
    private Map<String, Serializable> rawMap = new HashMap<>();

    private List<BoxItemAction> boxItemActions = new ArrayList<>();

    public void addAction(BoxItemAction boxItemAction) {
        boxItemActions.add(boxItemAction);
    }

    public Serializable get(String key) {
        return rawMap.get(key);
    }

    public void replace(String key, Serializable value) {
        rawMap.replace(key, value);
    }

    public List<BoxItemAction> getBoxItemActions() {
        return boxItemActions;
    }

    public void setBoxItemActions(BoxItemActionList lineActions) {
        this.getBoxItemActions().addAll(lineActions.getBoxItemActions());
    }

    public BoxItemData setBoxItemActions(List<BoxItemAction> boxItemActions) {
        this.boxItemActions = boxItemActions;
        return this;
    }

    public Map<String, Serializable> getRawMap() {
        return rawMap;
    }

    public BoxItemData setRawMap(Map<String, Serializable> rawMap) {
        this.rawMap = rawMap;
        return this;
    }

    public Serializable getCodUsuarioAlocado() {
        return rawMap.get("codUsuarioAlocado");
    }

    public Serializable getCodPeticao() {
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