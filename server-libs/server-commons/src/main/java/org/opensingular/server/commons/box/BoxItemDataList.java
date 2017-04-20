package org.opensingular.server.commons.box;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BoxItemDataList implements Serializable {

    private List<BoxItemData> boxItemDataList = new ArrayList<>(0);

    public List<BoxItemData> getBoxItemDataList() {
        return boxItemDataList;
    }

    public BoxItemDataList setBoxItemDataList(List<BoxItemData> boxItemDataList) {
        this.boxItemDataList = boxItemDataList;
        return this;
    }



}