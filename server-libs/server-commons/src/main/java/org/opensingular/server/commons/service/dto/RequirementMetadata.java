package org.opensingular.server.commons.service.dto;

import java.io.Serializable;

public class RequirementMetadata implements Serializable {

    private String id;
    private String label;

    public RequirementMetadata() {
    }

    public RequirementMetadata(String id) {
        this.id = id;
    }

    public RequirementMetadata(String key, String requirementDescription) {
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
