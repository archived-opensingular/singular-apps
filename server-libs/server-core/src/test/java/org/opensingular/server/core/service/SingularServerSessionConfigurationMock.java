package org.opensingular.server.core.service;

import org.opensingular.flow.persistence.entity.ProcessGroupEntity;
import org.opensingular.lib.wicket.util.resource.Icone;
import org.opensingular.server.commons.WorkspaceConfigurationMetadata;
import org.opensingular.server.commons.flow.actions.DefaultActions;
import org.opensingular.server.commons.persistence.dao.flow.ProcessGroupDAO;
import org.opensingular.server.commons.service.dto.BoxConfigurationData;
import org.opensingular.server.commons.service.dto.BoxDefinitionData;
import org.opensingular.server.commons.service.dto.DatatableField;
import org.opensingular.server.commons.service.dto.ItemBox;
import org.opensingular.server.commons.service.dto.ProcessDTO;
import org.opensingular.server.commons.service.dto.RequirementData;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Primary
@Named
@Scope("session")
public class SingularServerSessionConfigurationMock extends SingularServerSessionConfiguration {

    @Inject
    private ProcessGroupDAO processGroupDAO;

    @Inject
    private PlatformTransactionManager transactionManager;

    @Override
    public void init() {
        configBoxesMock();
    }


    public void configBoxesMock() {
        try {
            new TransactionTemplate(transactionManager).execute((transactionStatus) -> {
                ProcessGroupEntity processGroupEntity = new ProcessGroupEntity();
                processGroupEntity.setName("Grupo Processo Teste");
                processGroupEntity.setCod("GRUPO_TESTE");
                processGroupEntity.setConnectionURL("http://localhost:8080/rest/nada");
                processGroupDAO.saveOrUpdate(processGroupEntity);
                Map<ProcessGroupEntity, WorkspaceConfigurationMetadata> map = new HashMap<>();
                map.put(processGroupEntity, buildTestBoxes());

                setConfigMaps(map);
                return null;
            });

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private WorkspaceConfigurationMetadata buildTestBoxes() {
        WorkspaceConfigurationMetadata w = new WorkspaceConfigurationMetadata();
        w.setBoxesConfiguration(new ArrayList<>());
        BoxConfigurationData box = new BoxConfigurationData();
        box.setId("id-teste-SingularServerSessionConfigurationMock");
        box.setLabel("super caixa");
        box.setProcesses(new ArrayList<>());
        ProcessDTO p = new ProcessDTO();
        p.setName("ajaaja");
        box.getProcesses().add(p);
        box.setBoxesDefinition(new ArrayList<>());
        BoxDefinitionData boxDefinitionData = new BoxDefinitionData();
        final ItemBox     teste             = new ItemBox();
        teste.setName("Rascunho");
        teste.setDescription("Petições de rascunho");
        teste.setIcone(Icone.DOCS);
        teste.setShowNewButton(true);
        teste.setShowDraft(true);
        teste.addAction(DefaultActions.EDIT)
                .addAction(DefaultActions.VIEW)
                .addAction(DefaultActions.DELETE);
        teste.setFieldsDatatable(getDatatableFields());
        boxDefinitionData.setRequirements(new ArrayList<>());
        RequirementData req = new RequirementData();
        req.setLabel("Super req");
        req.setId("superreq");
        boxDefinitionData.getRequirements().add(req);
        boxDefinitionData.setItemBox(teste);
        box.getBoxesDefinition().add(boxDefinitionData);
        box.setProcesses(new ArrayList<>());
        w.getBoxesConfiguration().add(box);
        return w;
    }


    public List<DatatableField> getDatatableFields() {
        List<DatatableField> fields = new ArrayList<>(3);
        fields.add(DatatableField.of("Descrição", "description"));
        fields.add(DatatableField.of("Dt. Edição", "editionDate"));
        fields.add(DatatableField.of("Data de Entrada", "creationDate"));
        return fields;
    }
}
