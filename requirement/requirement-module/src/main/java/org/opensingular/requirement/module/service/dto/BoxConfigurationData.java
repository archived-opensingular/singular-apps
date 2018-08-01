/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.requirement.module.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BoxConfigurationData implements Serializable {
    private List<BoxDefinitionData> itemBoxesMetadata;
    private List<FlowDefinitionDTO> processes;

    public BoxConfigurationData() {
    }

    public List<FlowDefinitionDTO> getProcesses() {
        return processes;
    }

    public void setProcesses(List<FlowDefinitionDTO> processes) {
        this.processes = processes;
    }

    public List<ItemBox> getItemBoxes() {
        if (itemBoxesMetadata == null) {
            return new ArrayList<>(0);
        }
        return itemBoxesMetadata.stream().map(BoxDefinitionData::getItemBox).collect(Collectors.toList());
    }

    public BoxDefinitionData getItemPorLabel(String itemName) {
        for (BoxDefinitionData itemBoxMeta : itemBoxesMetadata) {
            if (itemBoxMeta.getItemBox().getName().equalsIgnoreCase(itemName)) {
                return itemBoxMeta;
            }
        }
        return null;
    }

    public FlowDefinitionDTO getProcessByAbbreviation(String flowDefinitionAbbreviation) {
        return getProcesses()
                .stream()
                .filter(p -> p.getAbbreviation().equalsIgnoreCase(flowDefinitionAbbreviation))
                .findFirst()
                .orElse(null);
    }

    public List<BoxDefinitionData> getBoxesDefinition() {
        return itemBoxesMetadata;
    }

    public void setBoxesDefinition(List<BoxDefinitionData> itemBoxesMetadata) {
        this.itemBoxesMetadata = itemBoxesMetadata;
    }

    /**
     * matar esse método em favor do uso do id do requerimento
     *
     * @return
     */
    @Deprecated
    public List<FormDTO> getForms() {
        return new ArrayList<>(0);
    }
}