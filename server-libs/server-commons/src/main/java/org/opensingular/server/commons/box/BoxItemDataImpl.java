package org.opensingular.server.commons.box;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.opensingular.server.commons.box.action.BoxItemActionList;
import org.opensingular.server.commons.service.dto.BoxItemAction;

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
    public Serializable getPetitionId() {
        return rawMap.get("codPeticao");
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
    public Serializable getParentPetition() {
        return rawMap.get("parentPetition");
    }

    @Override
    public Serializable getRootPetition() {
        return rawMap.get("rootPetition");
    }

    @Override
    public Serializable getRequirementDefinitionId() {
        return rawMap.get("requirementDefinitionId");
    }

    /**
     * @param id
     * @deprecated para uso temporário enquanto a forma de identificar o requerimento não é definida
     */
    @Deprecated
    public void setRequirementDefinitionId(String id) {
        rawMap.put("requirementDefinitionId", id);
    }

    public void setRawMap(Map<String, Serializable> rawMap) {
        this.rawMap = rawMap;
    }

    public Map<String, Serializable> getRawMap() {
        return rawMap;
    }
}