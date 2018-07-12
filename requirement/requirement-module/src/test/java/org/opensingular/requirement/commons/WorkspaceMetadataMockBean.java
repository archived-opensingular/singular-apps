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
import org.opensingular.requirement.module.service.dto.*;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Primary
@Named("workspaceConfigurationMetadata")
public class WorkspaceMetadataMockBean extends WorkspaceConfigurationMetadata {
    private WorkspaceConfigurationMetadata w;

    @PostConstruct
    public void init() {
        w = new WorkspaceConfigurationMetadata();
        w.setBoxesConfiguration(new ArrayList<>());
        BoxConfigurationData box = new BoxConfigurationData();
        box.setId("id-teste-SingularServerSessionConfigurationMock");
        box.setLabel("super caixa");
        box.setProcesses(new ArrayList<>());
        RequirementDefinitionDTO p = new RequirementDefinitionDTO("ajaaja", "ajaaja", null);
        box.getProcesses().add(p);
        box.setBoxesDefinition(new ArrayList<>());
        BoxDefinitionData boxDefinitionData = new BoxDefinitionData();
        final ItemBox teste = new ItemBox();
        teste.setName("Rascunho");
        teste.setDescription("Petições de rascunho");
        teste.setIcone(DefaultIcons.DOCS);
        teste.setShowDraft(true);
        teste.setId("1");
        teste.setFieldsDatatable(getDatatableFields());
        boxDefinitionData.setRequirements(new ArrayList<>());
        RequirementData req = new RequirementData();
        req.setLabel("Super req");
        req.setId(2L);
        boxDefinitionData.getRequirements().add(req);
        boxDefinitionData.setItemBox(teste);
        box.getBoxesDefinition().add(boxDefinitionData);
        w.getBoxesConfiguration().add(box);
    }

    private List<DatatableField> getDatatableFields() {
        List<DatatableField> fields = new ArrayList<>(3);
        fields.add(DatatableField.of("Descrição", "description"));
        fields.add(DatatableField.of("Dt. Edição", "editionDate"));
        fields.add(DatatableField.of("Data de Entrada", "creationDate"));
        return fields;
    }

    @Override
    public List<BoxConfigurationData> getBoxesConfiguration() {
        return w.getBoxesConfiguration();
    }

    @Override
    public void setBoxesConfiguration(List<BoxConfigurationData> boxesConfiguration) {
        w.setBoxesConfiguration(boxesConfiguration);
    }

    @Override
    public Optional<BoxConfigurationData> getMenuByLabel(String menu) {
        return w.getMenuByLabel(menu);
    }
}
