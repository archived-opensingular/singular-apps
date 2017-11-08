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

package org.opensingular.server.commons.test;

import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.lib.commons.base.SingularException;
import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.server.commons.WorkspaceConfigurationMetadata;
import org.opensingular.server.commons.persistence.dao.server.ModuleDAO;
import org.opensingular.server.commons.service.dto.BoxConfigurationData;
import org.opensingular.server.commons.service.dto.BoxDefinitionData;
import org.opensingular.server.commons.service.dto.DatatableField;
import org.opensingular.server.commons.service.dto.ItemBox;
import org.opensingular.server.commons.service.dto.RequirementData;
import org.opensingular.server.commons.service.dto.RequirementDefinitionDTO;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Named
public class WorkspaceMetadataMockBean {


    private Map<ModuleEntity, WorkspaceConfigurationMetadata> map = new HashMap<>();

    @Inject
    private ModuleDAO moduleDAO;

    @Inject
    private PlatformTransactionManager transactionManager;


    @PostConstruct
    public void init() {
        configBoxesMock();
    }


    public WorkspaceConfigurationMetadata gimmeSomeMock() {
        WorkspaceConfigurationMetadata w = new WorkspaceConfigurationMetadata();
        w.setBoxesConfiguration(new ArrayList<>());
        BoxConfigurationData box = new BoxConfigurationData();
        box.setId("id-teste-SingularServerSessionConfigurationMock");
        box.setLabel("super caixa");
        box.setProcesses(new ArrayList<>());
        RequirementDefinitionDTO p = new RequirementDefinitionDTO("ajaaja", "ajaaja", null, Collections.emptyList());
        box.getProcesses().add(p);
        box.setBoxesDefinition(new ArrayList<>());
        BoxDefinitionData boxDefinitionData = new BoxDefinitionData();
        final ItemBox     teste             = new ItemBox();
        teste.setName("Rascunho");
        teste.setDescription("Petições de rascunho");
        teste.setIcone(DefaultIcons.DOCS);
        teste.setShowDraft(true);
        teste.setId("1");
//        teste.addAction(DefaultActions.EDIT)
//                .addAction(DefaultActions.VIEW)
//                .addAction(DefaultActions.DELETE);
        teste.setFieldsDatatable(getDatatableFields());
        boxDefinitionData.setRequirements(new ArrayList<>());
        RequirementData req = new RequirementData();
        req.setLabel("Super req");
        req.setId(2L);
        boxDefinitionData.getRequirements().add(req);
        boxDefinitionData.setItemBox(teste);
        box.getBoxesDefinition().add(boxDefinitionData);
        w.getBoxesConfiguration().add(box);
        return w;
    }

    private List<DatatableField> getDatatableFields() {
        List<DatatableField> fields = new ArrayList<>(3);
        fields.add(DatatableField.of("Descrição", "description"));
        fields.add(DatatableField.of("Dt. Edição", "editionDate"));
        fields.add(DatatableField.of("Data de Entrada", "creationDate"));
        return fields;
    }


    private void configBoxesMock() {
        try {
            ModuleEntity moduleEntity = new ModuleEntity();
            new TransactionTemplate(transactionManager).execute((transactionStatus) -> {
                moduleEntity.setName("Grupo Processo Teste");
                moduleEntity.setCod("GRUPO_TESTE");
                moduleEntity.setConnectionURL("http://localhost:8080/rest/nada");
                moduleDAO.saveOrUpdate(moduleEntity);
                return null;
            });
            map.put(moduleEntity, gimmeSomeMock());
        } catch (Exception e) {
            throw SingularException.rethrow(e);
        }
    }

    public Map<ModuleEntity, WorkspaceConfigurationMetadata> getMap() {
        return new LinkedHashMap<>(map);
    }
}
