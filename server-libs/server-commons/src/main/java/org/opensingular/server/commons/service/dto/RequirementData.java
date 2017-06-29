package org.opensingular.server.commons.service.dto;

import java.io.Serializable;

public class RequirementData implements Serializable {

    private Long id;
    private String label;

    public RequirementData() {
    }

    public RequirementData(Long id) {
        this.id = id;
    }

    public RequirementData(Long key, String requirementDescription) {
        this.id = key;
        this.label = requirementDescription;
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
