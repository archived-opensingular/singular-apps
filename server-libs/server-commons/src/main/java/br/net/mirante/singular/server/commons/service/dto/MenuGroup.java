/*
 * Copyright (c) 2016, Mirante and/or its affiliates. All rights reserved.
 * Mirante PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package br.net.mirante.singular.server.commons.service.dto;

import java.io.Serializable;
import java.util.List;

public class MenuGroup implements Serializable {

    private String label;
    private List<ItemBox> itemBoxes;
    private List<ProcessDTO> processes;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<ProcessDTO> getProcesses() {
        return processes;
    }

    public void setProcesses(List<ProcessDTO> processes) {
        this.processes = processes;
    }

    public List<ItemBox> getItemBoxes() {
        return itemBoxes;
    }

    public void setItemBoxes(List<ItemBox> itemBoxes) {
        this.itemBoxes = itemBoxes;
    }

    public ItemBox getItemPorLabel(String itemName) {
        if (itemBoxes != null) {
            for (ItemBox itemBox : itemBoxes) {
                if (itemBox.getName().equalsIgnoreCase(itemName)) {
                    return itemBox;
                }
            }
        }

        return null;
    }
}