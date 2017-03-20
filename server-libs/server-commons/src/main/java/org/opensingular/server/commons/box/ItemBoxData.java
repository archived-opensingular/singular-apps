package org.opensingular.server.commons.box;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.opensingular.server.commons.service.dto.BoxItemAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemBoxData implements Serializable {

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

    public ItemBoxData setBoxItemActions(List<BoxItemAction> boxItemActions) {
        this.boxItemActions = boxItemActions;
        return this;
    }

    public List<BoxItemAction> getBoxItemActions() {
        return boxItemActions;
    }

    public ItemBoxData setRawMap(Map<String, Serializable> rawMap) {
        this.rawMap = rawMap;
        return this;
    }

    public Map<String, Serializable> getRawMap() {
        return rawMap;
    }
}