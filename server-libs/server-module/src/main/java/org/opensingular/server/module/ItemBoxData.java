package org.opensingular.server.module;

import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.service.dto.DatatableField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemBoxData implements Serializable {


    private Map<DatatableField, Serializable> data           = new HashMap<>();
    private List<BoxItemAction>               boxItemActions = new ArrayList<>();

    void addColumn(DatatableField key, Serializable value) {
        data.put(key, value);
    }

    public void addAction(BoxItemAction boxItemAction) {
        boxItemActions.add(boxItemAction);
    }


}
