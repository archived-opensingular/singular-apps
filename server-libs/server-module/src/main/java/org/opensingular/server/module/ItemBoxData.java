package org.opensingular.server.module;

import org.opensingular.server.commons.service.dto.BoxItemAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemBoxData implements Serializable {


    private Map<String, Object> data           = new HashMap<>();
    private List<BoxItemAction> boxItemActions = new ArrayList<>();

    public void addColumn(String key, Object value) {
        data.put(key, value);
    }

    public void addAction(BoxItemAction boxItemAction) {
        boxItemActions.add(boxItemAction);
    }


}
