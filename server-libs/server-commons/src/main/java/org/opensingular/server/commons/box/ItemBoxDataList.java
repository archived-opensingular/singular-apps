package org.opensingular.server.commons.box;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemBoxDataList implements Serializable {

    private List<ItemBoxData> itemBoxDataList = new ArrayList<>(0);

    public List<ItemBoxData> getItemBoxDataList() {
        return itemBoxDataList;
    }

    public ItemBoxDataList setItemBoxDataList(List<ItemBoxData> itemBoxDataList) {
        this.itemBoxDataList = itemBoxDataList;
        return this;
    }

}