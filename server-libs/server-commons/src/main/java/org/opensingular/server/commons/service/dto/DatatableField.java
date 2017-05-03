package org.opensingular.server.commons.service.dto;

import java.io.Serializable;

public class DatatableField implements Serializable {

    public String key;
    public String label;

    public DatatableField() {
    }

    public DatatableField(String key, String label) {
        this.key = key;
        this.label = label;
    }

    public static DatatableField of(String key, String label) {
        return new DatatableField(key, label);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
