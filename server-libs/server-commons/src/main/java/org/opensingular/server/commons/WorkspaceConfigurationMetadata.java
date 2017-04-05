package org.opensingular.server.commons;

import org.opensingular.server.commons.service.dto.BoxConfigurationData;

import java.io.Serializable;
import java.util.List;

public class WorkspaceConfigurationMetadata implements Serializable {

    private List<BoxConfigurationData> boxesConfiguration;

    public WorkspaceConfigurationMetadata() {
    }

    public WorkspaceConfigurationMetadata(List<BoxConfigurationData> boxConfigurationMetadatas) {
        this.boxesConfiguration = boxConfigurationMetadatas;
    }

    public List<BoxConfigurationData> getBoxesConfiguration() {
        return boxesConfiguration;
    }

    public void setBoxesConfiguration(List<BoxConfigurationData> boxesConfiguration) {
        this.boxesConfiguration = boxesConfiguration;
    }

}
