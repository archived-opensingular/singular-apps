/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.requirement.module;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.opensingular.requirement.module.service.dto.BoxConfigurationData;

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

    public Optional<BoxConfigurationData> getMenuByLabel(String menu) {
        return getBoxesConfiguration()
                .stream()
                .filter(i -> i.getLabel().equalsIgnoreCase(menu)).findFirst();
    }

}
