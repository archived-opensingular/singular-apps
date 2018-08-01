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

package org.opensingular.requirement.commons;

import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.requirement.module.WorkspaceConfigurationMetadata;
import org.opensingular.requirement.module.service.dto.BoxConfigurationData;
import org.opensingular.requirement.module.service.dto.BoxDefinitionData;
import org.opensingular.requirement.module.service.dto.DatatableField;
import org.opensingular.requirement.module.service.dto.ItemBox;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Primary
@Named("workspaceConfigurationMetadata")
public class WorkspaceMetadataMockBean extends WorkspaceConfigurationMetadata {
    private WorkspaceConfigurationMetadata w;

    @PostConstruct
    public void init() {
        w = new WorkspaceConfigurationMetadata();
        BoxConfigurationData box = new BoxConfigurationData();
        box.setBoxesDefinition(new ArrayList<>());
        BoxDefinitionData boxDefinitionData = new BoxDefinitionData();
        final ItemBox teste = new ItemBox();
        teste.setName("Rascunho");
        teste.setDescription("Petições de rascunho");
        teste.setIcone(DefaultIcons.DOCS);
        teste.setId("1");
        teste.setFieldsDatatable(getDatatableFields());
        boxDefinitionData.setRequirements(new LinkedHashSet<>());
        boxDefinitionData.getRequirements().add(null);
        boxDefinitionData.setItemBox(teste);
        box.getBoxesDefinition().add(boxDefinitionData);
        w.setBoxConfiguration(box);
    }

    private List<DatatableField> getDatatableFields() {
        List<DatatableField> fields = new ArrayList<>(3);
        fields.add(DatatableField.of("Descrição", "description"));
        fields.add(DatatableField.of("Dt. Edição", "editionDate"));
        fields.add(DatatableField.of("Data de Entrada", "creationDate"));
        return fields;
    }

    @Override
    public BoxConfigurationData getBoxConfiguration() {
        return w.getBoxConfiguration();
    }

    @Override
    public void setBoxConfiguration(BoxConfigurationData boxConfiguration) {
        w.setBoxConfiguration(boxConfiguration);
    }
}
