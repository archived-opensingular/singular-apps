package org.opensingular.server.commons;

import org.opensingular.server.commons.service.dto.BoxConfigurationMetadata;

import java.io.Serializable;
import java.util.List;

public class WorkspaceConfigurationMetadata implements Serializable {

    private List<BoxConfigurationMetadata> boxesConfiguration;

    public WorkspaceConfigurationMetadata() {
    }

    public WorkspaceConfigurationMetadata(List<BoxConfigurationMetadata> boxConfigurationMetadatas) {
        this.boxesConfiguration = boxConfigurationMetadatas;
    }

    public List<BoxConfigurationMetadata> getBoxesConfiguration() {
        return boxesConfiguration;
    }

    public void setBoxesConfiguration(List<BoxConfigurationMetadata> boxesConfiguration) {
        this.boxesConfiguration = boxesConfiguration;
    }

}
