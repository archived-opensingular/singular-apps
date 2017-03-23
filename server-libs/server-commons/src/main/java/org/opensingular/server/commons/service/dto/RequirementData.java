package org.opensingular.server.commons.service.dto;

import java.io.Serializable;

public class RequirementData implements Serializable {

    private String id;
    private String label;

    public RequirementData() {
    }

    public RequirementData(String id) {
        this.id = id;
    }

    public RequirementData(String key, String requirementDescription) {
        this.id = key;
        this.label = requirementDescription;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
