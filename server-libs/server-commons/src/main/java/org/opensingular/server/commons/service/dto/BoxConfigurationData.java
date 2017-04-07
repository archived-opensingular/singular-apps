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

package org.opensingular.server.commons.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BoxConfigurationData implements Serializable {

    private String                id;
    private String                label;
    private List<BoxDefinitionData> itemBoxesMetadata;
    private List<ProcessDTO>        processes;

    public BoxConfigurationData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
        if (itemBoxesMetadata == null) {
            return Collections.EMPTY_LIST;
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

    public ProcessDTO getProcessByAbbreviation(String processAbbreviation) {
        return getProcesses()
                .stream()
                .filter(p -> p.getAbbreviation().equalsIgnoreCase(processAbbreviation))
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
